package com.example.be_ClothingStore.repository;

import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.Products;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


@Repository
public interface ProductRepository extends MongoRepository<Products, String> {
    Optional<Products> findById(ObjectId id);
}
