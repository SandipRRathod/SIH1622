package com.certificate.Services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certificate.Models.Cardentials;
import com.certificate.Models.CasteApplication;
import com.certificate.Models.Certificate;
import com.certificate.Models.IncomeApplication;
import com.certificate.Repos.CasteRepo;
import com.certificate.Repos.CertificateRepository;

@Service
public class CasteService {

	@Autowired
	private CasteRepo casteRepo;
	
	@Autowired
	private CertificateRepository certificateRepository;
	
	@Autowired
	private CardentialsService cardentialsService;

	public void save(CasteApplication application) {
		
		Cardentials cardentials= cardentialsService.findByUserEmailOrPhone(application.getRegisterdId()).orElse(null);

		if (cardentials!=null) {
		Certificate certificate= new Certificate();
		certificate.setUserEmailOrPhone(application.getRegisterdId());
		certificate.setApplicationId(application.getApplicationID());
		certificate.setApplicantName(application.getBenificaryName());
		certificate.setCertiName(application.getCertiName());
		certificate.setCertiStatus(application.getCertiStatus());
		certificate.setRejectionStatus("-");
		certificate.setPaymentStatus(application.getPaymentStatus());
		
		certificate.setUser(cardentials.getUser());
		
		certificateRepository.save(certificate);
		
		 this.casteRepo.save(application);
		}
		
		
	}
	
	
	
	 public String generateApplicationId() {
	        String applicationID;
	        do {
	            String id = "CASTE"+(long) (new Random().nextDouble() * 900000000000L);
	            applicationID = id;
	        } while (casteRepo.existsByApplicationID(applicationID));  // Ensure uniqueness
	        
	        return applicationID;
	    }


	public List<CasteApplication> getAll() {
		
		return casteRepo.findAll();
	}
	
	
	
}
