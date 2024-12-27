package com.ENSATApp.EApp.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ENSATApp.EApp.LoginRequest;
import com.ENSATApp.EApp.PasswordUpdateRequest;
import com.ENSATApp.EApp.models.LoginInfo;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.repositories.LoginInfoRepository;
import com.ENSATApp.EApp.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    private LoginInfoRepository loginInfoRepository; // Injected instance

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpRequest> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.submitSignUpRequest(request));
    }

    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable String id) {
        authService.approveSignUpRequest(id);
        return ResponseEntity.ok("Sign-up request approved and credentials sent via email.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        // Fetch the user by email
        LoginInfo user = loginInfoRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the old password matches
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Old password is incorrect");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        loginInfoRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

}
