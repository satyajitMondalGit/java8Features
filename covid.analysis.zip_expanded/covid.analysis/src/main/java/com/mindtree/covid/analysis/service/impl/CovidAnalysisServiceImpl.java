package com.mindtree.covid.analysis.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.entity.CovidData;
import com.mindtree.covid.analysis.exception.StateNotFoundException;
import com.mindtree.covid.analysis.repository.CovidDataRepository;
import com.mindtree.covid.analysis.service.CovidAnalysisService;

public class CovidAnalysisServiceImpl implements CovidAnalysisService {

	@Autowired
	private CovidDataRepository CovidDataRepo;

	@Override
	public List<String> getStatesName() {
		List<CovidData> covidDataList = CovidDataRepo.findAll();

		return new ArrayList<String>(
				covidDataList.stream().collect(Collectors.groupingBy(CovidData::getState)).keySet());
	}

	@Override
	public List<String> getDistrictsName(String stateCode) {
		List<CovidData> covidDataList = CovidDataRepo.findAll();

		Predicate<CovidData> statesAllRecords = c -> c.getState().equalsIgnoreCase(stateCode);
		Stream<CovidData> stateRecords = covidDataList.stream().filter(c -> statesAllRecords.test(c));
		if (stateRecords.count() > 0) {
			return new ArrayList<String>(stateRecords.collect(Collectors.groupingBy(CovidData::getDistrict)).keySet());
		} else {
			throw new StateNotFoundException("No Such State is in the Lst");
		}

	}

	@Override
	public List<ResponseStateDataTO> getStatesDataByDateRange(RequestDateRangeTO dateRangeTo) {
		List<CovidData> covidDataList = CovidDataRepo.findAll();

		Predicate<LocalDate> datainDateRangePredi = (d) -> {

			LocalDate startDate = LocalDate.parse(dateRangeTo.getStartDate());
			LocalDate endDate = LocalDate.parse(dateRangeTo.getEndDate());
			return d.isAfter(startDate) && d.isBefore(endDate) ? true : false;
		};

		Function<CovidData, ResponseStateDataTO> convertFunc = c -> new ResponseStateDataTO(c.getDate(), c.getState(),
				Integer.valueOf(c.getConfirmed()));
		Function<ResponseStateDataTO, String> groupingKey = r -> r.getDate() + r.getState();
		BiFunction<String, Integer, ResponseStateDataTO> resultList = (K,
				V) -> new ResponseStateDataTO(LocalDate.parse(K.substring(0, 10)), K.substring(10), V);

		Map<String, Integer> resultMap = covidDataList.stream().filter(c -> datainDateRangePredi.test(c.getDate()))
				.map(c -> convertFunc.apply(c)).collect(Collectors.groupingBy(r -> groupingKey.apply(r),
						Collectors.summingInt(ResponseStateDataTO::getConfirmedTotal)));

		List<ResponseStateDataTO> opList = new ArrayList<>();
		for (Entry<String, Integer> e : resultMap.entrySet()) {
			opList.add(resultList.apply(e.getKey(), e.getValue()));
		}

		return opList;
	}

	@Override
	public List<ResponseConfirmCaseTO> displayConfirmedCasesCompareingTwoStates(
			RequestConfirmCaseTO requestConfirmCaseTo) {
		// TODO Auto-generated method stub
		return null;
	}

}
