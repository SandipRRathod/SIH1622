package com.certificate.Services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certificate.Models.ANADApplication;
import com.certificate.Models.Cardentials;
import com.certificate.Models.CasteApplication;
import com.certificate.Models.Certificate;
import com.certificate.Models.IncomeApplication;
import com.certificate.Models.User;
import com.certificate.Repos.ANADApplicationRepo;
import com.certificate.Repos.CasteRepo;
import com.certificate.Repos.CertificateRepository;
import com.certificate.Repos.IncomeApplicationRepo;

@Service
public class CertificateService {

	@Autowired
	private CertificateRepository certificateRepository;

	@Autowired
	private IncomeApplicationRepo IapplicationRepo;

	@Autowired
	private CasteRepo casteRepo;

	@Autowired
	private ANADApplicationRepo anadApplicationRepo;

	@Autowired
	private CardentialsService cardentialsService;

	public List<Certificate> getAllCertificates(String Id) {
		return certificateRepository.findByRegisterdId(Id);
	}

	public Certificate allocateResources(Certificate certificate, String username) {
		// Logic for resource allocation

		Cardentials cardentials = cardentialsService.findByUserEmailOrPhone(username)
				.orElseThrow(() -> new RuntimeException("User Not Found With " + username));

		User user = cardentials.getUser();

		certificate.setUser(user);

		return certificateRepository.save(certificate);
	}

	public Certificate getCertificate(String id) {
		return certificateRepository.findById(id).orElse(null);
	}

	public Certificate updateUser(String id, Certificate certificate) {
		Certificate userUpadated = certificateRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("not found with id:" + id));

		userUpadated.setCertiStatus(certificate.getCertiStatus());
		userUpadated.setRejectionStatus(certificate.getRejectionStatus());

		String Astatus = certificate.getCertiStatus();
		String Rstatus = certificate.getRejectionStatus();

		try {
			IncomeApplication application = IapplicationRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("IncomeApplication not found with id:" + id));
			application.setCertiStatus(Astatus);
			application.setRejectionStatus(Rstatus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			ANADApplication application2 = anadApplicationRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("ANADApplication not found with id:" + id));
			application2.setCertiStatus(Astatus);
			application2.setRejectionStatus(Rstatus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			CasteApplication application3 = casteRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("CasteApplication not found with id:" + id));
			application3.setCertiStatus(Astatus);
			application3.setRejectionStatus(Rstatus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return this.certificateRepository.save(userUpadated);
	}

//	====------------------------------------------------------------------------------------------
	// Age nationnality and domecile
	
	

		public byte[] generateACertificate(String id,String applicantName) {
			
			
			Certificate certificate=certificateRepository.findById(id).orElse(null);
			
			User user= certificate.getUser();
			
			
			
			
	    try {
	        PDDocument document = new PDDocument();
	        PDPage page = new PDPage();
	        document.addPage(page);

	        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	            // Add border to the page (rectangle)
	            addPageBorder(contentStream, page);

	            // Header (Centered and Bold)
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	            float yPosition = page.getMediaBox().getHeight() - 100; // Adjust y-position to stay inside the border
	            contentStream.newLineAtOffset(240, yPosition); // Centered
	            contentStream.showText("Tahsildar Office");
	            contentStream.endText();
	            

	            // Title (Centered and Bold)
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
	            yPosition -= 50;
	            contentStream.newLineAtOffset(95, yPosition);
	            contentStream.showText("Age, Nationality, and Domicile Certificate");
	            contentStream.endText();

	            contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
				yPosition -= 27; // Start y-position for the applicant details
				// Adjust y-position to stay inside the border
				contentStream.newLineAtOffset(335, yPosition); 
				contentStream.showText("Application Id: "+id);
				contentStream.endText();
				
				
				  // Body Paragraph
	            String bodyText = "Based on the evidence provided in the submitted documents, it is certified that "
                + applicantName
                + " residing at Village "+user.getUserVillage()+", Post "+user.getUserVillage()+", Taluka "+user.getUserTq()+", "
                + "District  "+user.getUserDist()+" is born on "+user.getUserDob()+" in India and is a citizen of India. Based on the evidence provided "
                + "in the submitted documents, he/she is a resident of the state of Maharashtra.";
				
	          
	            float marginLeft = 100; // Left margin
	            float marginRight = page.getMediaBox().getWidth() - 100; // Right margin
	            float lineHeight = 15; // Line height
	            float fontSize = 13; // Font size

	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, fontSize);
	            contentStream.newLineAtOffset(marginLeft, yPosition -= 35);

	            String[] words = bodyText.split(" ");
	            StringBuilder line = new StringBuilder();
	            for (String word : words) {
	                // Check if the current line width exceeds the margin
	                if (PDType1Font.HELVETICA.getStringWidth(line + word + " ") / 1000 * fontSize > (marginRight - marginLeft)) {
	                    contentStream.showText(line.toString().trim());
	                    contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
	                    line = new StringBuilder();
	                }
	                line.append(word).append(" ");
	            }
	            // Print the last line
	            if (!line.isEmpty()) {
	                contentStream.showText(line.toString().trim());
	            }
	            contentStream.endText();
	            
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 11);
	            yPosition -= 115;
	            contentStream.newLineAtOffset(100, yPosition);
	            contentStream.showText("Submited Documents / Certificate..");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("1) Identity Proof");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("2) Address Proof");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("3) Birth Certifiacte Or TC Proof");
	            contentStream.endText();
	            
	            // Footer (Centered)
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 13);
	            yPosition -= 200;
	            contentStream.newLineAtOffset(370, yPosition);
	            contentStream.showText("-Authorized By-");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            yPosition -= 2;
	            contentStream.newLineAtOffset(115, yPosition);
	            contentStream.showText("Date : "+LocalDate.now().toString());
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 13);
	            yPosition -= 18;
	            contentStream.newLineAtOffset(340, yPosition);
	            contentStream.showText("Tehsildar / Deputy Tehsildar");
	            contentStream.endText();

	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            yPosition -= 17;
	            contentStream.newLineAtOffset(343, yPosition);
	            contentStream.showText("Tehsil Office, District Office");
	            contentStream.endText();
	            
	            
	            contentStream.close();

	        }

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        document.save(baos);
	        document.close();
	        return baos.toByteArray();

	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}



//====------------------------------------------------------------------------------------------
	// Income


	public byte[] generateICertificate(String id,String applicantName) {
		
		Certificate certificate=certificateRepository.findById(id).orElse(null);
		
		User user= certificate.getUser();
		
	  IncomeApplication  application=IapplicationRepo.findById(id).orElse(null);
		
		try {
			// Create a new document
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			// Start content stream to write text to the PDF
			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// Add border to the page (rectangle)
			addPageBorder(contentStream, page);
			
			
			// Title (Header) - Centered and Bold
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
			// Ensure the yPosition starts inside the border area
			float yPosition = page.getMediaBox().getHeight() - 100; // Adjust y-position to stay inside the border
			contentStream.newLineAtOffset(240, yPosition); // Centered
			contentStream.showText("Tahsildar Office");
			contentStream.endText();
			
		
			
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
			yPosition -= 50; // Start y-position for the applicant details
			// Adjust y-position to stay inside the border
			contentStream.newLineAtOffset(155, yPosition); // Centered
			contentStream.showText("INCOME CERTIFICATE FOR");
			contentStream.endText();

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
			// Ensure the yPosition starts inside the border area
			yPosition -= 25; // Adjust y-position to stay inside the border
			contentStream.newLineAtOffset(245, yPosition); // Centered
			contentStream.showText(application.getIncomeFor().toUpperCase());
			contentStream.endText();
			
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
			yPosition -= 27; // Start y-position for the applicant details
			// Adjust y-position to stay inside the border
			contentStream.newLineAtOffset(335, yPosition); 
			contentStream.showText("Application Id: "+id);
			contentStream.endText();
			
			
            // Body Paragraph
            String bodyText = "This is to certify that, " + applicantName + " is a resident of Village "+user.getUserVillage()
                    + " residing at Village "+user.getUserVillage()+", Post "+user.getUserVillage()+", Taluka "+user.getUserTq()+", "
                    + "District "+user.getUserDist()+" is born in India and is a citizen of India. The Reasion Of this Certificate Is "+application.getCertiReasion()+" and Based on the evidence provided "
                    + "in the submitted documents.";

            float marginLeft = 100; // Left margin
            float marginRight = page.getMediaBox().getWidth() - 100; // Right margin
            float lineHeight = 15; // Line height
            float fontSize = 13; // Font size

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, fontSize);
            contentStream.newLineAtOffset(marginLeft, yPosition -= 35);

            String[] words = bodyText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                // Check if the current line width exceeds the margin
                if (PDType1Font.HELVETICA.getStringWidth(line + word + " ") / 1000 * fontSize > (marginRight - marginLeft)) {
                    contentStream.showText(line.toString().trim());
                    contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
                    line = new StringBuilder();
                }
                line.append(word).append(" ");
            }
            // Print the last line
            if (!line.isEmpty()) {
                contentStream.showText(line.toString().trim());
            }
            contentStream.endText();

            
            if(application.getIncomeFor().equalsIgnoreCase("One Year")) {
            	contentStream.beginText();
    			contentStream.setFont(PDType1Font.HELVETICA, 13);
    			yPosition -= 90; // Adjust y-position for validity statement
    			contentStream.newLineAtOffset(100, yPosition);
    			contentStream.showText("the annual income of Year: "+(LocalDate.now().getYear()-1)+"-"+LocalDate.now().getYear()+" | Annual Income: RS "+application.getAnnualIncome());
    			contentStream.endText();
            }
            else {
            	contentStream.beginText();
    			contentStream.setFont(PDType1Font.HELVETICA, 13);
    			yPosition -= 90; // Adjust y-position for validity statement
    			contentStream.newLineAtOffset(100, yPosition);
    			contentStream.showText("the annual income of Last Three Years : ");
    			contentStream.endText();
    			
    			
    			yPosition-=30;
    			// Start drawing the table
    			float margin = 100; // Left margin
    			float tableWidth = 400;
    			float cellHeight = 20;
    			float tableTopY = yPosition; // Start from the current yPosition

    			// Draw table borders
    			contentStream.setStrokingColor(Color.BLACK); // Set border color
    			contentStream.setLineWidth(1.0f);

    			// Header Row
    			contentStream.addRect(margin, tableTopY, tableWidth, cellHeight);
    			contentStream.stroke();

    			// Draw header columns
    			contentStream.addRect(margin, tableTopY, 150, cellHeight); // Year column
    			contentStream.addRect(margin + 150, tableTopY, 250, cellHeight); // Income column
    			contentStream.stroke();

    			// Add header text
    			contentStream.beginText();
    			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
    			contentStream.newLineAtOffset(margin + 10, tableTopY + 5);
    			contentStream.showText("Year");
    			contentStream.endText();

    			contentStream.beginText();
    			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
    			contentStream.newLineAtOffset(margin + 160, tableTopY + 5);
    			contentStream.showText("Annual Income (RS)");
    			contentStream.endText();

    			// Add data rows
    			String[] years = {
    			    (LocalDate.now().getYear() - 3) + "-" + (LocalDate.now().getYear() - 2),
    			    (LocalDate.now().getYear() - 2) + "-" + (LocalDate.now().getYear() - 1),
    			    (LocalDate.now().getYear() - 1) + "-" + LocalDate.now().getYear()
    			};

    			String[] incomes = {
    			    application.getAnnualThirdIncome(),
    			    application.getAnnualSecondIncome(),
    			    application.getAnnualIncome()
    			};

    			for (int i = 0; i < years.length; i++) {
    			    tableTopY -= cellHeight; // Move to the next row
    			    
    			    // Draw row border
    			    contentStream.addRect(margin, tableTopY, tableWidth, cellHeight);
    			    contentStream.stroke();

    			    // Add year data
    			    contentStream.beginText();
    			    contentStream.setFont(PDType1Font.HELVETICA, 12);
    			    contentStream.newLineAtOffset(margin + 10, tableTopY + 5);
    			    contentStream.showText(years[i]);
    			    contentStream.endText();

    			    // Add income data
    			    contentStream.beginText();
    			    contentStream.setFont(PDType1Font.HELVETICA, 12);
    			    contentStream.newLineAtOffset(margin + 160, tableTopY + 5);
    			    contentStream.showText("RS " + incomes[i]);
    			    contentStream.endText();
    			}

            }
            
			

			// Validity (Separate Paragraph)
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 13);
			yPosition -=85; // Adjust y-position for validity statement
			contentStream.newLineAtOffset(100, yPosition);
			contentStream.showText("This certificate is valid until 31st March "+(LocalDate.now().getYear()+1)+".");
			contentStream.endText();

			contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            yPosition -= 40;
            contentStream.newLineAtOffset(100, yPosition);
            contentStream.showText("Submited Documents / Certificate..");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            yPosition -= 15;
            contentStream.newLineAtOffset(110, yPosition);
            contentStream.showText("1) Identity Proof");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            yPosition -= 15;
            contentStream.newLineAtOffset(110, yPosition);
            contentStream.showText("2) Address Proof");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            yPosition -= 15;
            contentStream.newLineAtOffset(110, yPosition);
            contentStream.showText("3) Proof Of Income");
            contentStream.endText();
            
            // Footer (Centered)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 13);
            yPosition -= 150;
            contentStream.newLineAtOffset(370, yPosition);
            contentStream.showText("-Authorized By-");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            yPosition -= 2;
            contentStream.newLineAtOffset(115, yPosition);
            contentStream.showText("Date : "+LocalDate.now().toString());
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 13);
            yPosition -= 18;
            contentStream.newLineAtOffset(340, yPosition);
            contentStream.showText("Tehsildar / Deputy Tehsildar");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            yPosition -= 17;
            contentStream.newLineAtOffset(343, yPosition);
            contentStream.showText("Tehsil Office, District Office");
            contentStream.endText();


			// Close the content stream before saving the document
			contentStream.close();

			// Save the document to byte array output stream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			document.save(baos);
			document.close();

			return baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}



//			====------------------------------------------------------------------------------------------
	// Caste

	public byte[] generateCCertificate(String id,String applicantName) {
		
		
        Certificate certificate=certificateRepository.findById(id).orElse(null);
		
		User user= certificate.getUser();
		
		CasteApplication application= casteRepo.findById(id).orElse(null);

		try {

			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				
				// Add border to the page (rectangle)
				addPageBorder(contentStream, page);
	
				
				// Title (Header) - Centered and Bold
				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
				// Ensure the yPosition starts inside the border area
				float yPosition = page.getMediaBox().getHeight() - 100; // Adjust y-position to stay inside the border
				contentStream.newLineAtOffset(260, yPosition); // Centered
				contentStream.showText("Form - 8");
				contentStream.endText();
				
			

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
				yPosition -= 60; // Start y-position for the applicant details
				// Adjust y-position to stay inside the border
				contentStream.newLineAtOffset(220, yPosition);
				contentStream.showText("Caste Certificate");
				contentStream.endText();
				
				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
				yPosition -= 27; // Start y-position for the applicant details
				// Adjust y-position to stay inside the border
				contentStream.newLineAtOffset(335, yPosition); 
				contentStream.showText("Application Id: "+id);
				contentStream.endText();
				
				 // Body Paragraph
	            String bodyText = "This is to certify that, " + applicantName + " Son Of "+user.getFatherName()+" is a resident of Village "+user.getUserVillage()
	                    + " residing at Village "+user.getUserVillage()+", Post "+user.getUserVillage()+", Taluka "+user.getUserTq()+", "
	                    + "District "+user.getUserDist()+" is Belongs To "+user.getUserCast()+" Caste Which Is Recognised As "+application.getCasteType()+".";

	            
	            
	            float marginLeft = 100; // Left margin
	            float marginRight = page.getMediaBox().getWidth() - 100; // Right margin
	            float lineHeight = 15; // Line height
	            float fontSize = 13; // Font size

	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, fontSize);
	            contentStream.newLineAtOffset(marginLeft, yPosition -= 35);

	            String[] words = bodyText.split(" ");
	            StringBuilder line = new StringBuilder();
	            for (String word : words) {
	                // Check if the current line width exceeds the margin
	                if (PDType1Font.HELVETICA.getStringWidth(line + word + " ") / 1000 * fontSize > (marginRight - marginLeft)) {
	                    contentStream.showText(line.toString().trim());
	                    contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
	                    line = new StringBuilder();
	                }
	                line.append(word).append(" ");
	            }
	            // Print the last line
	            if (!line.isEmpty()) {
	                contentStream.showText(line.toString().trim());
	            }
	            contentStream.endText();


	            // Body Paragraph2
	            String bodyText2 = 
	            		applicantName + " And / Or His Family residing in Village "+user.getUserVillage()+", Taluka "+user.getUserTq()+", "
	                    + "District "+user.getUserDist()+" Of The State Of Maharashtra";

	           

	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, fontSize);
	            contentStream.newLineAtOffset(marginLeft, yPosition -= 70);

	            String[] words2 = bodyText2.split(" ");
	            StringBuilder line2 = new StringBuilder();
	            for (String word : words2) {
	                // Check if the current line width exceeds the margin
	                if (PDType1Font.HELVETICA.getStringWidth(line2 + word + " ") / 1000 * fontSize > (marginRight - marginLeft)) {
	                    contentStream.showText(line2.toString().trim());
	                    contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
	                    line2 = new StringBuilder();
	                }
	                line2.append(word).append(" ");
	            }
	            // Print the last line
	            if (!line2.isEmpty()) {
	                contentStream.showText(line2.toString().trim());
	            }
	            contentStream.endText();

				
				contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 11);
	            yPosition -= 100;
	            contentStream.newLineAtOffset(100, yPosition);
	            contentStream.showText("Submited Documents / Certificate..");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("1) Identity Proof");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("2) Address Proof");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("3) Caste Proof");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 10);
	            yPosition -= 15;
	            contentStream.newLineAtOffset(110, yPosition);
	            contentStream.showText("4) Birth Certificate Or Tc Proof");
	            contentStream.endText();
	            
	            // Footer (Centered)
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 13);
	            yPosition -= 170;
	            contentStream.newLineAtOffset(370, yPosition);
	            contentStream.showText("-Authorized By-");
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            yPosition -= 2;
	            contentStream.newLineAtOffset(115, yPosition);
	            contentStream.showText("Date : "+LocalDate.now().toString());
	            contentStream.endText();
	            
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 13);
	            yPosition -= 18;
	            contentStream.newLineAtOffset(310, yPosition);
	            contentStream.showText("Deputy Collector / Sub Division Officer");
	            contentStream.endText();

	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA, 12);
	            yPosition -= 17;
	            contentStream.newLineAtOffset(343, yPosition);
	            contentStream.showText("Collector Office, District Office");
	            contentStream.endText();



				// Close the content stream before saving the document
				contentStream.close();
			
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			document.save(baos);
			document.close();
			return baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace(); // Log the exception
			return null; // Or handle the error as needed
		}
	}

	
	
	// Method to add a border to the page
		private void addPageBorder(PDPageContentStream contentStream, PDPage page) throws IOException {
			// Define the border's position and size
			float margin = 50f; // Margin from the page edges
			float width = page.getMediaBox().getWidth() - 2 * margin;
			float height = page.getMediaBox().getHeight() - 2 * margin;

			// Set the stroke color for the border (black)
			contentStream.setLineWidth(1f);
			contentStream.setStrokingColor(0, 0, 0); // RGB (black)

			// Draw the border
			contentStream.moveTo(margin, margin);
			contentStream.lineTo(margin + width, margin);
			contentStream.lineTo(margin + width, margin + height);
			contentStream.lineTo(margin, margin + height);
			contentStream.closePath();
			contentStream.stroke();
		}
	
	public List<Certificate> getAllCertificates() {
		// TODO Auto-generated method stub
		return certificateRepository.findAll();
	}

}
