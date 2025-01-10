package com.certificate.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class IncomeApplication {

	@Id
	private String applicationID;
	private String incomeFor;
	
	private String relation;
	private String solutation;
	private String benificaryName;
    private String certiName;
    private String paymentStatus;
	private String certiReasion;
	private String certiStatus;
	private String rejectionStatus;
	
	private String farmaccHolderName;
	private float farmArea;
	private String area;
	
	private String annualThirdIncome;
	private String annualSecondIncome;
	private String annualIncome;
	
	private String registerdId;
    
	private String identityProof;
	private String addressProof;
	private String proofOfIncome;


}
