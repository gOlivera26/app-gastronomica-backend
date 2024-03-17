package com.example.appgastronomica.repository;

import com.example.appgastronomica.entities.PedidoEntity;
import com.example.appgastronomica.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByEstado(EstadoPedido estadoPedido);
}
