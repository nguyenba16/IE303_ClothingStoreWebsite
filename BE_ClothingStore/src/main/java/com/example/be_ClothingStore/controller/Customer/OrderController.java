package com.example.be_ClothingStore.controller.Customer;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.be_ClothingStore.domain.Items;
import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.service.OrderService;
import com.example.be_ClothingStore.service.UserService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/customer")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController (OrderService orderService, UserService userService){
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/orders/create/{userId}")
    public ResponseEntity<?> addAOrder(@RequestBody Orders orders, @PathVariable("userId") String userId) throws IdInvalidException{
        Users owner = this.userService.handleFetchUser(userId);
        if (owner == null) {
            throw new IdInvalidException("User not found with ID: " + userId);
        }
        owner.setPassword(null);
        owner.setRole(null);
        owner.setImageBody(null);
        Orders newOrder = new Orders();
        newOrder.setUserID(owner);
        List<Items> orderItems = orders.getOderItems();
        newOrder.setOderItems(orderItems);
        newOrder.setTotalPrice(orders.getTotalPrice());
        newOrder.setStatus("pending");
        Orders res = this.orderService.addAOrder(newOrder);
        if (res != null){
            return ResponseEntity.ok().body(res);
        }
        return ResponseEntity.badRequest().body(null);        
    }
}
