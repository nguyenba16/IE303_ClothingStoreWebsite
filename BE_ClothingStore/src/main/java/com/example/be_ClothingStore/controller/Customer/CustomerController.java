package com.example.be_ClothingStore.controller.Customer;

import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.service.Customer.CustomerService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final SecurityUtil securityUtil;

    public CustomerController (CustomerService customerService, SecurityUtil securityUtil) {
        this.customerService = customerService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<Users> getDetailCustomer(HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);

        if (userId == null) {
            throw new IdInvalidException("Token không hợp lệ hoặc hết hạn");
        }

        Users user = this.customerService.handleFetchUser(userId);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy user");
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> updates ,HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        Users user = this.customerService.handleFetchUser(userId);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy user");
        }
        updates.forEach((key, value)-> {
            Field field = ReflectionUtils.findField(Users.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            }
        });
        Users updatedUser = this.customerService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // còn sửa ava, sửa ảnh fit

    
}
