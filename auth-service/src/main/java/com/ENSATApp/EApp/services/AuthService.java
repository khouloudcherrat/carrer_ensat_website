package com.ENSATApp.EApp.services;

import java.security.SecureRandom;
import java.util.List; // Import List
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ENSATApp.EApp.JwtTokenProvider;
import com.ENSATApp.EApp.PasswordUpdateRequest; // Import PasswordUpdateRequest
import com.ENSATApp.EApp.controllers.LoginRequest; // Import LoginRequest
import com.ENSATApp.EApp.models.LoginInfo;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.repositories.LoginInfoRepository;
import com.ENSATApp.EApp.repositories.SignUpRequestRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthService {
    private final SignUpRequestRepository signUpRequestRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    public AuthService(SignUpRequestRepository signUpRequestRepository,
                       LoginInfoRepository loginInfoRepository, BCryptPasswordEncoder passwordEncoder,
                       JavaMailSender mailSender, JwtTokenProvider jwtTokenProvider) {
        this.signUpRequestRepository = signUpRequestRepository;
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
}
