package com.example.be_ClothingStore.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.Users;

@Repository
public interface OrderRepository extends MongoRepository<Orders, String> {
    Optional<Orders> findById(ObjectId id);
    List<Orders> findByStatus(String status);
    List<Orders> findOrderByUserID(Users user);
}
