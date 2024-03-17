package com.example.appgastronomica.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String nroDoc;
    private String email;

    public String nombreCompleto(){
        return this.getNombre()+" "+this.getApellido();
    }
}
