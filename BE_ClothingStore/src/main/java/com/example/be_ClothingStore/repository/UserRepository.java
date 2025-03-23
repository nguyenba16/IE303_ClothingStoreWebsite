package com.example.be_ClothingStore.repository;
import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.Users;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<Users, String>  {
    // Để đổi tìm kiểu objectid chứ k tìm kiểu String
    Optional<Users> findById(ObjectId id);
    Optional<Users> findByEmail(String email);
}