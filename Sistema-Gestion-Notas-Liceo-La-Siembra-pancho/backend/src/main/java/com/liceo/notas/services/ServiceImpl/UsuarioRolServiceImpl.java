package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.UsuarioRolDTO;
import com.liceo.notas.dtos.mappers.UsuarioRolMapper;
import com.liceo.notas.entities.UsuarioRol;
import com.liceo.notas.repositories.RolRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.repositories.UsuarioRolRepository;
import com.liceo.notas.services.UsuarioRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional
    public UsuarioRolDTO asignarRolAUsuario(UsuarioRolDTO dto) {
        // Verificar que existen ambos (usuario y rol)
        if (!usuarioRepository.existsById(dto.getIdUsuario())) {
            throw new RuntimeException("El usuario especificado no existe");
        }
        if (!rolRepository.existsById(dto.getIdRol())) {
            throw new RuntimeException("El rol especificado no existe");
        }

        // Verificar que no exista ya la asignación
        if (usuarioRolRepository.existsByUsuarioIdAndRolId(dto.getIdUsuario(), dto.getIdRol())) {
            throw new RuntimeException("Esta asignación usuario-rol ya existe");
        }

        UsuarioRol entidad = UsuarioRolMapper.toEntity(dto);
        entidad = usuarioRolRepository.save(entidad);
        return UsuarioRolMapper.toDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> listarRolesPorUsuario(String idUsuario) {
        return usuarioRolRepository.findByUsuarioId(idUsuario).stream()
                .map(UsuarioRolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> listarUsuariosPorRol(Integer idRol) {
        return usuarioRolRepository.findByRolId(idRol).stream()
                .map(UsuarioRolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarAsignacion(String idUsuario, Integer idRol) {
        usuarioRolRepository.deleteByUsuarioIdAndRolId(idUsuario, idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsignacion(String idUsuario, Integer idRol) {
        return usuarioRolRepository.existsByUsuarioIdAndRolId(idUsuario, idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> listarTodasLasAsignaciones() {
        return usuarioRolRepository.findAll().stream()
                .map(UsuarioRolMapper::toDTO)
                .collect(Collectors.toList());
    }
}