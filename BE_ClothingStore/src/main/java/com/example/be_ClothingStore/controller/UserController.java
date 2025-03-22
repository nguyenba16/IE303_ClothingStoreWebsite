package com.example.be_ClothingStore.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.service.UserService;
import com.example.be_ClothingStore.service.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users/create")
    public ResponseEntity<Users> createNewUser (@RequestBody Users user) {
        // mã hóa Password
        String  hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        Users newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
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
