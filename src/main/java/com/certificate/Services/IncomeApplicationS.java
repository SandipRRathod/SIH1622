package com.certificate.Services;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certificate.Models.Cardentials;
import com.certificate.Models.Certificate;
import com.certificate.Models.IncomeApplication;
import com.certificate.Repos.CertificateRepository;
import com.certificate.Repos.IncomeApplicationRepo;

@Service
public class IncomeApplicationS {

	@Autowired
	private IncomeApplicationRepo incomeApplicationRepo;

	@Autowired
	private CertificateRepository certificateRepository;

	@Autowired
	private CardentialsService cardentialsService;

	public void save(IncomeApplication application) {
		
		Cardentials cardentials= cardentialsService.findByUserEmailOrPhone(application.getRegisterdId()).orElse(null);

		if (cardentials!=null) {

			Certificate certificate = new Certificate();

			certificate.setUserEmailOrPhone(application.getRegisterdId());
			certificate.setApplicationId(application.getApplicationID());
			certificate.setApplicantName(application.getBenificaryName());
			certificate.setCertiName(application.getCertiName());
			certificate.setCertiStatus(application.getCertiStatus());
			certificate.setRejectionStatus("-");
			certificate.setPaymentStatus(application.getPaymentStatus());
			
			
			certificate.setUser(cardentials.getUser());

			certificateRepository.save(certificate);

			this.incomeApplicationRepo.save(application);
			

		} 
	}

	public List<IncomeApplication> getAll() {
		return incomeApplicationRepo.findAll();
	}


	public String generateApplicationId() {
		String applicationID;
		do {
			String id = "INCOME" + (long) (new Random().nextDouble() * 900000000000L);
			applicationID = id;
		} while (incomeApplicationRepo.existsByApplicationID(applicationID)); // Ensure uniqueness

		return applicationID;
	}

}
