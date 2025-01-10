package com.certificate.Models;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Certificate {
	    @Id
	    private String applicationId;
	    private String applicantName;
	    private String certiName;
	    private String certiStatus;
	    private String rejectionStatus;
	    private String paymentStatus;
	    
	    private String userEmailOrPhone;
	    
	    @ManyToOne
	    @JoinColumn(name = "user_certificate_id")
	    @JsonBackReference
	    private User user;    
}
