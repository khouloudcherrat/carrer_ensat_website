package com.ENSATApp.EApp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.ENSATApp.EApp.models.LoginInfo;
import com.ENSATApp.EApp.models.SignUpRequest;
import com.ENSATApp.EApp.repositories.LoginInfoRepository;
import com.ENSATApp.EApp.repositories.SignUpRequestRepository;
import com.ENSATApp.EApp.services.AuthService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import factories.SignUpRequestFactory;

@SpringBootTest
public class AuthControllerTest {
    @Autowired
    AuthController authController;
    @Autowired
    SignUpRequestRepository signUpRequestRepository; 
    @Autowired
    LoginInfoRepository loginInfoRepository;

    @BeforeAll
    public static void setUp() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("ensat_carrer_app");
        // Clear all collections to ensure the test environment is clean
        for (String collectionName : database.listCollectionNames()) {
            database.getCollection(collectionName).deleteMany(new org.bson.Document());
        }
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
}
