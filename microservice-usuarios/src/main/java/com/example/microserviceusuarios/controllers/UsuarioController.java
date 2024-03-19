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
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol){
        Rol rolCreado = usuarioService.crearRol(rol);
        if(rolCreado == null){
            log.warn("No se pudo crear el rol");
            return ResponseEntity.badRequest().build();
        }
        log.info("Rol creado: {}", rolCreado);
        return ResponseEntity.ok(rolCreado);
    }
}
