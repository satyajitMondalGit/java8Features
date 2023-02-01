package com.mindtree.covid.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindtree.covid.analysis.entity.CovidData;

public interface CovidAnalysis extends JpaRepository<CovidData, Integer> {

}
