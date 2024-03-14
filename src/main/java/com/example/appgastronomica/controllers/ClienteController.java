package com.example.appgastronomica.controllers;

import com.example.appgastronomica.models.Cliente;
import com.example.appgastronomica.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cliente")
public class ClienteController {
    public final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/postCliente")
    public Mono<ResponseEntity<Cliente>> createPersona(@RequestBody Cliente cliente) {
        if (cliente == null) {
            log.warn("Se recibió una solicitud con cliente nulo.");
            return Mono.error(new IllegalArgumentException("Cliente no puede ser nulo"));
        }
        return clienteService.crearCliente(cliente)
                .map(createdCliente -> {
                    log.info("Cliente creado con éxito: {}", createdCliente);
                    return ResponseEntity.ok(createdCliente);
                })
                .onErrorMap(e -> {
                    log.error("Error al crear cliente: {}", e.getMessage());
                    return e;
                });
    }

    @GetMapping("/getAllClientes")
    public Flux<Cliente> getAllClientes() {
        return clienteService.obtenerClientes()
                .collectList()
                .flatMapMany(clientes -> {
                    if (clientes.isEmpty()) {
                        log.warn("No se encontraron clientes.");
                        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron clientes"));
                    }
                    log.info("Clientes encontrados: {}", clientes);
                    return Flux.fromIterable(clientes);
                });
    }

}


