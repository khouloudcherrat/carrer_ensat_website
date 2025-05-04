package com.ENSATApp.EApp.service;

import java.security.SecureRandom;
import java.util.List; // Import List
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ENSATApp.EApp.config.security.JwtTokenProvider;
import com.ENSATApp.EApp.dto.LoginRequest;
import com.ENSATApp.EApp.dto.PasswordUpdateRequest;
import com.ENSATApp.EApp.model.LoginInfo;
import com.ENSATApp.EApp.model.Partner;
import com.ENSATApp.EApp.model.SignUpRequest;
import com.ENSATApp.EApp.repository.LoginInfoRepository;
import com.ENSATApp.EApp.repository.PartnerRepository;
import com.ENSATApp.EApp.repository.SignUpRequestRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthService {
    private final SignUpRequestRepository signUpRequestRepository;
    private final PartnerRepository partnerRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    public AuthService(SignUpRequestRepository signUpRequestRepository,
                       LoginInfoRepository loginInfoRepository, BCryptPasswordEncoder passwordEncoder,
                       JavaMailSender mailSender, JwtTokenProvider jwtTokenProvider, PartnerRepository partnerRepository) {
        this.signUpRequestRepository = signUpRequestRepository;
        this.partnerRepository = partnerRepository;
        this.loginInfoRepository = loginInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender; // Initialize mail sender
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Submit sign-up request
    public SignUpRequest submitSignUpRequest(SignUpRequest request) {
        return signUpRequestRepository.save(request);
    }

    // Login operation
    public String login(LoginRequest loginRequest) { // Change parameter to LoginRequest
        // Fetch user by email
        LoginInfo loginInfo = loginInfoRepository.findByEmail(loginRequest.getEmail()) // Use loginRequest
                .orElseThrow(() -> new RuntimeException("Invalid email"));
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), loginInfo.getPassword())) { // Use loginRequest
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(loginInfo.getEmail(), loginInfo.getRole());
        return token;
    }

    // Approve sign-up request
    public void approveSignUpRequest(String requestId) {
        SignUpRequest request = signUpRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Generate random password
        String rawPassword = generateRandomPassword();

        // Save login info
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(request.getEmail());
        loginInfo.setPassword(passwordEncoder.encode(rawPassword)); // Store hashed password
        loginInfo.setRole(request.getRole());
        loginInfoRepository.save(loginInfo);

        // Delete sign-up request after approval
        signUpRequestRepository.deleteById(requestId);

        // Send email with login credentials
        sendEmail(request.getEmail(), "Your Account is Approved", 
                  "<p>Your account has been approved.</p>" +
                  "<p><strong>Username:</strong> " + request.getEmail() + "<br>" +
                  "<strong>Password:</strong> " + rawPassword + "</p>" +
                  "<p>To change your password, please click the following link:<br>" +
                  "<a href='" + frontendBaseUrl + "/update-password'>Change your password</a></p>" +
                  "<p><em>Please log in and change your password immediately.</em></p>");
    }

    // Reject the sign-up request
    public void rejectSignUpRequest(String requestId) {
        SignUpRequest request = signUpRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Delete the rejected request
        signUpRequestRepository.deleteById(requestId);

        // Send rejection email
        sendEmail(request.getEmail(), "Your Sign-Up Request Was Rejected",
                  "We regret to inform you that your sign-up request has been rejected.\n" +
                  "Reason: Your identity could not be verified by the admin.\n\n" +
                  "If you believe this is a mistake, please contact support.");
    }

    // Get all sign-up requests
    public List<SignUpRequest> getAllSignUpRequests() {
        return signUpRequestRepository.findAll();
    }

    // Generate a random password
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    // Send email
    private void sendEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Update the password
    public String updatePassword(PasswordUpdateRequest request) {
        // Fetch the user by email
        LoginInfo user = loginInfoRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Check if the old password matches
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
    
        // Update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        loginInfoRepository.save(user);
    
        return "Password updated successfully";
    }

    // Retrieve all partners
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public List<Partner> getUnregistredPartners() {
        List<Partner> allPartners = partnerRepository.findAll();
        List<String> registeredEmails = loginInfoRepository.findAll().stream()
            .map(LoginInfo::getEmail)
            .toList();
    
        return allPartners.stream()
            .filter(p -> !registeredEmails.contains(p.getEmail()))
            .toList();
    }

    // Send credentials to a partner
    public void sendCredentialsToPartner(String partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        // Generate random password
        String rawPassword = generateRandomPassword();

        // Save login info
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(partner.getEmail());
        loginInfo.setPassword(passwordEncoder.encode(rawPassword)); // Store hashed password
        loginInfo.setRole("Partner");
        loginInfoRepository.save(loginInfo);

        // Send email with login credentials
        sendEmail(partner.getEmail(), "Access to ENSAT Career Platform", 
            "<p>Hello,</p>" +
            "<p>You are now registered as a representative of <strong>" + partner.getOrganization() + "</strong>.</p>" +
            "<p>Here are your login credentials:</p>" +
            "<ul>" +
            "<li><strong>Username:</strong> " + partner.getEmail() + "</li>" +
            "<li><strong>Password:</strong> " + rawPassword + "</li>" +
            "</ul>" +
            "<p>You can access the platform here: <a href='" + frontendBaseUrl + "'>" + frontendBaseUrl + "</a></p>" +
            "<p>Please change your password immediately after logging in by visiting the following page:<br>" +
            "<a href='" + frontendBaseUrl + "/update-password'>Change your password</a></p>" +
            "<p>Best regards,<br>ENSAT Career Platform Team</p>");
    }



    
    
}
