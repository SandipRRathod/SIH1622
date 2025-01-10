package com.certificate.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.certificate.Models.Cardentials;
import com.certificate.Models.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
	
	boolean existsByApplicationId(String applicationID);
	
	@Query(value = "SELECT * FROM certificate WHERE user_email_or_phone= :user_email_or_phone ",nativeQuery = true)
	List<Certificate> findByRegisterdId(@Param("user_email_or_phone") String user_email_or_phone);

}
