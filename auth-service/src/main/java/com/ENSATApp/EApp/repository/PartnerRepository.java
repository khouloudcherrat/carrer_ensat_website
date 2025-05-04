package com.ENSATApp.EApp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.model.Partner;

public interface PartnerRepository extends MongoRepository<Partner, String> {
    Partner findByEmail(String email);
}
