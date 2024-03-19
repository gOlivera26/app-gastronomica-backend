package com.example.microserviceusuarios.controllers;

import com.example.microserviceusuarios.dtos.*;
import com.example.microserviceusuarios.entities.UsuarioEntity;
import com.example.microserviceusuarios.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(authService.login(request));
    }
@PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegistroRequest request) {
        return ResponseEntity.ok(authService.registro(request));
    }
    @PostMapping("/update")
    public ResponseEntity<UsuarioEntity> updateBasicData(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(authService.actualizarUsuario(request));
    }
    //Metodo para enviar codigo de verificacion al mail del usuario interno
   /* @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email != null && !email.isEmpty()) {
            String verificationCode = authService.generateVerificationCode();
            authService.sendVerificationCode(email, verificationCode);
            authService.storeVerificationCodeInDatabase(email, verificationCode);
            return ResponseEntity.ok("Se ha enviado un código de verificación al email del usuario.");
        } else {
            return ResponseEntity.badRequest().body("El email no puede estar vacío.");
        }
    }

    */
    //Metodo para usar codigo de verificacion para generar token para modificar password
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody UpdateCredentialRequest resetPasswordRequest) {
        String token = authService.generateTokenForPasswordReset(resetPasswordRequest.getEmail(), resetPasswordRequest.getVerificationCode());
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    //Metodo para actualizar password mediante token obtenido en el metodo anterior
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdateCredentialResponse request) {
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
    }
}
