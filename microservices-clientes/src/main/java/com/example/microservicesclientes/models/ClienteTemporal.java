package com.example.microservicesclientes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteTemporal {
    private Long id;
    private String nombre;
    private String apellido;
}