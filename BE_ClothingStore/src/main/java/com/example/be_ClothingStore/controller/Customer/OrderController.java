package com.example.be_ClothingStore.controller.Customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.be_ClothingStore.domain.Items;
import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.service.OrderService;
import com.example.be_ClothingStore.service.UserService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/customer")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final SecurityUtil securityUtil;
    public OrderController (OrderService orderService, UserService userService, SecurityUtil securityUtil){
        this.orderService = orderService;
        this.userService = userService;
        this.securityUtil = securityUtil;
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

    @GetMapping("/orders")
    public ResponseEntity<?> postMethodName(HttpServletRequest request) throws IdInvalidException {
        String token = this.securityUtil.getTokenFromCookie(request);
        if (token == null) {
            throw new IdInvalidException("Không tìm thấy token trong cookie!");
        }
        String userId = this.securityUtil.getUserIdFromToken(token);
        List<Orders> orders = this.orderService.findAllOrderbyUserId(userId);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/orders/detail/{orderId}")
    public ResponseEntity<?> getMethodName(@PathVariable("orderId") String orderID) {
        Orders order = this.orderService.findOrderById(orderID);
        if (order != null){
            return ResponseEntity.ok().body(order);
        } else {
            RestResponse<Orders> res = new RestResponse<>(HttpStatus.BAD_REQUEST.value(),
            "Không tìm thấy đơn hàng nào","Không tìm thấy đơn hàng nào", null);
           return ResponseEntity.badRequest().body(res);
        }
         
    }
    
    // Customer chỉ dc hủy đơn khi đơn chưa sang trạng thái confirm - mới pending
    @DeleteMapping("/orders/cancel-order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId){
        Orders order = this.orderService.findOrderById(orderId);
        if (order == null){
            RestResponse<Orders> res = new RestResponse<>(HttpStatus.BAD_REQUEST.value(),
            "Không tìm thấy đơn hàng nào","Không tìm thấy đơn hàng nào", null);
           return ResponseEntity.badRequest().body(res);
        } 
        if (order.getStatus().equals("pending")){
            order.setStatus("cancel");
            Orders orderCanceled = this.orderService.addAOrder(order);
            return ResponseEntity.status(HttpStatus.OK).body(orderCanceled);
        } else {
            RestResponse<Orders> res = new RestResponse<>(HttpStatus.BAD_REQUEST.value(),
            "Không thể hủy đơn - vì đơn đã được xác nhận!","Không thể hủy đơn - vì đơn đã được xác nhận!", null);
           return ResponseEntity.badRequest().body(res);
        }
    }
    
}
