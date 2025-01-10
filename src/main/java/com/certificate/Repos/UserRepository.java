package com.certificate.Repos;

import java.util.Optional;
import java.util.Random;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.certificate.Models.Cardentials;
import com.certificate.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	
	@Query("SELECT c FROM User c WHERE c.userEmail = :userEmailOrPhone")
	Optional<User> findByEmail(@Param("userEmailOrPhone") String userEmailOrPhone);
	
	@Query("SELECT c FROM User c WHERE c.userMob = :userEmailOrPhone")
	Optional<User> findByPhone(@Param("userEmailOrPhone") String userEmailOrPhone );
	
	@Query("SELECT c FROM User c WHERE c.userMob = :userEmailOrPhone")
	Optional<User> findByPhone(@Param("userEmailOrPhone") long userEmailOrPhone );
	
	@Query("SELECT c FROM User c WHERE c.userEmail = :userEmailOrPhone")
     User findByEmailForUpdate(@Param("userEmailOrPhone") String userEmailOrPhone);
	
	@Query("SELECT c FROM User c WHERE c.userMob = :userEmailOrPhone")
     User findByPhoneForUpdate(@Param("userEmailOrPhone") String userEmailOrPhone );
	

	
//	@Query("SELECT u,c FROM User u,Credential c  where u.user_email=c.user_email_or_phone")
//	Optional<User> findByEmail(@Param("userEmailOrPhone") String userEmailOrPhone );
}
