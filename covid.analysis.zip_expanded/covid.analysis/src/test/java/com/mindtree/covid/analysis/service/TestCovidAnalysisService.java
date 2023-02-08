package com.mindtree.covid.analysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.entity.CovidData;
import com.mindtree.covid.analysis.repository.CovidDataRepository;

@SpringBootTest
public class TestCovidAnalysisService {

	@MockBean
	CovidDataRepository CovidDataRepo;

	@Autowired
	CovidAnalysisService covidAnalysisService;

	List<CovidData> dataListWB = new ArrayList<>();

	List<CovidData> dataListOR = new ArrayList<>();

	List<CovidData> covidDataList = new ArrayList<>();

	@BeforeEach
	public void setup() {
		dataListWB.add(new CovidData(1, LocalDate.parse("2020-08-07"), "WB", "Kolkata", "100", "20", "10"));
		dataListWB.add(new CovidData(1, LocalDate.parse("2020-08-07"), "WB", "Hooghly", "200", "40", "20"));
		dataListWB.add(new CovidData(1, LocalDate.parse("2020-08-08"), "WB", "Darjeeling", "300", "60", "30"));
		dataListWB.add(new CovidData(1, LocalDate.parse("2020-08-08"), "WB", "Howrah", "400", "80", "40"));

		dataListOR.add(new CovidData(1, LocalDate.parse("2020-08-07"), "OR", "Puri", "100", "30", "15"));
		dataListOR.add(new CovidData(1, LocalDate.parse("2020-08-07"), "OR", "Konarock", "200", "60", "30"));
		dataListOR.add(new CovidData(1, LocalDate.parse("2020-08-08"), "OR", "Bhubaneswar", "400", "120", "10"));
		dataListOR.add(new CovidData(1, LocalDate.parse("2020-08-08"), "OR", "Mayurbhanj", "100", "20", "10"));

		covidDataList.addAll(dataListWB);
		covidDataList.addAll(dataListOR);
	}

	@Test
	public void testGetStatesName() {

		List<String> states = new ArrayList<>();
		states.add("WB");
		states.add("OR");
		when(CovidDataRepo.findAllStates()).thenReturn(states);
		assertEquals(states, covidAnalysisService.getStatesName());

	}

	@Test
	public void testGetDistrictsName() {
		List<String> dists = new ArrayList<>();
		dists.add("Darjeeling");
		dists.add("Hooghly");
		dists.add("Howrah");
		dists.add("Kolkata");

		when(CovidDataRepo.findByState("WB")).thenReturn(dataListWB);
		assertEquals(dists, covidAnalysisService.getDistrictsName("WB"));
	}

	@Test
	public void testGetStatesDataByDateRange() {

		RequestDateRangeTO dateRangeTo = new RequestDateRangeTO("2020-08-07", "2020-08-08");
		when(CovidDataRepo.findByDateRange(LocalDate.parse(dateRangeTo.getStartDate()),
				LocalDate.parse(dateRangeTo.getEndDate()))).thenReturn(covidDataList);

		assertEquals(4, covidAnalysisService.getStatesDataByDateRange(dateRangeTo).size());
	}

	@Test
	public void testDisplayConfirmedCasesCompareingTwoStates() {
		RequestConfirmCaseTO requestConfirmCaseTo = new RequestConfirmCaseTO("2020-08-07", "2020-08-08", "WB", "OR");
		when(CovidDataRepo.findByStatesAndDateRange(requestConfirmCaseTo.getFirstState(),
				requestConfirmCaseTo.getSecondState(), LocalDate.parse(requestConfirmCaseTo.getStartDate()),
				LocalDate.parse(requestConfirmCaseTo.getEndDate()))).thenReturn(covidDataList);

		assertEquals(2, covidAnalysisService.displayConfirmedCasesCompareingTwoStates(requestConfirmCaseTo).size());
	}
}
