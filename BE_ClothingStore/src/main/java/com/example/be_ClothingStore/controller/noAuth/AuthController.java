package com.example.be_ClothingStore.controller.noAuth;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.domain.ForgotPassword.EmailRequest;
import com.example.be_ClothingStore.domain.ForgotPassword.VerificationCode;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.domain.dto.LoginDTO;
import com.example.be_ClothingStore.domain.dto.ResponseLoginDTO;
import com.example.be_ClothingStore.service.UserService;
import com.example.be_ClothingStore.service.ForgotPassword.ForgotPasswordService;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/no-auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationMangerBuilder ;
    private final ForgotPasswordService forgotPasswordService;
    public AuthController( ForgotPasswordService forgotPasswordService, UserService userService, PasswordEncoder passwordEncoder,AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationMangerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO,HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        // Xác thực user
        Authentication authentication = authenticationMangerBuilder.getObject().authenticate(authenticationToken);
        String token = this.securityUtil.createToken(authentication);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/"); // Áp dụng toàn bộ domain
        cookie.setMaxAge(60 * 60 * 24); // Hết hạn sau 1 ngày
        response.addCookie(cookie);
        ResponseLoginDTO responseDTO = new ResponseLoginDTO();
        responseDTO.setEmail(loginDTO.getEmail());
        responseDTO.setRole(authentication.getAuthorities().toString());
        return ResponseEntity.ok().body(responseDTO);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/"); // Áp dụng toàn bộ domain
        cookie.setMaxAge(0); // Hết hạn ngay
        response.addCookie(cookie);

        // xóa khỏi context
        SecurityContextHolder.clearContext();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Đăng xuất thành công!");

        return ResponseEntity.ok(responseBody);
    }

    // Có @Valid trả ra nếu sai kiểu của các trường trong BD yêu cầu
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody Users user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        Users newUser = this.userService.handleCreateUser(user);
        if (newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } else {
            RestResponse<?> errorResponse = new RestResponse<>(400, "BAD_REQUEST", "Email đã tồn tại!", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    
    @PostMapping("/forgot-password")
    public Boolean sendCodeAuthen(@RequestBody EmailRequest emailRequest) {
        if (this.userService.handleGetUserbyEmail(emailRequest.getEmail()) == null) {
            return false;
        }
        this.forgotPasswordService.generateAndSendCode(emailRequest.getEmail());
        return true;
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationCode verificationCode) {
        boolean isValid = this.forgotPasswordService.verifyCode(verificationCode.getEmail(), verificationCode.getCode(), verificationCode.getNewPassword());
        if (!isValid) {
            RestResponse<?> res = new RestResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Code không hợp lệ hoặc đã hết hạn!",
                "Code không hợp lệ hoặc đã hết hạn!",
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }    
    
}
