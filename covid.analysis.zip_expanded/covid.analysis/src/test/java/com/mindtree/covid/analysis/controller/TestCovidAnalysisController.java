package com.mindtree.covid.analysis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.service.CovidAnalysisService;

@WebMvcTest(CovidAnalysisController.class)
public class TestCovidAnalysisController {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CovidAnalysisService covidAnalysisService;

	@Test
	public void testGetStatesName() throws Exception {
		List<String> stateList = new ArrayList<String>();
		stateList.add("DL");
		stateList.add("WB");
		when(covidAnalysisService.getStatesName()).thenReturn(stateList);

		MockHttpServletResponse response = mvc.perform(get("/states/").accept(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		assertEquals(response.getStatus(), HttpStatus.OK.value());

		String expected = "[\"DL\",\"WB\"]";

		JSONAssert.assertEquals(expected, response.getContentAsString(), true);
	}

	@Test
	public void testGetDistrictsName() throws Exception {
		List<String> distList = new ArrayList<String>();
		distList.add("Kolkata");
		distList.add("Hooghly");
		distList.add("Darjeeling");
		when(covidAnalysisService.getDistrictsName("WB")).thenReturn(distList);

		MockHttpServletResponse response = mvc
				.perform(get("/state/{stateCode}/districts/", "WB").accept(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		assertEquals(response.getStatus(), HttpStatus.OK.value());

		String expected = "[\"Kolkata\",\"Hooghly\",\"Darjeeling\"]";

		JSONAssert.assertEquals(expected, response.getContentAsString(), true);
	}

	@Test
	public void testGetStatesDataByDateRange() throws JsonProcessingException, Exception {
		RequestDateRangeTO dateRangeTo = new RequestDateRangeTO("2020-10-07", "2020-10-08");
		List<ResponseStateDataTO> responseList = new ArrayList<>();
		responseList.add(new ResponseStateDataTO(LocalDate.parse("2020-10-07"), "DL", 10, 10, 10));
		responseList.add(new ResponseStateDataTO(LocalDate.parse("2020-10-08"), "DL", 12, 12, 12));

		when(covidAnalysisService.getStatesDataByDateRange(dateRangeTo)).thenReturn(responseList);

		MockHttpServletResponse response = mvc
				.perform(post("/states/data").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(dateRangeTo)).accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.OK.value());

		String expected = "[{\"date\":\"2020-10-07\",\"state\":\"DL\",\"testedTotal\":10,\"confirmedTotal\":10,\"recoveredTotal\":10},"
				+ "{\"date\":\"2020-10-08\",\"state\":\"DL\",\"testedTotal\":12,\"confirmedTotal\":12,\"recoveredTotal\":12}]";

		JSONAssert.assertEquals(expected, response.getContentAsString(), true);
	}

	@Test
	public void testDisplayConfirmedCasesCompareingTwoStates() throws JsonProcessingException, Exception {

		RequestConfirmCaseTO requestConfirmCaseTo = new RequestConfirmCaseTO("2020-10-07", "2020-10-08", "DL", "WB");
		List<ResponseConfirmCaseTO> listResult = new ArrayList<>();
		listResult.add(new ResponseConfirmCaseTO(LocalDate.parse("2020-10-07"), "DL", 100, "WB", 200));
		listResult.add(new ResponseConfirmCaseTO(LocalDate.parse("2020-10-08"), "DL", 200, "WB", 300));

		when(covidAnalysisService.displayConfirmedCasesCompareingTwoStates(requestConfirmCaseTo))
				.thenReturn(listResult);

		MockHttpServletResponse response = mvc
				.perform(post("/states/confirmed/data").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(requestConfirmCaseTo))
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.OK.value());

		String expected = "[{\"date\":\"2020-10-07\",\"firstState\":\"DL\",\"firstStateConfirmedTotal\":100,"
				+ "\"secondState\":\"WB\",\"secondStateConfirmedTotal\":200},{\"date\":\"2020-10-08\","
				+ "\"firstState\":\"DL\",\"firstStateConfirmedTotal\":200,\"secondState\":\"WB\","
				+ "\"secondStateConfirmedTotal\":300}]";

		JSONAssert.assertEquals(expected, response.getContentAsString(), true);

	}

}
