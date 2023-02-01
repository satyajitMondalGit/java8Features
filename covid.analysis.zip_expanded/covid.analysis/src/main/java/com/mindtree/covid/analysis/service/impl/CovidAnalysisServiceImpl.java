package com.mindtree.covid.analysis.service.impl;

import java.util.List;

import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;
import com.mindtree.covid.analysis.service.CovidAnalysisService;

public class CovidAnalysisServiceImpl implements CovidAnalysisService {

	@Override
	public List<String> getStatesName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDistrictsName(String stateCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResponseStateDataTO> getStatesDataByDateRange(RequestDateRangeTO dateRangeTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResponseConfirmCaseTO> displayCOnfirmedCasesCompareingTwoStates(
			RequestConfirmCaseTO requestConfirmCaseTo) {
		// TODO Auto-generated method stub
		return null;
	}

}
