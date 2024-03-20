package com.example.microserviceusuarios.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String nroDoc;
    private String email;

}
