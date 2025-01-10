package com.certificate.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.certificate.Models.ANADApplication;

@Repository
public interface ANADApplicationRepo extends JpaRepository<ANADApplication, String> {

	boolean existsByApplicationID(String applicationID);
}
