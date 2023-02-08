package com.mindtree.covid.analysis.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.service.CovidAnalysisService;


@RestController
public class CovidAnalysisController {
	
	
	@Autowired
	private CovidAnalysisService covidAnalysisService;
	
	
	@GetMapping("/states/")
	ResponseEntity<List<String>> getStatesName(){
		
		
		return new ResponseEntity<>(covidAnalysisService.getStatesName(),HttpStatus.OK);
	}
	
	@GetMapping("/state/{stateCode}/districts/")
	ResponseEntity<List<String>> getDistrictsName(@Valid @PathVariable String stateCode){
		
		
		return new ResponseEntity<>(covidAnalysisService.getDistrictsName(stateCode),HttpStatus.OK);
	}

	@PostMapping("/states/data")
	ResponseEntity<List<ResponseStateDataTO>> getStatesDataByDateRange( @Valid @RequestBody RequestDateRangeTO dateRangeTo ){
		
	
		return new ResponseEntity<>(covidAnalysisService.getStatesDataByDateRange(dateRangeTo),HttpStatus.OK);
	}
	
	@PostMapping("/states/confirmed/data")
	ResponseEntity<List<ResponseConfirmCaseTO>> displayConfirmedCasesCompareingTwoStates( @Valid @RequestBody RequestConfirmCaseTO requestConfirmCaseTo ){
		
		return new ResponseEntity<List<ResponseConfirmCaseTO>>(covidAnalysisService.displayConfirmedCasesCompareingTwoStates(requestConfirmCaseTo),HttpStatus.OK);
	}
	
}
