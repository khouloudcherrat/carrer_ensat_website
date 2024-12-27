package com.ENSATApp.EApp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.models.LoginInfo;

public interface LoginInfoRepository extends MongoRepository<LoginInfo, String> {
    Optional<LoginInfo> findByEmail(String email);
}