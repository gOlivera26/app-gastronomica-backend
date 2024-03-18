package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.PedidoEntity;
import com.example.microservicesclientes.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByEstado(EstadoPedido estadoPedido);
}
