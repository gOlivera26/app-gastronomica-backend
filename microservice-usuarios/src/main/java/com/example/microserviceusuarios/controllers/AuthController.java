package com.example.microserviceusuarios.controllers;

import com.example.microserviceusuarios.dtos.*;
import com.example.microserviceusuarios.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("Usuario no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else if (errorMessage.equals("Usuario no activo")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
             else {
                log.error("Error during login: {}", errorMessage);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }

    @PostMapping("/registerUsuario")
    public ResponseEntity<?> register(@RequestBody RegistroRequest request) {
        try {
            return ResponseEntity.ok(authService.registro(request));
        } catch (RuntimeException e) {
            log.error("Error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/updateUsuario")
    public ResponseEntity<?> updateBasicData(@RequestBody UpdateUserRequest request) {
        try {
            return ResponseEntity.ok(authService.actualizarUsuario(request));
        } catch (IllegalArgumentException e) {
            log.error("Error during user data update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error during user data update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Metodo para enviar codigo de verificacion al mail del usuario interno
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String email = request.getEmail();
            if (email != null && !email.isEmpty()) {
                String verificationCode = authService.generateVerificationCode();
                authService.sendVerificationCode(email, verificationCode);
                authService.storeVerificationCodeInDatabase(email, verificationCode);
                return ResponseEntity.ok("Se ha enviado un código de verificación al email del usuario.");
            } else {
                return ResponseEntity.badRequest().body("El email no puede estar vacío.");
            }
        } catch (RuntimeException e) {
            log.error("Error during password reset request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Metodo para usar codigo de verificacion para generar token para modificar password
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody UpdateCredentialRequest resetPasswordRequest) {
        try {
            String token = authService.generateTokenForPasswordReset(resetPasswordRequest.getEmail(), resetPasswordRequest.getVerificationCode());
            return ResponseEntity.ok(AuthResponse.builder().token(token).build());
        } catch (RuntimeException e) {
            log.error("Error during password reset: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Metodo para actualizar password mediante token obtenido en el metodo anterior
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdateCredentialResponse request) {
        try {
            String token = request.getToken();
            String newPassword = request.getNewPassword();
            if (token != null && !token.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
                if (authService.actualizarPasswordUsingToken(token, newPassword)) {
                    return ResponseEntity.ok("La contraseña se ha actualizado correctamente.");
                } else {
                    return ResponseEntity.badRequest().body("No se ha podido actualizar la contraseña.");
                }
            } else {
                return ResponseEntity.badRequest().body("El token y la nueva contraseña no pueden estar vacíos.");
            }
        } catch (RuntimeException e) {
            log.error("Error during password update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
