package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {
}
