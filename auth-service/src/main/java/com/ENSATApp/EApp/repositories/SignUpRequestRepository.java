package com.ENSATApp.EApp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.models.SignUpRequest;

public interface SignUpRequestRepository extends MongoRepository<SignUpRequest, String> {}
