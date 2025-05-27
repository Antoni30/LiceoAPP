package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.RolDTO;
import com.liceo.notas.dtos.mappers.RolMapper;
import com.liceo.notas.entities.Rol;
import com.liceo.notas.repositories.RolRepository;
import com.liceo.notas.services.RolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class RolServiceImpl implements RolService {
    @Autowired
    private RolRepository repository;

    @Override
    @Transactional
    public RolDTO crearRol(RolDTO dto) {
        Rol entidad = RolMapper.toEntity(dto);
        entidad = repository.save(entidad);
        return RolMapper.toDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(RolMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> listarTodos() {
        return repository.findAll().stream()
                .map(RolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<RolDTO> actualizarRol(Integer id, RolDTO dto) {
        return repository.findById(id)
                .map(entidad -> {
                    entidad.setNombre(dto.getNombre());
                    return RolMapper.toDTO(repository.save(entidad));
                });
    }

    @Override
    @Transactional
    public boolean eliminarRol(Integer id) {
        return repository.findById(id)
                .map(entidad -> {
                    repository.delete(entidad);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .map(RolMapper::toDTO);
    }
}