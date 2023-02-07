package com.mindtree.covid.analysis.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.entity.CovidData;
import com.mindtree.covid.analysis.exception.InvalidStateCodeException;
import com.mindtree.covid.analysis.repository.CovidDataRepository;
import com.mindtree.covid.analysis.service.CovidAnalysisService;

@Service
public class CovidAnalysisServiceImpl implements CovidAnalysisService {

	@Autowired
	private CovidDataRepository CovidDataRepo;
	
	@Value("${invalidStateCode}")
	String invaidStateCode;

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

		List<CovidData> covidDataList = CovidDataRepo.findByState("stateCode").orElseThrow(()->new InvalidStateCodeException(invaidStateCode));
		
		System.out.println(covidDataList.size());
			
		return covidDataList.stream().map(c -> c.getDistrict()).distinct().sorted((c1, c2) -> c1.compareTo(c2)).collect(Collectors.toList());
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
//		
//		List<CovidData> covidDataList = CovidDataRepo.findAll();
//		
//		BiPredicate<LocalDate, String> validRecordsFilter = (l,s)->{
//			boolean isInsideDate = false;
//			boolean isStateValid = false;
//			
//			LocalDate startDate = LocalDate.parse(requestConfirmCaseTo.getStartDate());
//			LocalDate endDate = LocalDate.parse(requestConfirmCaseTo.getEndDate());
//			
//			isInsideDate =  l.isAfter(startDate) && l.isBefore(endDate) ? true : false;
//			
//			if(s.equalsIgnoreCase(requestConfirmCaseTo.getFirstState())
//					||s.equalsIgnoreCase(requestConfirmCaseTo.getSecondState())) {
//				isStateValid =true;
//			}
//			
//			return isInsideDate && isStateValid;
//		};
//		
//		Function<List<CovidData>, ResponseConfirmCaseTO> responsemapper = (L) -> {
//			
//			
//			LocalDate date = L.get(0).getDate();
//			String fs;
//			String ss;
//			
//			if(L.get(0).getState().equalsIgnoreCase(requestConfirmCaseTo.getFirstState()))
//			
//			
//			return new ResponseConfirmCaseTO(L.get(0).getDate(),L.get(0).getState(),)
//		}
//		
//	//	Map<LocalDate,List<List<CovidData>>> mapResult =
//				covidDataList.stream()
//				.filter(c->validRecordsFilter.test(c.getDate(), c.getState()))
//				.collect(Collectors.groupingBy(CovidData::getDate))
//				.entrySet().stream()
//				.map(e -> e.getValue().stream().collect(Collectors.groupingBy(CovidData::getState))
//							
//							.entrySet().stream().map( g -> g.getValue().stream()
//									.reduce((f1, f2) -> new CovidData(f1.getId(),f1.getDate(),
//											f1.getState(),f1.getDistrict(), f1.getTested(),
//											f1.getConfirmed()+f2.getConfirmed(),f1.getRecovered()))))
//				.map(mapper)
//							
//				
//				
//				;
//		
		return null;
	}

}
