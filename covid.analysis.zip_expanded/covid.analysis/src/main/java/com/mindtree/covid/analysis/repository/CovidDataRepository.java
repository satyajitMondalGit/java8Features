package com.mindtree.covid.analysis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mindtree.covid.analysis.entity.CovidData;

public interface CovidDataRepository extends JpaRepository<CovidData, Integer> {
	
	@Query("select distinct c.state from CovidData c")
	List<String> findAllStates();
	
//	@Query("from CovidData c where c.state = :state")
	Optional<List<CovidData>> findByState(String state);

}
