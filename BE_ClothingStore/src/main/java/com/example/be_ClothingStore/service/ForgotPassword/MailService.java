package com.example.be_ClothingStore.service.ForgotPassword;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService (JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Elegante - Xác thực khôi phục mật khẩu");
        message.setText("Mã xác thực của bạn là: " + code + ". Mã có hiệu lực trong 10 phút.");
        mailSender.send(message);
    }
}
