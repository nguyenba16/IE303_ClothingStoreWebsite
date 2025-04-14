package com.example.be_ClothingStore.controller.Admin.Orders;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.service.OrderService;
import com.example.be_ClothingStore.service.error.IdInvalidException;


@Controller
@RequestMapping("/admin")
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam(name = "status", required = false) String status) {
        List<Orders> orders = null;
        if (status != null && !status.isEmpty()) {
            orders = this.orderService.getOrderByStatus(status);
        } else {
            orders = this.orderService.getAllOrder();
        }
        if(orders == null || orders.isEmpty()) {
            RestResponse<List<Orders>> errorResponse = new RestResponse<>(HttpStatus.NOT_FOUND.value(),
                "Không tìm thấy đơn hàng nào.",
                "Không tìm thấy đơn hàng nào.",
                null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
    
    @PatchMapping("/orders/update/{orderId}")
    public ResponseEntity<?> updateOrderStarus(@RequestBody Orders updatedOrder, @PathVariable("orderId") String orderId) throws IdInvalidException{
        Orders order = this.orderService.findOrderById(orderId);
        System.out.println("====================asdsa===="+ order);
        if (order == null){
            throw new IdInvalidException("Không tìm thấy đơn hàng nào hợp lệ!");
        }
        order.setStatus(updatedOrder.getStatus());
        Orders newOrder =this.orderService.addAOrder(order);
        return ResponseEntity.ok().body(newOrder);
    }

    @DeleteMapping("/orders/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") String orderId){
        try {
            if (this.orderService.deleteOrder(orderId)) {
                RestResponse<?> response = new RestResponse<>(200, null, "Đã xóa sản phẩm thành công!", null);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (IdInvalidException ex) {
            RestResponse<?> response = new RestResponse<>(400, ex.getMessage(), ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
