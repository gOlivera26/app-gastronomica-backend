package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.TipoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProductoEntity, Long> {

}
