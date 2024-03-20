package com.example.microserviceusuarios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String nroDoc;
    private String telefono;
    private Boolean activo;
    private String imagenProfile;
    private Rol rol;
}
