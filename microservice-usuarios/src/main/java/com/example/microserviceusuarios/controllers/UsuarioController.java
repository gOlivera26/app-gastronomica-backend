package com.example.microserviceusuarios.controllers;

import com.example.microserviceusuarios.models.Rol;
import com.example.microserviceusuarios.services.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping("/obtenerUsuarios")
    public ResponseEntity<?> obtenerUsuarios(){
        try {
            return ResponseEntity.ok(usuarioService.obtenerUsuarios());
        } catch (RuntimeException e) {
            String errorMessage = "Error al obtener los usuarios: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/obtenerUsuarioPorRol/{idRol}")
    public ResponseEntity<?> obtenerUsuarioPorRol(@PathVariable Long idRol){
        try {
            return ResponseEntity.ok(usuarioService.obtenerUsuariosPorRol(idRol));
        } catch (RuntimeException e) {
            String errorMessage = "Error al obtener los usuarios por rol: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/emailExiste/{email}")
    public ResponseEntity<?> emailExiste(@PathVariable String email){
        try {
            return ResponseEntity.ok(usuarioService.emailExiste(email));
        } catch (RuntimeException e) {
            String errorMessage = "Error al verificar si el email existe: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/usernameExiste/{username}")
    public ResponseEntity<?> usernameExiste(@PathVariable String username){
        try {
            return ResponseEntity.ok(usuarioService.usernameExiste(username));
        } catch (RuntimeException e) {
            String errorMessage = "Error al verificar si el username existe: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/nroDocExiste/{nroDoc}")
    public ResponseEntity<?> nroDocExiste(@PathVariable String nroDoc){
        try {
            return ResponseEntity.ok(usuarioService.nroDocExiste(nroDoc));
        } catch (RuntimeException e) {
            String errorMessage = "Error al verificar si el nroDoc existe: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @PutMapping("/bajaUsuario/{nroDoc}")
    public ResponseEntity<?> bajaUsuario(@PathVariable String nroDoc){
        try {
            return ResponseEntity.ok(usuarioService.bajaUsuario(nroDoc));
        } catch (RuntimeException e) {
            String errorMessage = "Error al dar de baja el usuario: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @PutMapping("/agregarImagenProfile/{nroDoc}/imagen")
    public ResponseEntity<?> agregarImagenUsuario(@PathVariable String nroDoc,
                                                  @RequestParam("imagen") MultipartFile imagen) {
        try {
            usuarioService.agregarImagenUsuario(nroDoc, imagen);
            log.info("Imagen agregada al usuario con nroDoc: {}", nroDoc);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            String errorMessage = "Error al agregar la imagen al usuario: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/obtenerImagenProfile/{username}")
    public ResponseEntity<?> obtenerImagenUsuario(@PathVariable String username) {
        try {
            String imagenBase64 = usuarioService.obtenerImagenUsuario(username);
            return ResponseEntity.ok(imagenBase64);
        } catch (RuntimeException e) {
            String errorMessage = "Error al obtener la imagen del usuario: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
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
    @DeleteMapping("/eliminarRol/{idRol}")
    public ResponseEntity<Rol> eliminarRol(@PathVariable Long idRol){
        try {
            Rol rolEliminado = usuarioService.eliminarRol(idRol);
            log.info("Rol eliminado: {}", rolEliminado);
            return ResponseEntity.ok(rolEliminado);
        } catch (RuntimeException e) {
            String errorMessage = "Error al eliminar el rol: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/editarRol")
    public ResponseEntity<?> editarRol(@RequestBody Rol rol){
        try {
            Rol rolEditado = usuarioService.editarRol(rol);
            log.info("Rol editado: {}", rolEditado);
            return ResponseEntity.ok(rolEditado);
        } catch (RuntimeException e) {
            String errorMessage = "Error al editar el rol: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
    @GetMapping("/obtenerRoles")
    public ResponseEntity<?> obtenerRoles(){
        try {
            return ResponseEntity.ok(usuarioService.obtenerRoles());
        } catch (RuntimeException e) {
            String errorMessage = "Error al obtener los roles: " + e.getMessage();
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
