package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.repository.OrderRepository;
import com.example.be_ClothingStore.repository.UserRepository;
import com.example.be_ClothingStore.service.error.IdInvalidException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    public OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

   

    public List<Orders> getOrderByStatus(String status){
        List<Orders> orders = this.orderRepository.findByStatus(status);
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

    public List<Orders> findAllOrderbyUserId(String userId){
        ObjectId userIdObj = new ObjectId(userId);
        Optional<Users> user = this.userRepository.findById(userIdObj);
        Users owner = null;
        if (user.isPresent()){
            owner = user.get();
        }
        List<Orders> orders = this.orderRepository.findOrderByUserID(owner);
        return orders;
    }

    public List<Orders> findUserOrderByStatus(String userId, String status) {
        ObjectId userIdObj = new ObjectId(userId);
        Optional<Users> user = this.userRepository.findById(userIdObj);
        Users owner = null;
        if (user.isPresent()){
            owner = user.get();
        }
        List<Orders> orders = this.orderRepository.findByUserIDAndStatus(owner, status);
        return orders;
    }
}
