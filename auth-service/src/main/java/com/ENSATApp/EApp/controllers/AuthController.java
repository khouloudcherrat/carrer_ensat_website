package com.ENSATApp.EApp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ENSATApp.EApp.PasswordUpdateRequest;
import com.ENSATApp.EApp.models.Partner;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.services.AuthService;
import com.ENSATApp.EApp.services.SseService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final SseService sseService;

    public AuthController(AuthService authService, SseService sseService) {
        this.authService = authService;
        this.sseService = sseService;
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/sign-up-requests")
    public ResponseEntity<List<SignUpRequest>> getAllSignUpRequests() {
        return ResponseEntity.ok(authService.getAllSignUpRequests());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/sign-up-requests/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable String id) {
        authService.approveSignUpRequest(id);
        sseService.notifyClients();
        return ResponseEntity.ok("Sign-up request approved and credentials sent via email.");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/sign-up-requests/{id}/reject")
    public ResponseEntity<Map<String,String>> rejectRequest(@PathVariable String id) {
        authService.rejectSignUpRequest(id);
        sseService.notifyClients();
        return ResponseEntity.ok(Map.of("message", "Sign-up request rejected."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all-partners")
    public ResponseEntity<List<Partner>> getAllPartners() {
        return ResponseEntity.ok(authService.getAllPartners());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/unregistred-partners")
    public ResponseEntity<List<Partner>> getUnregistredPartners() {
        return ResponseEntity.ok(authService.getUnregistredPartners());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/partners/{id}/send-credentials")
    public ResponseEntity<Map<String,String>> sendCredentialsToPartner(@PathVariable String id) {

        
        authService.sendCredentialsToPartner(id);
        sseService.notifyClients();
        return ResponseEntity.ok(Map.of("message", "Credentials sent successfully."));
    }

}
