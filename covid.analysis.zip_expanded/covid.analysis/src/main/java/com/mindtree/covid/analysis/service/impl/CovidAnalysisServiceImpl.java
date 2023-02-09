package com.mindtree.covid.analysis.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.entity.CovidData;
import com.mindtree.covid.analysis.exception.InvalidDateRangeException;
import com.mindtree.covid.analysis.exception.InvalidStateCodeException;
import com.mindtree.covid.analysis.repository.CovidDataRepository;
import com.mindtree.covid.analysis.service.CovidAnalysisService;

@Service
public class CovidAnalysisServiceImpl implements CovidAnalysisService {

	@Autowired
	private CovidDataRepository CovidDataRepo;

	@Autowired
	private MessageSource msgSource;
	
	
	
	@Override
	public List<String> getStatesName() {
//		List<CovidData> covidDataList = CovidDataRepo.findAll();
//
//		return new ArrayList<String>(
//				covidDataList.stream().collect(Collectors.groupingBy(CovidData::getState)).keySet());

		return CovidDataRepo.findAllStates();
	}

	@Override
	public List<String> getDistrictsName(String stateCode) {
//		List<CovidData> covidDataList = CovidDataRepo.findAll();
//
//		Predicate<CovidData> statesAllRecords = c -> c.getState().equalsIgnoreCase(stateCode);
//
//		Optional<CovidData> listOfSatateData = covidDataList.stream().filter(c -> statesAllRecords.test(c)).findAny();
//		if (listOfSatateData.isPresent()) {
//
//			return covidDataList.stream().filter(c -> statesAllRecords.test(c)).map(c -> c.getDistrict()).distinct()
//					.sorted((c1, c2) -> c1.compareTo(c2)).collect(Collectors.toList());
//		} else {
//			throw new InvalidStateCodeException("No Such State present. Please enter a valid State name");
//		}

		List<CovidData> covidDataList = CovidDataRepo.findByState(stateCode);
		if (covidDataList == null || covidDataList.size() == 0) {
			throw new InvalidStateCodeException(msgSource.getMessage("invalid.state.message", null, Locale.ENGLISH));
		}

		return covidDataList.stream().map(c -> c.getDistrict()).distinct().sorted((c1, c2) -> c1.compareTo(c2))
				.collect(Collectors.toList());
	}

	@Override
	public List<ResponseStateDataTO> getStatesDataByDateRange(RequestDateRangeTO dateRangeTo) {
		LocalDate startDate = LocalDate.parse(dateRangeTo.getStartDate());
		LocalDate endDate = LocalDate.parse(dateRangeTo.getEndDate());
		if (startDate.isAfter(endDate)) {
			throw new InvalidDateRangeException(msgSource.getMessage("invalid.date.range.message", null, Locale.ENGLISH));
		}

		List<CovidData> covidDataList = CovidDataRepo.findByDateRange(startDate, endDate);
		if (covidDataList == null || covidDataList.size() < 0) {
			throw new InvalidStateCodeException(msgSource.getMessage("invalid.state.message", null, Locale.ENGLISH));
		}

		Function<CovidData, ResponseStateDataTO> convertFunc = c -> new ResponseStateDataTO(c.getDate(), c.getState(),
				Integer.valueOf(c.getTested()), Integer.valueOf(c.getConfirmed()), Integer.valueOf(c.getRecovered()));

		List<ResponseStateDataTO> opList = covidDataList.stream().map(c -> convertFunc.apply(c))
				.collect(Collectors.groupingBy(r -> r.getDate() + r.getState())).entrySet().stream()
				.map(e -> e.getValue().stream().reduce((f1, f2) -> new ResponseStateDataTO(f1.getDate(), f1.getState(),
						f1.getTestedTotal() + f2.getTestedTotal(), f1.getConfirmedTotal() + f2.getConfirmedTotal(),
						f1.getRecoveredTotal() + f2.getRecoveredTotal())).orElse(null))
				.filter(r -> r != null).sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
				.collect(Collectors.toList());
		return opList;
	}

	@Override
	public List<ResponseConfirmCaseTO> displayConfirmedCasesCompareingTwoStates(
			RequestConfirmCaseTO requestConfirmCaseTo) {

		LocalDate startDate = LocalDate.parse(requestConfirmCaseTo.getStartDate());
		LocalDate endDate = LocalDate.parse(requestConfirmCaseTo.getEndDate());
		String firstSt = requestConfirmCaseTo.getFirstState();
		String secondSt = requestConfirmCaseTo.getSecondState();
		if (startDate.isAfter(endDate)) {
			throw new InvalidDateRangeException(msgSource.getMessage("invalid.date.range.message", null, Locale.ENGLISH));
		}

		List<CovidData> covidDataList = CovidDataRepo.findByStatesAndDateRange(firstSt, secondSt, startDate, endDate);
		if (covidDataList == null || covidDataList.size() < 0) {
			throw new InvalidStateCodeException(msgSource.getMessage("invalid.state.message", null, Locale.ENGLISH));
		}
		System.out.println(covidDataList.size());

		Function<List<CovidData>, ResponseConfirmCaseTO> convertFunc = list -> {
			LocalDate d = list.get(0).getDate();
			int fConfirm = 0;
			int sconFirm = 0;
			for (CovidData c : list) {
				if (c.getState().equalsIgnoreCase(firstSt)) {
					fConfirm = Integer.valueOf(c.getConfirmed());
				}
				if (c.getState().equalsIgnoreCase(secondSt)) {
					sconFirm = Integer.valueOf(c.getConfirmed());
				}
			}
			return new ResponseConfirmCaseTO(d, firstSt, fConfirm, secondSt, sconFirm);
		};

		BiFunction<String, String, String> addConfirmFunc = (s1, s2) -> String
				.valueOf(Integer.valueOf(s1) + Integer.valueOf(s2));

		List<ResponseConfirmCaseTO> opList = covidDataList.stream()
				.collect(Collectors.groupingBy(r -> r.getDate() + r.getState())).entrySet().stream()
				.map(e -> e.getValue().stream()
						.reduce((f1, f2) -> new CovidData(f1.getId(), f1.getDate(), f1.getState(), f1.getDistrict(),
								f1.getTested(), addConfirmFunc.apply(f1.getConfirmed(), f2.getConfirmed()),
								f1.getRecovered()))
						.orElse(null))
				.filter(r -> r != null).collect(Collectors.groupingBy(CovidData::getDate)).entrySet().stream()
				.map(e -> convertFunc.apply(e.getValue())).collect(Collectors.toList());
		return opList;
	}

}
