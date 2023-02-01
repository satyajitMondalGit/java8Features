package com.mindtree.covid.analysis.dto;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestConfirmCaseTO {

	@Pattern(message = "Date formate should be Like yyyy-mm-dd", regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
	private String startDate;

	@Pattern(message = "Date formate should be Like yyyy-mm-dd", regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
	private String endDate;

	private String firstState;
	
	private String SecondState;

}
