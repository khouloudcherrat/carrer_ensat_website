package factories;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.github.javafaker.Faker;


import com.ENSATApp.EApp.models.SignUpRequest;
import java.util.HashMap;

public class SignUpRequestFactory {

    private static final Faker faker = new Faker();

    // List of available branches
    private static final String[] BRANCH_OPTIONS = {
        "Systems and Networks Engineering (GSR)",
        "Electronic and Automatic Systems Engineering (GSEA)",
        "Computer Engineering (GINF)",
        "Industrial Engineering (GIND)",
        "Energy Engineering and Industrial Environment (G2EI)",
        "Cybersecurity and Cyberintelligence (CSI)"
    };

    // Method to generate a random SignUpRequest object
    public static SignUpRequest create() {
        SignUpRequest signUpRequest = new SignUpRequest();

        signUpRequest.setFirstName(faker.name().firstName());
        signUpRequest.setLastName(faker.name().lastName());
        signUpRequest.setEmail(faker.internet().emailAddress());
        signUpRequest.setCinCard(faker.idNumber().valid());
        signUpRequest.setBranch(faker.options().option(BRANCH_OPTIONS)); // Randomly pick from BRANCH_OPTIONS
        signUpRequest.setRole(faker.options().option("student", "alumni")); // Randomly pick "student" or "alumni"
        
        // Create dynamic formDetails
        Map<String, Object> formDetails = generateFormDetails(signUpRequest.getRole());
        signUpRequest.setFormDetails(formDetails);

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        signUpRequest.setCreatedAt(LocalDateTime.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        return signUpRequest;
    }

    // Method to generate dynamic formDetails based on the role
    private static Map<String, Object> generateFormDetails(String role) {
        Map<String, Object> formDetails = new HashMap<>();
    
        // If the role is "student", generate student-specific details
        if ("student".equals(role)) {
            formDetails.put("yearOfAdmission", faker.number().numberBetween(2018, 2023));
            formDetails.put("educationalLevel", faker.options().option("CP1", "CP2", "CI1", "CI2", "CI3"));
        }
        // If the role is "alumni", generate alumni-specific details
        else if ("alumni".equals(role)) {
            formDetails.put("graduationYear", faker.number().numberBetween(2015, 2023));
            formDetails.put("professionalStatus", faker.options().option("seeking job", "employed", "entrepreneur"));
    
            // If professionalStatus is "employed", add a company field
            String professionalStatus = (String) formDetails.get("professionalStatus");
            if ("employed".equals(professionalStatus)) {
                formDetails.put("company", faker.company().name());  // Random company name
            }
        }
    
        return formDetails;
    }
    
}