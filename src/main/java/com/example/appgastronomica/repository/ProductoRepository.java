package com.example.appgastronomica.repository;

import com.example.appgastronomica.entities.ProductoEntity;
import com.example.appgastronomica.entities.TipoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByTipoProducto(TipoProductoEntity tipoProductoEntity);
}
