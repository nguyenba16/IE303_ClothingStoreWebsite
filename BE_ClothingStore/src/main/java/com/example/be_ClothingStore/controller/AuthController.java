package com.example.be_ClothingStore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.dto.LoginDTO;
import com.example.be_ClothingStore.domain.dto.ResponseLoginDTO;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.validation.Valid;


@RestController
public class AuthController {
    private final SecurityUtil securityUtil;
    private final AuthenticationManagerBuilder authenticationMangerBuilder ;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationMangerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        // Xác thực user
        Authentication authentication = authenticationMangerBuilder.getObject().authenticate(authenticationToken);

        String token = this.securityUtil.createToken(authentication);
        ResponseLoginDTO res = new ResponseLoginDTO();
        res.setAccessToken(token);
        // Lấy bằng cách
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String email = authentication.getName(); // Lấy email của user
        // String role = authentication.getAuthorities().toString(); // Lấy quyền
        return ResponseEntity.ok().body(res);
    }
    
}
