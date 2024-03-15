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
    public Cliente crearCliente(Cliente cliente) {
        Optional<ClienteEntity> clienteEntities = clienteRepository.findByNroDoc(cliente.getNroDoc());
        if(clienteEntities.isPresent()){
            throw new RuntimeException("El numero de documento ya pertenece a un cliente");
        }
        ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
        return modelMapper.map(clienteRepository.save(clienteEntity), Cliente.class);
    }

    @Override
    public Cliente eliminarClientePorNroDoc(String nroDoc) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findByNroDoc(nroDoc);
        if(clienteEntity.isEmpty()){
            throw new RuntimeException("El cliente no existe");
        }
        clienteRepository.delete(clienteEntity.get());
        return modelMapper.map(clienteEntity.get(), Cliente.class);
    }

    @Override
    public Cliente modificarCliente(Cliente cliente) {
        if(cliente.getId() == null){
            throw new RuntimeException("El ID no puede ser nulo");
        }
        Optional<ClienteEntity> clienteEntityOptional = clienteRepository.findById(cliente.getId());
        if(clienteEntityOptional.isPresent()){
            ClienteEntity clienteEntity = clienteEntityOptional.get();
            if(!clienteEntity.getNroDoc().equals(cliente.getNroDoc()) && clienteRepository.findByNroDoc(cliente.getNroDoc()).isPresent()){
                throw new RuntimeException("El numero de documento ya pertenece a un cliente");
            }
            if(!clienteEntity.getTelefono().equals(cliente.getTelefono()) && clienteRepository.findByTelefono(cliente.getTelefono()).isPresent()){
                throw new RuntimeException("El telefono ya pertenece a un cliente");
            }
            clienteEntity.setNombre(cliente.getNombre());
            clienteEntity.setApellido(cliente.getApellido());
            clienteEntity.setNroDoc(cliente.getNroDoc());
            clienteEntity.setTelefono(cliente.getTelefono());

            ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);
            return modelMapper.map(clienteGuardado, Cliente.class);
        }
        else{
            throw new RuntimeException("El cliente no existe");
        }
    }


    @Override
    public List<Cliente> obtenerClientes() {
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();
        return clienteEntities.stream().map(clienteEntity -> modelMapper.map(clienteEntity, Cliente.class)).toList();
    }


}
