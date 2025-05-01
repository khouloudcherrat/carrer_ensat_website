package com.ENSATApp.EApp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.models.Partner;

public interface PartnerRepository extends MongoRepository<Partner, String> {
    Partner findByEmail(String email);
}
