package com.certificate.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Cardentials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cardentialId;
    private String userEmailOrPhone;
    private String userPassword;

	@OneToOne(mappedBy = "cardentials",cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;
	  
}
