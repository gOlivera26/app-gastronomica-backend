package com.example.microservicesclientes.controllers;

import com.example.microservicesclientes.models.Cliente;
import com.example.microservicesclientes.services.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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
    public ResponseEntity<Cliente> createPersona(@RequestBody Cliente cliente) {
        Cliente clienteCreado = clienteService.crearCliente(cliente);
        if(clienteCreado == null){
            log.warn("No se pudo crear el cliente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Cliente creado: {}", clienteCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }
    @DeleteMapping("/deleteCliente/{nroDoc}")
    public ResponseEntity<Cliente> deleteCliente(@PathVariable String nroDoc) {
        Cliente clienteEliminado = clienteService.eliminarClientePorNroDoc(nroDoc);
        if(clienteEliminado == null){
            log.warn("No se pudo eliminar el cliente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Cliente eliminado: {}", clienteEliminado);
        return ResponseEntity.status(HttpStatus.OK).body(clienteEliminado);
    }
    @PutMapping("/updateCliente")
    public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente) {
        Cliente clienteModificado = clienteService.modificarCliente(cliente);
        if(clienteModificado == null){
            log.warn("No se pudo modificar el cliente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Cliente modificado: {}", clienteModificado);
        return ResponseEntity.status(HttpStatus.OK).body(clienteModificado);
    }

    @GetMapping("/getAllClientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.obtenerClientes();
        if(clientes.isEmpty()){
            log.warn("No se encontraron clientes");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron clientes");
        }
        log.info("Clientes encontrados: {}", clientes);
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }
    @GetMapping("/getCliente/{nroDoc}")
    public ResponseEntity<Cliente> getCliente(@PathVariable String nroDoc) {
        Cliente cliente = clienteService.obtenerClientePorNroDoc(nroDoc);
        if(cliente == null){
            log.warn("No se encontró el cliente");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el cliente");
        }
        log.info("Cliente encontrado: {}", cliente);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

}


