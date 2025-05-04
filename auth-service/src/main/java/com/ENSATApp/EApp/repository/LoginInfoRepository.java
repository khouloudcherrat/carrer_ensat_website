package com.ENSATApp.EApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ENSATApp.EApp.model.LoginInfo;

public interface LoginInfoRepository extends MongoRepository<LoginInfo, String> {
    Optional<LoginInfo> findByEmail(String email);
    List<LoginInfo> findAll();
}