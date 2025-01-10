package com.certificate.Models;

import javax.swing.GroupLayout.Alignment;

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
public class UserFeedback {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String userFName;
	private String userEmail;
	private String userMN;
	private String subject;
	private String massage;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userfeedbackId",referencedColumnName = "userId")
	@JsonBackReference
	private User user;
	
}
