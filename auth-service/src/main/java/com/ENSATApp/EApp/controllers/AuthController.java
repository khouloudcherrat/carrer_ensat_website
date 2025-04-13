package com.ENSATApp.EApp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ENSATApp.EApp.PasswordUpdateRequest;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpRequest> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.submitSignUpRequest(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        try {
            String response = authService.updatePassword(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/admin/sign-up-requests")
    public ResponseEntity<List<SignUpRequest>> getAllSignUpRequests() {
        return ResponseEntity.ok(authService.getAllSignUpRequests());
    }

    @PostMapping("/admin/sign-up-requests/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable String id) {
        authService.approveSignUpRequest(id);
        return ResponseEntity.ok("Sign-up request approved and credentials sent via email.");
    }

    @PostMapping("/admin/sign-up-requests/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable String id) {
        authService.rejectSignUpRequest(id);
        return ResponseEntity.ok("Sign-up request rejected.");
    }

}
