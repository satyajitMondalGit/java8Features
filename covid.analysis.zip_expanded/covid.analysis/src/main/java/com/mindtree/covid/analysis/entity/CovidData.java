package com.mindtree.covid.analysis.entity;

import java.time.LocalDate;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "covid_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CovidData implements Comparable<CovidData> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "state")
	private String state;

	@Column(name = "district")
	private String district;

	@Column(name = "tested")
	private String tested;

	@Column(name = "confirmed")
	private String confirmed;

	@Column(name = "recovered")
	private String recovered;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CovidData other = (CovidData) obj;

		if (id != other.id)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;

		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;

		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;

		if (tested == null) {
			if (other.tested != null)
				return false;
		} else if (!tested.equals(other.tested))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((district == null) ? 0 : district.hashCode());
		result = prime * result + ((tested == null) ? 0 : tested.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public int compareTo(CovidData arg0) {

		return Comparator.comparingInt(CovidData::getId).thenComparing(CovidData::getState)
				.thenComparing(CovidData::getDistrict).thenComparing(CovidData::getDate)
				.thenComparing(CovidData::getTested).compare(this, arg0);

	}

}
