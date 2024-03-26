package com.example.microserviceusuarios.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String username;
    @Column
    private String nroDoc;

    @Column
    private String email;

    @Column
    private String telefono;
    @Column
    private String password;
    @Column
    private Boolean activo;
    @Column
    private String verificationCode;
    @Column
    @Lob
    private byte[] imagenProfile;
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private RolEntity rol;

    public UsuarioEntity(Long id, String nombre, String apellido, String username, String nroDoc, String email, String telefono, String password, Boolean activo, String verificationCode, RolEntity rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.nroDoc = nroDoc;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.activo = activo;
        this.verificationCode = verificationCode;
        this.rol = rol;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolDescripcion = rol.getDescripcion();
        switch (rolDescripcion) {
            case "Admin":
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case "Atencion al publico":
                return List.of(new SimpleGrantedAuthority("ROLE_PUBLIC_ATTENDANT"));
            case "Repartidor":
                return List.of(new SimpleGrantedAuthority("ROLE_DELIVERY_PERSON"));
            case "Cocinero":
                return List.of(new SimpleGrantedAuthority("ROLE_COOK"));
            default:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
