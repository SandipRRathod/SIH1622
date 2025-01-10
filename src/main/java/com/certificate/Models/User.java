package com.certificate.Models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;
    private String fatherName;
    private String motherName;
    private int userAge;
    private String userDob;
    private long userMob;
    private String userEmail;
    private String userGender;
    private String userCast;
    private long userAdhar;
    private String userPan;
    private String userNationlity;
    private String userOccup;
    private String userVillage;

	private String userDist;
    private String userTq;
    private String userState;
    private int userPincode;
    
    @OneToOne
    @JoinColumn(name = "cardentialId")
    @JsonBackReference
    private Cardentials cardentials;
    
    
    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Certificate> certificates;
   
    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserFeedback> feedbacks;
   

}
