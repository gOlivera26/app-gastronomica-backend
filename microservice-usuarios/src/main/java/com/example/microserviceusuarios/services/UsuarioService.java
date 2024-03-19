package com.example.microserviceusuarios.services;

import com.example.microserviceusuarios.models.Rol;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {

    Rol crearRol(Rol rol);

}
