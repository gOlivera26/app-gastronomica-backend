package com.example.microserviceusuarios.services.Impl;

import com.example.microserviceusuarios.dtos.AuthResponse;
import com.example.microserviceusuarios.dtos.LoginRequest;
import com.example.microserviceusuarios.dtos.RegistroRequest;
import com.example.microserviceusuarios.dtos.UpdateUserRequest;
import com.example.microserviceusuarios.entities.RolEntity;
import com.example.microserviceusuarios.entities.UsuarioEntity;
import com.example.microserviceusuarios.jwt.JwtService;
import com.example.microserviceusuarios.repository.RolRepository;
import com.example.microserviceusuarios.repository.UsuarioRepository;
import com.example.microserviceusuarios.services.AuthService;
import com.example.microserviceusuarios.services.EmailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, ModelMapper modelMapper, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public AuthResponse registro(RegistroRequest registroRequest) {
        Optional<UsuarioEntity> existingUserEmail = usuarioRepository.findByEmail(registroRequest.getEmail());
        if(existingUserEmail.isPresent()) {
            throw new RuntimeException("Email está en uso");
        }
        Optional<UsuarioEntity> existingUserDocNumber = usuarioRepository.findByNroDoc(registroRequest.getNroDoc());
        if(existingUserDocNumber.isPresent()) {
            throw new RuntimeException("Número de documento ya está en uso");
        }
        Optional<UsuarioEntity> existingUsername = usuarioRepository.findByUsername(registroRequest.getUsername());
        if(existingUsername.isPresent()) {
            throw new RuntimeException("Nombre de usuario ya está en uso");
        }

        RolEntity rol = rolRepository.findById(registroRequest.getIdRol().getId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nombre(registroRequest.getNombre())
                .apellido(registroRequest.getApellido())
                .username(registroRequest.getUsername())
                .nroDoc(registroRequest.getNroDoc())
                .email(registroRequest.getEmail())
                .telefono(registroRequest.getTelefono())
                .password(passwordEncoder.encode(registroRequest.getPassword()))
                .activo(true)
                .rol(rol)
                .build();
        usuarioRepository.save(usuarioEntity);

        return AuthResponse.builder()
                .token(jwtService.getToken((UserDetails) usuarioEntity))
                .build();
    }


    @Transactional
    @Override
    public UsuarioEntity actualizarUsuario(UpdateUserRequest updateUserRequest) {
        if(updateUserRequest == null){
            throw new IllegalArgumentException("La solicitud de actualización no debe ser nula");
        }
        UsuarioEntity usuarioEntity = usuarioRepository.findById(updateUserRequest.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<UsuarioEntity> existeEmail = usuarioRepository.findByEmail(updateUserRequest.getEmail());
        if(existeEmail.isPresent() && !existeEmail.get().getId().equals(updateUserRequest.getId())) {
            throw new RuntimeException("Email está en uso");
        }
        Optional<UsuarioEntity> existeNroDoc = usuarioRepository.findByNroDoc(updateUserRequest.getNroDoc());
        if(existeNroDoc.isPresent() && !existeNroDoc.get().getId().equals(updateUserRequest.getId())) {
            throw new RuntimeException("Número de documento ya está en uso");
        }
        Optional<UsuarioEntity> existeUsername = usuarioRepository.findByUsername(updateUserRequest.getUsername());
        if(existeUsername.isPresent() && !existeUsername.get().getId().equals(updateUserRequest.getId())) {
            throw new RuntimeException("Nombre de usuario ya está en uso");
        }

        usuarioEntity.setNombre(updateUserRequest.getNombre());
        usuarioEntity.setApellido(updateUserRequest.getApellido());
        usuarioEntity.setNroDoc(updateUserRequest.getNroDoc());
        usuarioEntity.setTelefono(updateUserRequest.getTelefono());
        usuarioEntity.setUsername(updateUserRequest.getUsername());
        usuarioEntity.setEmail(updateUserRequest.getEmail());

        RolEntity rol = rolRepository.findById(updateUserRequest.getIdRol().getId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuarioEntity.setRol(rol);

        usuarioEntity.setEmail(updateUserRequest.getEmail());
        usuarioEntity.setActivo(updateUserRequest.getActivo());
        return usuarioRepository.save(usuarioEntity);
    }

    @Transactional
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Boolean active = usuarioRepository.findByUsername(loginRequest.getUsername())
                .map(UsuarioEntity::getActivo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if(!active){
            throw new RuntimeException("Usuario no activo");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails user = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }


    @Transactional
    @Override
    public boolean actualizarPasswordUsingToken(String token, String newPassword) {
        String username = jwtService.getUsernameFromToken(token);

        if(username != null){
            UsuarioEntity usuarioEntity = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            usuarioEntity.setPassword(passwordEncoder.encode(newPassword));
            usuarioEntity.setVerificationCode(null);
            usuarioRepository.save(usuarioEntity);
            return true; //contraseña actualizada
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean verificarPasswordResetCode(String email, String verificationCode) {
        return usuarioRepository.findByUsername(email)
                .map(user -> {
                    String storedVerificationCode = user.getVerificationCode();
                    return storedVerificationCode != null && storedVerificationCode.equals(verificationCode);
                })
                .orElse(false);
    }

    @Transactional
    @Override
    public void storeVerificationCodeInDatabase(String email, String verificationCode) {
        UsuarioEntity user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerificationCode(verificationCode);
        usuarioRepository.save(user);
    }
    @Transactional
   @Override
   public void sendVerificationCode(String email, String verificationCode) {
        emailService.sendVerificationCode(email, verificationCode);
    }

    @Transactional
    @Override
    public String generateTokenForPasswordReset(String username, String verificationCode) {
        //Verifica el código de verificación para el usuario con el correo electrónico dado
        if (verificarPasswordResetCode(username, verificationCode)) {
            //Genera un token para permitir la modificación de las credenciales
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.getToken(userDetails);
        } else {
            throw new RuntimeException("Código de verificación no válido");
        }
    }

    @Override
    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
