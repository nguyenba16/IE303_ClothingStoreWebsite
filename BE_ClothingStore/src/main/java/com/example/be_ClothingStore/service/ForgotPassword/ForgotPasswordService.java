package com.example.be_ClothingStore.service.ForgotPassword;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.domain.ForgotPassword.VerificationCode;
import com.example.be_ClothingStore.repository.UserRepository;
import com.example.be_ClothingStore.repository.VerificationCodeRepository;
import com.example.be_ClothingStore.service.UserService;

@Service
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MailService mailService;
    private final UserService userseService;
    private final PasswordEncoder passwordEncoder;
    public ForgotPasswordService(PasswordEncoder passwordEncoder, UserService userService ,UserRepository userRepository, VerificationCodeRepository verificationCodeRepository, MailService mailService){
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailService= mailService;
        this.userseService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Async
    public void generateAndSendCode(String email) {
        Optional<Users> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) throw new RuntimeException("Email không tồn tại!");

        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6 số
        VerificationCode vcode = new VerificationCode();
        vcode.setCode(code); 
        vcode.setEmail(email);
        vcode.setExpiredAt(LocalDateTime.now().plusMinutes(10));

        // Xóa mã code trước đó của email này.
        Optional<VerificationCode> verificationCode = this.verificationCodeRepository.findByEmail(email);
        if (verificationCode.isPresent()) {
            VerificationCode vertiCode = verificationCode.get();
            this.verificationCodeRepository.deleteById(vertiCode.getId());
        }
        verificationCodeRepository.save(vcode);

        mailService.sendVerificationCode(email, code);
    }

    public Boolean verifyCode(String email, String code, String newPassword){
        Optional<VerificationCode> verificationCode = this.verificationCodeRepository.findByEmail(email);
        if (verificationCode.isPresent()) {
            VerificationCode verifyCode = verificationCode.get();
            // Kiểm tra xem có hết hạn hoặc k đúng code không
            if (verifyCode.getCode().equals(code) && verifyCode.getExpiredAt().isAfter(LocalDateTime.now())){
                Users user = this.userseService.handleGetUserbyEmail(email);
                String hashPassword = this.passwordEncoder.encode(newPassword);
                user.setPassword(hashPassword);
                this.userRepository.save(user);
                return true;
            }
            return false;
        }
        // Không tồn tại email nào
        return false;
    }
}
