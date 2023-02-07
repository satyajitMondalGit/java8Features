package com.mindtree.covid.analysis.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mindtree.covid.analysis.entity.CovidData;

public interface CovidDataRepository extends JpaRepository<CovidData, Integer> {
	
	@Query("select distinct c.state from CovidData c")
	List<String> findAllStates();
	
	@Query("select c  from CovidData c where c.state = :state ")
	List<CovidData> findByState(String state);

	@Query("Select c from CovidData c where c.date between :start and :end ")
	List<CovidData> findByDateRange(LocalDate start, LocalDate end);
	
	
	@Query("Select c from CovidData c where c.state IN (:fState, :sState) and c.date between :start and :end ")
	List<CovidData> findByStatesAndDateRange(String fState, String sState, LocalDate start, LocalDate end);
	
}
