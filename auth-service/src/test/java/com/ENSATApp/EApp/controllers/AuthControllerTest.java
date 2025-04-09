package com.ENSATApp.EApp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ENSATApp.EApp.PasswordUpdateRequest;
import com.ENSATApp.EApp.models.LoginInfo;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.repositories.LoginInfoRepository;
import com.ENSATApp.EApp.repositories.SignUpRequestRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import factories.LoginInfoFactory;
import factories.SignUpRequestFactory;

@SpringBootTest
public class AuthControllerTest {
    @Autowired
    AuthController authController;
    @Autowired
    SignUpRequestRepository signUpRequestRepository; 
    @Autowired
    LoginInfoRepository loginInfoRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeAll
    public static void setUp() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("ensat_carrer_app");
        // Clear all collections to ensure the test environment is clean
        for (String collectionName : database.listCollectionNames()) {
            database.getCollection(collectionName).deleteMany(new org.bson.Document());
        }
    }

    @BeforeEach
    public void cleanDatabase() {
        loginInfoRepository.deleteAll();
        signUpRequestRepository.deleteAll();
    }

    @Test
    void signUpTest(){
        SignUpRequest request = SignUpRequestFactory.create();
        SignUpRequest expected = authController.signUp(request).getBody();
        assertNotNull(expected);
        assertEquals(request, expected);
        // Check if signUpRequest exists in the database
        SignUpRequest result = signUpRequestRepository.findById(expected.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void approveRequestTest(){
        // Creating a sign-up request
        SignUpRequest request = SignUpRequestFactory.create();
        SignUpRequest saved_request = authController.signUp(request).getBody();

        // Approve the request
        authController.approveRequest(saved_request.getId());

        // Verify if the request was deleted from sign-up requests
        assertNull(signUpRequestRepository.findById(saved_request.getId()).orElse(null));

        //verify it exists in login info and that mail adresses are matching
        LoginInfo result = loginInfoRepository.findByEmail(request.getEmail()).orElse(null);
        assertNotNull(result);
        assertEquals(saved_request.getEmail(), result.getEmail() );

    }

    @Test
    void loginTest() {
        // Create a loginInfo object 
        LoginInfo loginInfo = LoginInfoFactory.create();
                
        // Create a loginRequest DTO object with similar infos of loginInfo object
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(loginInfo.getEmail());
        String rawPassword = loginInfo.getPassword();
        loginRequest.setPassword(rawPassword);

        // Encode the password and save the loginInfo in the database
        loginInfo.setPassword(passwordEncoder.encode(loginInfo.getPassword()));
        loginInfoRepository.save(loginInfo);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check HTTP 200 OK
        assertNotNull(response.getBody()); // Check body is not null

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body.get("token")); // Ensure token exists
    }

    @Test
    void updatePasswordTest(){
        // Create and save a loginInfo
        LoginInfo loginInfo = LoginInfoFactory.create();
        String rawPassword = loginInfo.getPassword();

        // Encode the password and save the loginInfo in the database
        loginInfo.setPassword(passwordEncoder.encode(rawPassword));
        loginInfoRepository.save(loginInfo);

        // Update the password
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(loginInfo.getEmail(), rawPassword, "newPassword");
        ResponseEntity<String> response = authController.updatePassword(passwordUpdateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check HTTP 200 OK
    }
}