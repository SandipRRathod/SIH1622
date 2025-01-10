package com.certificate.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ANADApplication {

	@Id
	private String applicationID;
	private String benificaryName;
	private String solutation;
	private String relation;
	private String certiName;
	private String certiType;
	private String certiReasion;
	private String residingSince;
	private String paymentStatus;
	private String certiStatus;
	private String rejectionStatus;
	
	private String registerdId;
	
	private String identityProof;
	private String addressProof;
	private String birthProof;
	
	
	
}
