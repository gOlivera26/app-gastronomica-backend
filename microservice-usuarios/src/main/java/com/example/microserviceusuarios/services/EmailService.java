package com.example.microserviceusuarios.services;

import com.example.microserviceusuarios.configs.EmailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public interface EmailService {
    void sendVerificationCode(String email, String verificationCode);
}
