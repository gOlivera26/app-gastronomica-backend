package com.example.appgastronomica.repository;

import com.example.appgastronomica.entities.ClienteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByNroDoc(String nroDoc);
}
