package com.example.microserviceusuarios.services;

import com.example.microserviceusuarios.entities.UsuarioEntity;
import com.example.microserviceusuarios.models.Rol;
import com.example.microserviceusuarios.models.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UsuarioService {

    List<Usuario> obtenerUsuarios();
    List<Usuario> obtenerUsuariosPorRol(Long idRol);
    Rol crearRol(Rol rol);
    Rol eliminarRol(Long id);
    Rol editarRol(Rol rol);
    List<Rol> obtenerRoles();
    Boolean emailExiste(String email);
    Boolean usernameExiste(String username);
    Boolean nroDocExiste(String nroDoc);
    Boolean bajaUsuario(String nroDoc);
    void agregarImagenUsuario(String nroDoc, MultipartFile imagen);
    byte[] obtenerImagenUsuario(String nroDoc);

}
