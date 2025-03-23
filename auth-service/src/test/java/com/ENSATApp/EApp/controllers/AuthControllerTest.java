package com.ENSATApp.EApp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.ENSATApp.EApp.models.SignUpRequest;
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

    @BeforeAll
    public static void setUp() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("ensat_carrer_app");
        // Clear collections to ensure the test environment is clean
        database.getCollection("sign_up_requests").deleteMany(new org.bson.Document());
    }

    @Test
    void signUpTest(){
        SignUpRequest request = SignUpRequestFactory.create();
        SignUpRequest result = authController.signUp(request).getBody();
        System.out.println("_______result_______"+ result);
        assertNotNull(result);
        assertEquals(request, result);
        // Check if signUpRequest exists in the database
        SignUpRequest savedRequest = signUpRequestRepository.findById(result.getId()).orElse(null);
        assertEquals(request, savedRequest);
    }
}
