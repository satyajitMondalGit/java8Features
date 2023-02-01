package com.mindtree.covid.analysis.service;

import java.util.List;

import com.mindtree.covid.analysis.dto.RequestConfirmCaseTO;
import com.mindtree.covid.analysis.dto.RequestDateRangeTO;
import com.mindtree.covid.analysis.dto.ResponseConfirmCaseTO;
import com.mindtree.covid.analysis.dto.ResponseStateDataTO;

public interface CovidAnalysisService {

	List<String> getStatesName();

	List<String> getDistrictsName(String stateCode);

	List<ResponseStateDataTO> getStatesDataByDateRange(RequestDateRangeTO dateRangeTo);

	List<ResponseConfirmCaseTO> displayCOnfirmedCasesCompareingTwoStates(RequestConfirmCaseTO requestConfirmCaseTo);

}
