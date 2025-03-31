package com.example.be_ClothingStore.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.service.UserService;
import com.example.be_ClothingStore.service.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<Users> getUserByID(@PathVariable("id") String id) 
    //ném ra global exception
    throws IdInvalidException {
        Users user = this.userService.handleFetchUser(id);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy user");
        } 
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    
}
