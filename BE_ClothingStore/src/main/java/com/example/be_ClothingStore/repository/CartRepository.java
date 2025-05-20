package com.example.be_ClothingStore.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

import com.example.be_ClothingStore.domain.Carts;
import com.example.be_ClothingStore.domain.Users;

@Repository
public interface CartRepository extends MongoRepository<Carts, String> {
    @Query(value = "{ 'userID.$id': ?0 }", exists = true)
    boolean existsByUserID(ObjectId userId);

    @Query(value = "{ 'userID.$id': ?0 }")
    Optional<Carts> findByUserID(ObjectId userId);

    @Query(value = "{ 'userID.$id': ?0 }")
    void deleteByUserID(ObjectId userId);
}
