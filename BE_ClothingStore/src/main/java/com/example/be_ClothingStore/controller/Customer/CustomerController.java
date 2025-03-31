package com.example.be_ClothingStore.controller.Customer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.be_ClothingStore.domain.Image;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.service.CloudinaryService;
import com.example.be_ClothingStore.service.Customer.CustomerService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final SecurityUtil securityUtil;
    private final CloudinaryService cloudinaryService;

    public CustomerController (CustomerService customerService, SecurityUtil securityUtil, CloudinaryService cloudinaryService) {
        this.customerService = customerService;
        this.securityUtil = securityUtil;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Users> getDetailCustomer(HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);

        if (userId == null) {
            throw new IdInvalidException("Token không hợp lệ hoặc hết hạn");
        }

        Users user = this.customerService.handleFetchUser(userId);
        user.setPassword(null);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "/profile/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> updateProfile(
        @RequestParam Map<String, String> updates,
        @RequestPart("avatar") MultipartFile avatar,
        @RequestPart("imageBody") MultipartFile imageBody,
        HttpServletRequest request
    ) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        Users user = this.customerService.handleFetchUser(userId);
        
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy user");
        }

        // Cập nhật các trường từ Form Data
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Users.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            }
        });

        if (avatar != null && !avatar.isEmpty()) {
            String oldPublicId = user.getAvatar() != null ? user.getAvatar().getPublicId() : null;
            Image image = cloudinaryService.uploadImage(avatar, "", oldPublicId);
            user.setAvatar(image);
        } 
        
        if (imageBody != null && !imageBody.isEmpty()) {
            String oldPublicId = user.getImageBody() != null ? user.getImageBody().getPublicId() : null;
            Image image = cloudinaryService.uploadImage(imageBody, "", oldPublicId);
            user.setImageBody(image);
        } 

        Users updatedUser = this.customerService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
    
}
