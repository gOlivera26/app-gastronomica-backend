package com.example.appgastronomica.services.impl;

import com.example.appgastronomica.entities.ClienteEntity;
import com.example.appgastronomica.models.Cliente;
import com.example.appgastronomica.repository.ClienteRepository;
import com.example.appgastronomica.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ModelMapper modelMapper, ClienteRepository clienteRepository){
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
    }
    @Override
    public Mono<Cliente> crearCliente(Cliente cliente) {
        return Mono.fromCallable(() -> {
            Optional<ClienteEntity> existeCliente = clienteRepository.findByNroDoc(cliente.getNroDoc());
            if (existeCliente.isPresent()) {
                throw new RuntimeException("El numero de documento ya existe en otro cliente");
            }
            ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
            ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);
            return modelMapper.map(clienteGuardado, Cliente.class);
        });
    }

    @Override
    public Flux<Cliente> obtenerClientes() {
        Flux<ClienteEntity> clienteEntityFlux = Flux.fromIterable(clienteRepository.findAll());
        return clienteEntityFlux.map(clienteEntity -> modelMapper.map(clienteEntity, Cliente.class));
    }
}
