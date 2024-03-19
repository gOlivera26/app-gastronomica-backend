package com.example.microserviceusuarios.services.Impl;

import com.example.microserviceusuarios.entities.RolEntity;
import com.example.microserviceusuarios.models.Rol;
import com.example.microserviceusuarios.repository.RolRepository;
import com.example.microserviceusuarios.repository.UsuarioRepository;
import com.example.microserviceusuarios.services.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Autowired
    public UsuarioServiceImpl(ModelMapper modelMapper, UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }


    @Override
    public Rol crearRol(Rol rol) {
        if(rol == null){
            throw new RuntimeException("El rol no puede ser nulo");
        }
        List<RolEntity> listRoles = rolRepository.findAll();
        if(listRoles.stream().anyMatch(r -> r.getDescripcion().equals(rol.getDescripcion()))){
            throw new RuntimeException("El rol ya existe");
        }
        RolEntity rolEntity = modelMapper.map(rol, RolEntity.class);
        return modelMapper.map(rolRepository.save(rolEntity), Rol.class);
    }
}
