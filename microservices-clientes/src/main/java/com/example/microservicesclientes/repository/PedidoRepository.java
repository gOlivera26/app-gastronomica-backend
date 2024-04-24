package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.PedidoEntity;
import com.example.microservicesclientes.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByEstado(EstadoPedido estadoPedido);

    @Query(value = "SELECT " +
            "c.id AS cliente_id, " +
            "c.nombre AS nombre_cliente, " +
            "c.apellido, " +
            "c.nro_doc, " +
            "c.telefono, " +
            "c.email, " +
            "p.id AS pedido_id, " +
            "p.fecha_pedido, " +
            "p.observacion AS observacion_pedido, " +
            "p.total AS total_pedido, " +
            "dp.id AS detalle_pedido_id, " +
            "dp.cantidad, " +
            "dp.subtotal, " +
            "pr.id AS producto_id, " +
            "pr.nombre AS nombre_producto " +
            "FROM " +
            "public.cliente c " +
            "JOIN public.pedido p ON c.id = p.cliente_id " +
            "JOIN public.detalle_pedido dp ON p.id = dp.pedido_id " +
            "JOIN public.producto pr ON dp.producto_id = pr.id " +
            "ORDER BY " +
            "c.id, p.id, dp.id", nativeQuery = true)
    List<Object[]> obtenerPedidosConDetallesYCliente();
}
