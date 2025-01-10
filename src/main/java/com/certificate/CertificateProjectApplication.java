package com.certificate;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import com.certificate.Controller.Controller;
import com.certificate.Models.Cardentials;
import com.certificate.Models.IncomeApplication;
import com.certificate.Services.CardentialsService;
import com.certificate.Services.IncomeApplicationS;

@SpringBootApplication
public class CertificateProjectApplication implements CommandLineRunner {
	
//	@Autowired
//	private IncomeApplicationS applicationS;
//	
	@Autowired
	private CardentialsService cardentialsService;
//	
//	@Autowired
//	private Controller controller;
	

	public static void main(String[] args) {
		SpringApplication.run(CertificateProjectApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
	
//		IncomeApplication application= new IncomeApplication();
//		
//		application.setApplicationID(applicationS.generateApplicationId());
//		
//		application.setCertiName("Income");
//		application.setRegisterdId("7666883677");
//		application.setBenificaryName("Sandip Raju Rathod");
//		
//		applicationS.save(application);
		
		
//		Cardentials cardentials= cardentialsService.findByUserEmailOrPhone("7666883677").orElse(null);
//		
//		System.out.println(cardentials);
		
//		controller.getCardentials("7666883677");
		
		
//		Cardentials cardentials= new Cardentials();
//		cardentials.setUserEmailOrPhone("sandiprathod2667@gmail.com");
//		cardentials.setUserPassword("1");
//		
//		cardentialsService.saveCardentials(cardentials);
		
//		cardentialsService.changeUsername("sandiprathod", "1", "sandip");
		
	}

}
