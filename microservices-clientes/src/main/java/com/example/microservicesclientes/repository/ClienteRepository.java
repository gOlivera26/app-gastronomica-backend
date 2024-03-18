package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.ClienteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByNroDoc(String nroDoc);

    Optional<ClienteEntity> findByTelefono(String telefono);

    Optional<ClienteEntity> findByEmail(String email);
}
