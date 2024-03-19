package com.example.microserviceusuarios.repository;

import com.example.microserviceusuarios.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByNroDoc(String nroDoc);

    Optional<UsuarioEntity> findByUsername(String username);
}
