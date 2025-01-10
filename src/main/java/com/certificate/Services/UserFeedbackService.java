package com.certificate.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certificate.Models.Cardentials;
import com.certificate.Models.UserFeedback;
import com.certificate.Repos.FeedbackRepo;

@Service
public class UserFeedbackService {

	@Autowired
	private FeedbackRepo feedbackRepo;
	
	@Autowired
	private CardentialsService service;
	
	public void save(UserFeedback feedback,String username) {
		
		Optional<Cardentials> cardentials= service.findByUserEmailOrPhone(username);
		
		 Cardentials cardentials2= cardentials.get();
		
		feedback.setUser(cardentials2.getUser());
		feedbackRepo.save(feedback);
	}
}
