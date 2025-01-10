package com.certificate.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certificate.Models.UserFeedback;

@Repository
public interface FeedbackRepo extends JpaRepository<UserFeedback, Long> {

}
