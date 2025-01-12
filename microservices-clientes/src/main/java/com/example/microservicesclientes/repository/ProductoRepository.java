package com.example.microservicesclientes.repository;

import com.example.microservicesclientes.entities.ProductoEntity;
import com.example.microservicesclientes.entities.TipoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByTipoProducto(TipoProductoEntity tipoProductoEntity);

    Optional<ProductoEntity> findByNovedadTrue();
}
