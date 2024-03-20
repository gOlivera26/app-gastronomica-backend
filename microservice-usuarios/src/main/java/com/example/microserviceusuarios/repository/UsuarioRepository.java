package com.example.microserviceusuarios.repository;

import com.example.microserviceusuarios.entities.RolEntity;
import com.example.microserviceusuarios.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByNroDoc(String nroDoc);

    Optional<UsuarioEntity> findByUsername(String username);

    List<UsuarioEntity> findByRol(RolEntity rolEntity);
    @Query("SELECT new com.example.microserviceusuarios.entities.UsuarioEntity(u.id, u.nombre, u.apellido, u.username, u.nroDoc, u.email, u.telefono, u.password, u.activo, u.verificationCode, u.rol) FROM UsuarioEntity u WHERE u.username = :username")
    Optional<UsuarioEntity> findByUsernameWithoutImage(@Param("username") String username);
}
