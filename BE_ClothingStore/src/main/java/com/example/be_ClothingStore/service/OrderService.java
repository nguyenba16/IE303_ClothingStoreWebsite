package com.example.be_ClothingStore.service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Orders addAOrder (Orders orders){
        return this.orderRepository.save(orders);
    }

    public Orders findOrderById(String id){
        ObjectId objectId = new ObjectId(id);
        Optional<Orders> order = this.orderRepository.findById(objectId);
        if (order.isPresent()){
            return order.get();
        }
        return null;
    }
}
