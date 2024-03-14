package com.example.appgastronomica.services;

import com.example.appgastronomica.models.Cliente;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface ClienteService {
    Mono<Cliente> crearCliente(Cliente cliente);
    Flux<Cliente> obtenerClientes();
}
