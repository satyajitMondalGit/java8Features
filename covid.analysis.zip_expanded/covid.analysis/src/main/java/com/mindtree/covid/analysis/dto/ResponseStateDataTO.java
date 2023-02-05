package com.mindtree.covid.analysis.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStateDataTO {

	
	private LocalDate date;
	private String state;
	private int confirmedTotal;
}
