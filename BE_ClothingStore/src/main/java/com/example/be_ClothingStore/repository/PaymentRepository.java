package com.example.be_ClothingStore.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.be_ClothingStore.domain.Payments;

@Repository
public interface PaymentRepository extends MongoRepository<Payments, String> {
    Optional<Payments> findById(ObjectId id);
    Optional<Payments> findByOrderID(String orderID);
}
