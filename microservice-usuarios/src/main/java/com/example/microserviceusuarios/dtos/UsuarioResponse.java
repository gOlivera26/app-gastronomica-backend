package com.example.microserviceusuarios.dtos;

import com.example.microserviceusuarios.models.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String nroDoc;
    private String telefono;
    private Boolean activo;
    private Rol rol;
}
