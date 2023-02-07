package com.mindtree.covid.analysis.dto;

import javax.validation.constraints.NotBlank;
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

	@Pattern(message = "{start.date.error.message}", regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
	private String startDate;

	@Pattern(message = "{end.date.error.message }", regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")
	private String endDate;

	@NotBlank
	private String firstState;
	
	@NotBlank
	private String SecondState;

}
