package com.example.be_ClothingStore.repository;

import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.Reviews;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;



@Repository
public interface ReviewRepository extends MongoRepository<Reviews, String>{
    Optional<Reviews> findById(ObjectId id);
    List<Reviews> findByProductID(String productID);
}
