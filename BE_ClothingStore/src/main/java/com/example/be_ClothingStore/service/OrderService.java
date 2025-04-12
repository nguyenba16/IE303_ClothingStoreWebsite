package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.repository.OrderRepository;
import com.example.be_ClothingStore.service.error.IdInvalidException;

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

    public List<Orders> getAllOrder(){
        List<Orders> orders = this.orderRepository.findAll();
        if (orders.size() != 0){
            return orders;
        }
        return null;
    }
    
    public Boolean deleteOrder(String orderId) throws IdInvalidException {
        ObjectId orderIdObject = new ObjectId(orderId);
        Optional<Orders> order= this.orderRepository.findById(orderIdObject);
        if (order.isEmpty()) {
            throw new IdInvalidException("Không tìm thấy đơn hàng với Id này!");
        }
        orderRepository.deleteById(orderId);
        return true;
    }
}
