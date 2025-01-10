package com.certificate.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.certificate.Models.Cardentials;



@Repository
public interface CardentialsRepo extends JpaRepository<Cardentials, Integer> {

	//created custome query 
	@Query("SELECT c FROM Cardentials c WHERE c.userEmailOrPhone = :userEmailOrPhone AND c.userPassword = :userPassword")
	Cardentials findByUserEmailOrPhoneAnduserPassword(@Param("userEmailOrPhone")String userEmailOrPhone,@Param("userPassword") String userPassword);

	//created custome query for authentication
	@Query("SELECT c FROM Cardentials c WHERE c.userEmailOrPhone = :userEmailOrPhone")
	Optional<Cardentials> findByUserEmailOrPhone(@Param("userEmailOrPhone") String userEmailOrPhone);
	
	Optional<Cardentials> findByUserPassword(String userEmailOrPhone);
}
