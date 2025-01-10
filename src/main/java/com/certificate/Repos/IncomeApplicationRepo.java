package com.certificate.Repos;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.certificate.Models.IncomeApplication;

@Repository
public interface IncomeApplicationRepo extends JpaRepository<IncomeApplication, String> {

	boolean existsByApplicationID(String applicationID);
	
}
