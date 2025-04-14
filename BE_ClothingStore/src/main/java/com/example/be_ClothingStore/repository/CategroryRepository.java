package com.example.be_ClothingStore.repository;

import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.Categrories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CategroryRepository extends MongoRepository<Categrories, String> {
     Optional<Categrories> findById (ObjectId id);
     Optional<Categrories> findByCategroryName(String categroryName);
}
