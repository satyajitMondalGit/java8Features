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
public class CovidDataTO {

	private int id;

	private LocalDate date;

	private String state;

	private String district;

	private String tested;

	private String confirmed;

	private String recovered;

}
