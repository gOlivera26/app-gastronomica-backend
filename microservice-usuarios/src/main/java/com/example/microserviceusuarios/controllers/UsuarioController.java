package com.example.microserviceusuarios.controllers;

import com.example.microserviceusuarios.models.Rol;
import com.example.microserviceusuarios.services.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crearRol")
    public ResponseEntity<?> crearRol(@RequestBody Rol rol){
        try {
            Rol rolCreado = usuarioService.crearRol(rol);
            log.info("Rol creado: {}", rolCreado);
            return ResponseEntity.ok(rolCreado);
        } catch (RuntimeException e) {
            String errorMessage = "Error al crear el rol: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
