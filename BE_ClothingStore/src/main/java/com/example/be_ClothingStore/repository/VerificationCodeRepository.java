package com.example.be_ClothingStore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.ForgotPassword.VerificationCode;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends MongoRepository<VerificationCode, ObjectId> {
    Optional<VerificationCode> findByEmail(String email);
}
