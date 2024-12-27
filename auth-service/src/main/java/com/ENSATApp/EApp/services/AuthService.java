package com.ENSATApp.EApp.services;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ENSATApp.EApp.models.LoginInfo;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.repositories.LoginInfoRepository;
import com.ENSATApp.EApp.repositories.SignUpRequestRepository;
import com.ENSATApp.EApp.JwtTokenProvider;

@Service
public class AuthService {
    private final SignUpRequestRepository signUpRequestRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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
                  "Your account has been approved.\n\nUsername: " + request.getEmail() +
                  "\nPassword: " + rawPassword + 
                  "\n\nPlease log in and change your password immediately.");
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
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    // Login operation
    public String login(String email, String rawPassword) {
        // Fetch user by email
        LoginInfo loginInfo = loginInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(rawPassword, loginInfo.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(loginInfo.getEmail(), loginInfo.getRole());

        return token;
    }
}
