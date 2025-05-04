package com.ENSATApp.EApp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.model.SignUpRequest;

public interface SignUpRequestRepository extends MongoRepository<SignUpRequest, String> {}
