package com.certificate.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CasteApplication {
	@Id
	private String applicationID;
	private String relation;
	private String solutation;
	private String benificaryName;
	private String certiName;
	private String casteType;
	private String paymentStatus;
	private String gender;
	private String certiStatus;
	private String rejectionStatus;
	private String registerdId;

	private String identityProof;
	private String addressProof;
	private String casteProof;
	private String birthProof;


}
