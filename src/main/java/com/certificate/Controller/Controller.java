package com.certificate.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.certificate.Models.ANADApplication;
import com.certificate.Models.Cardentials;
import com.certificate.Models.CasteApplication;
import com.certificate.Models.Certificate;
import com.certificate.Models.IncomeApplication;
import com.certificate.Models.User;
import com.certificate.Models.UserFeedback;
import com.certificate.Services.ANADService;
import com.certificate.Services.AdminService;
import com.certificate.Services.CardentialsService;
import com.certificate.Services.CasteService;
import com.certificate.Services.CertificateService;
import com.certificate.Services.IncomeApplicationS;
import com.certificate.Services.UserFeedbackService;
import com.certificate.Services.UserService;
import com.certificate.security.JWT.JwtTokenUtil;

@RequestMapping("/main")
@RestController
@CrossOrigin
public class Controller {

//	Services Objects -------------------------------------------------

	// service class objects injects by automatically

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private UserService userService;

	@Autowired
	private CardentialsService cardentialsService;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IncomeApplicationS applicationS;

	@Autowired
	private ANADService anadService;

	@Autowired
	private CasteService casteService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserFeedbackService feedbackService;
	// for Cardentials
	// -----------------------------------------------------------------------

	// Register a new user

	@PostMapping("/register")
	public ResponseEntity<?> saveCaedentials(@RequestBody Cardentials cardentials) {

		// Checks if the user is alredy have registerd or not
		Optional<Cardentials> cardential = cardentialsService.findByUserEmailOrPhone(cardentials.getUserEmailOrPhone());

		if (cardential.isPresent()) {
			// if registered giving error
			return ResponseEntity.badRequest().body("User already exists.");
		} else {

			// saving new user credentials
			cardentials.setUserPassword(passwordEncoder.encode(cardentials.getUserPassword()));
			this.cardentialsService.saveCardentials(cardentials);

			// and generating new Jwt token
			String token = jwtUtil.generateToken(cardentials.getUserEmailOrPhone());
			// and returned it
			return ResponseEntity.ok(token);
		}
	}

	// Authenticate user and issue JWT token
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateUser(@RequestBody Cardentials cardentials) {

		// getting credentials from db
		Optional<Cardentials> cardential = cardentialsService.authenticate(cardentials.getUserEmailOrPhone(),
				cardentials.getUserPassword());

		if (cardential.isPresent()) {

			// Generating jwt token for authentication
			String token = jwtUtil.generateToken(cardentials.getUserEmailOrPhone());

			// and Return the JWT token if the user is Authenticated
			return ResponseEntity.ok(token);
		} else {
			// or else send HttpStatus.UNAUTHORIZED
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/getcardentials/{username}")
	public ResponseEntity<?> getCardentials(@PathVariable String username) {

		// getting credentials from db
		return ResponseEntity.ok(cardentialsService.findByUserEmailOrPhone(username));
	}

	@PostMapping("/authenticate-Admin")
	public ResponseEntity<?> authenticateAdmin(@RequestBody Cardentials cardentials) {
		if (!(adminService.getUsername().equalsIgnoreCase(cardentials.getUserEmailOrPhone())
				&& adminService.getPassword().equalsIgnoreCase(cardentials.getUserPassword()))) {

			return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtil.generateToken(adminService.getUsername());
		return ResponseEntity.ok(token);
	}

	@PostMapping("/changeusername")
	public ResponseEntity<?> changeUsername(@RequestBody Map<String, String> payload) {

		// Extract parameters from the payload
		String oldUsername = payload.get("oldUsername");
		String oldPassword = payload.get("oldPassword");
		String newUsername = payload.get("newUsername");

		// Validate payload
		if (oldUsername.isBlank() || oldPassword.isBlank() || newUsername.isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("All fields (oldUsername, Password And newUsername) are required.");
		}

		// Check if the new username already exists
		if (cardentialsService.findByUserEmailOrPhone(newUsername).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
					.body("The new username/email/phone already exists. Please choose a different one.");
		}

		// Attempt to change username and password
		Cardentials updatedCardentials = cardentialsService.changeUsername(oldUsername, oldPassword, newUsername);

		if (updatedCardentials != null) {
			return ResponseEntity.ok(updatedCardentials.getUserEmailOrPhone());
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("Invalid old username or password. Please try again.");
	}

	@PostMapping("/changepassword")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {

		// Extract parameters from the payload
		String oldUsername = payload.get("oldUsername");
		String oldPassword = payload.get("oldPassword");
		String newPassword = payload.get("newPassword");

		// Validate payload
		if (oldUsername.isBlank() || oldPassword.isBlank() || newPassword.isBlank()) {

			// any of if condition is true then return this
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("All fields (Username, oldPassword And newPassword) are required.");
		}

		// Attempt to change username and password
		Cardentials updatedCardentials = cardentialsService.changePassword(oldUsername, oldPassword, newPassword);

		if (updatedCardentials != null) {
			return ResponseEntity.ok("Password Successfully Updated.. ");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password. Please try again.");
	}
//	-----------------------for user model-------------------------------------------

	@PostMapping("/user/{username}")
	public ResponseEntity<?> saveUser(@RequestBody User user, @PathVariable String username) {
		// simply saving the userDetails
		this.userService.saveUser(user, username);

		// and returning massage
		return ResponseEntity.ok("User details saved successfully.");
	}

	@GetMapping("/user/{userEmailOrPhone}")
	public User getUser(@PathVariable String userEmailOrPhone) {
		// getting user details for applications and etc

		return userService.getUser(userEmailOrPhone);
	}

	// Getting All Certifiactes By By Using Mappings
	@GetMapping("/certificate/{id}")
	public ResponseEntity<List<Certificate>> getUserCertificates(@PathVariable String id) {

		return ResponseEntity.ok(userService.getCertificates(id));
	}

	@PutMapping("/update/{id}")
	public User updateUser(@PathVariable String id, @RequestBody User user) {
		// updating user details via user-id
		return userService.updateUser(id, user);
	}

	@PutMapping("/updateEmail/{id}")
	public ResponseEntity<?> updateUserEmail(@PathVariable String id, @RequestParam String newEmail) {
		return ResponseEntity.ok(newEmail);
	}

//    =======-----------------------------------------------------------------
//    for Income

	@PostMapping("/income")
	public ResponseEntity<?> saveDetails(@RequestBody IncomeApplication application) {

		// generating a unique income application id and setting before saving in
		// database
		application.setApplicationID(applicationS.generateApplicationId());

		// saving in database after generating unique id
		this.applicationS.save(application);

		// Returning a that generated unique id
		return ResponseEntity.ok(application.getApplicationID());
	}

//	@GetMapping("/get/{Id}")
//	public Optional<IncomeApplication> get(@PathVariable("Id") String id) {
//		// geting user all details by application id
//		return this.applicationS.getDetails(id);
//	}



	@GetMapping("/INCM-Applications")
	public List<IncomeApplication> getAll() {
		return this.applicationS.getAll();
	}

//    ===--------------------------------------------------------------------------

	// for caste

	@PostMapping("/caste")
	public ResponseEntity<?> save(@RequestBody CasteApplication application) {
		application.setApplicationID(casteService.generateApplicationId());

		this.casteService.save(application);

		return ResponseEntity.ok(application.getApplicationID()); // same as income
	}

	@GetMapping("/caste-Applications")
	public List<CasteApplication> getAllCaste() {
		return this.casteService.getAll();
	}



	// ==---------------------------------------------------------------------------------------

	// for ANAD

	@PostMapping("/ANAD")
	public ResponseEntity<?> save(@RequestBody ANADApplication application) {
		application.setApplicationID(anadService.generateApplicationId());

		this.anadService.save(application);

		return ResponseEntity.ok(application.getApplicationID()); // same as income
	}

	@GetMapping("/ANAD-Applications")
	public List<ANADApplication> getAllANAD() {
		return this.anadService.getAll();
	}


	// ==================-------------------------------------------------------------------------

	// for files
	public Controller() throws IOException {
		super();
	} // Default constructor for excepting handling

	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping("/upload-multiple-files")
	public ResponseEntity<List<String>> uploadFile(@RequestParam MultipartFile[] files) throws IOException {

		List<String> l = new LinkedList<>();

		for (MultipartFile multipartFile : files) {

			// saves files dynamically
			Files.copy(multipartFile.getInputStream(),
					Paths.get(uploadDir + File.separator + multipartFile.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);

			// appends uri
			l.add(ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/")
					.path(multipartFile.getOriginalFilename()).toUriString());

		}
		// returns a list of Uri
		return ResponseEntity.ok(l);
	}

	// for
	// Certi=-----------------------------------------------------------------------------

	// if user wants User wants To get list from Certificate Directly using
	// Registerd
//	@GetMapping("/certificate/{id}")
//	public ResponseEntity<?> getAllCertificates(@PathVariable String id) {
//		
//		List<Certificate> a=certificateService.getAllCertificates(id);
//		
//		return ResponseEntity.ok(a);
//	}

	@GetMapping("/certificate")
	public List<Certificate> getAllCertificates() {
		return certificateService.getAllCertificates();
	}

	@PostMapping("/certificate/allocate/{username}")
	public ResponseEntity<?> allocateResources(@RequestBody Certificate certificate, @PathVariable String username) {
		this.certificateService.allocateResources(certificate, username);
		return ResponseEntity.ok("ok");
	}

	@GetMapping("/certificate/track/{id}")
	public ResponseEntity<Certificate> getCertificate(@PathVariable String id) {
		return ResponseEntity.ok(certificateService.getCertificate(id));
	}

	@PutMapping("/status/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody Certificate certificate) {
		// updating user details via user-id

		this.certificateService.updateUser(id, certificate);
		return ResponseEntity.ok("Updated The Certi Status");
	}
	
	


	@GetMapping(value = "/genrate/{applicationId}", produces = "application/pdf")
	public ResponseEntity<byte[]> generateCertificate(@PathVariable String applicationId, @RequestParam String name) {

		if (applicationId.contains("CASTE")) {
			byte[] pdfBytes = certificateService.generateCCertificate(applicationId,name);
			return ResponseEntity.ok().header("Content-Disposition", "inline; filename=certificate.pdf").body(pdfBytes);
		}

		else if (applicationId.contains("INCOME")) {
			byte[] pdfBytes = certificateService.generateICertificate(applicationId,name);
			return ResponseEntity.ok().header("Content-Disposition", "inline; filename=certificate.pdf").body(pdfBytes);

		}

		else if (applicationId.contains("ANAD")) {
			byte[] pdfBytes = certificateService.generateACertificate(applicationId,name);
			return ResponseEntity.ok().header("Content-Disposition", "inline; filename=certificate.pdf").body(pdfBytes);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}
	
	
	//------------------------------------feedback-----------------------------------------
	
	@PostMapping("/feedback/{id}")
	public ResponseEntity<?> saveFeedback(@RequestBody UserFeedback feedback,@PathVariable String id){
		feedbackService.save(feedback , id);
		return ResponseEntity.ok(" FeedBack Saved");
	}
	

}
