package com.certificate.Services;

import java.security.cert.Certificate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.certificate.Models.Cardentials;
import com.certificate.Models.User;
import com.certificate.Repos.CardentialsRepo;

@Service
public class CardentialsService {

	//JpaRepository class object for database
	@Autowired
    private CardentialsRepo cardentialsRepo;
	
	
	//PasswordEncoder for getting original password
	 @Autowired
	 private PasswordEncoder passwordEncoder; 
	 
	 
	 
	@Autowired
	private CertificateService certificateService;
	 
    //saving credentials
    public Cardentials saveCardentials(Cardentials c) {
        return this.cardentialsRepo.save(c);
    }

    //for authentication and etc
    public Optional<Cardentials> findByUserEmailOrPhone(String userEmailOrPhone) {
        return this.cardentialsRepo.findByUserEmailOrPhone(userEmailOrPhone);
    }
    

    public Optional<Cardentials> authenticate(String userEmailOrPhone, String userPassword) {
        // Find the user by email or phone
        Optional<Cardentials> cardentialsOpt = this.cardentialsRepo.findByUserEmailOrPhone(userEmailOrPhone);

        if (cardentialsOpt.isPresent()) {
            Cardentials cardentials = cardentialsOpt.get();
            
          //encoding password for authentication
            if (passwordEncoder.matches(userPassword, cardentials.getUserPassword())) {
//            	if matches then return 
                return Optional.of(cardentials);
            }
        }
        // else return no user
        return Optional.empty();
    }
    
    public Cardentials changeUsername(String oldUsername, String oldPassword, String newUsername) {
        // Find the user by old username
        Cardentials existingCardentials = cardentialsRepo.findByUserEmailOrPhone(oldUsername).orElse(null);
        
        // Validate if the user exists
        if (existingCardentials == null) {
            return null; // User not found
        }

        // Validate old password
        if (!passwordEncoder.matches(oldPassword, existingCardentials.getUserPassword())) {
            return null; // Password does not match
        }

        // Update the username
        existingCardentials.setUserEmailOrPhone(newUsername);
        User user =existingCardentials.getUser();
        
        List<com.certificate.Models.Certificate> certificate=user.getCertificates();
        
        certificate.forEach(t -> t.setUserEmailOrPhone(newUsername));
        
        
        // Save updated user credentials
        return cardentialsRepo.save(existingCardentials);
    }


    public Cardentials changePassword(String oldUsername, String oldPassword, String newPassword) {
    	 // Same As Above changeUsername()
        Cardentials existingCardentials = cardentialsRepo.findByUserEmailOrPhone(oldUsername).orElse(null);
        

        if (existingCardentials == null) {
            return null; 
        }

        if (!passwordEncoder.matches(oldPassword, existingCardentials.getUserPassword())) {
            return null; // Password does not match
        }

        
        //update the password
        existingCardentials.setUserPassword(passwordEncoder.encode(newPassword));

        return cardentialsRepo.save(existingCardentials);
    }
}
