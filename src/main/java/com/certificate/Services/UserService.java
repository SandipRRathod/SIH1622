package com.certificate.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certificate.Models.Cardentials;
import com.certificate.Models.Certificate;
import com.certificate.Models.User;
import com.certificate.Repos.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private CardentialsService cardentialsService;

	// saving user
	public User saveUser(User user, String username) {

		Cardentials cardentials = cardentialsService.findByUserEmailOrPhone(username)
				.orElseThrow(() -> new RuntimeException("User Not Found With " + username));

		user.setCardentials(cardentials);

		user = repository.save(user);

		return user;
	}

	// getting user by by cardentials if the cardentials are present then only user
	// can get
	public User getUser(String username) {

		Cardentials cardentials = cardentialsService.findByUserEmailOrPhone(username)
				.orElseThrow(() -> new RuntimeException("User Not Found With " + username));

		User user = cardentials.getUser();

		return user;
	}

//	public Optional<User> getUser(String name) {
//		return repository.findByName(name);
//	}

	//getting all Certificates
	public List<Certificate> getCertificates(String username) {
		Cardentials cardentials = cardentialsService.findByUserEmailOrPhone(username).orElseThrow(() -> new RuntimeException("User Not found"));
		
		User user = cardentials.getUser();
		return user.getCertificates();
	}

	//updating user details
	public User updateUser(String id, User user) {
		
		 Cardentials cardentials =cardentialsService.findByUserEmailOrPhone(id).orElseThrow(() -> new RuntimeException("Not Found With This Id"));

		 User userUpadated=cardentials.getUser();

		userUpadated.setUserName(user.getUserName());
		userUpadated.setUserEmail(user.getUserEmail());
		userUpadated.setUserMob(user.getUserMob());
		userUpadated.setFatherName(user.getFatherName());
		userUpadated.setMotherName(user.getMotherName());
		userUpadated.setUserAdhar(user.getUserAdhar());
		userUpadated.setUserAge(user.getUserAge());
		userUpadated.setUserDist(user.getUserDist());
		userUpadated.setUserCast(user.getUserCast());
		userUpadated.setUserDob(user.getUserDob());
		userUpadated.setUserGender(user.getUserGender());
		userUpadated.setUserNationlity(user.getUserNationlity());
		userUpadated.setUserOccup(user.getUserOccup());
		userUpadated.setUserPan(user.getUserPan());
		userUpadated.setUserPincode(user.getUserPincode());
		userUpadated.setUserVillage(user.getUserVillage());
		userUpadated.setUserState(user.getUserState());
		userUpadated.setUserTq(user.getUserTq());

		return repository.save(userUpadated);
	}

}
