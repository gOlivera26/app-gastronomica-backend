package com.example.microserviceusuarios.services;

import com.example.microserviceusuarios.dtos.AuthResponse;
import com.example.microserviceusuarios.dtos.LoginRequest;
import com.example.microserviceusuarios.dtos.RegistroRequest;
import com.example.microserviceusuarios.dtos.UpdateUserRequest;
import com.example.microserviceusuarios.entities.UsuarioEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse registro(RegistroRequest registroRequest);
    UsuarioEntity actualizarUsuario(UpdateUserRequest updateUserRequest);
    AuthResponse login(LoginRequest loginRequest);
    boolean actualizarPasswordUsingToken(String token, String newPassword);
    boolean verificarPasswordResetCode(String email, String verificationCode);
    void storeVerificationCodeInDatabase(String email, String verificationCode);
    String generateTokenForPasswordReset(String email, String verificationCode);
    String generateVerificationCode();
    String forgotPassword(String email);
    void sendVerificationCode(String email, String verificationCode);
}
