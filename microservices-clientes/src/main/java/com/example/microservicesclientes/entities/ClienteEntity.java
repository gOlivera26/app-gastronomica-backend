package com.example.microservicesclientes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @Column
    private String telefono;
    @Column
    private String nroDoc;
    @Column
    private String email;
}
