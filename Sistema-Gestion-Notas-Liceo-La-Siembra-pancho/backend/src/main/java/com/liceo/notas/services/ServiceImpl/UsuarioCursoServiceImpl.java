package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.UsuarioCursoDTO;
import com.liceo.notas.dtos.mappers.UsuarioCursoMapper;
import com.liceo.notas.entities.UsuarioCurso;
import com.liceo.notas.repositories.CursoRepository;
import com.liceo.notas.repositories.UsuarioCursoRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.services.UsuarioCursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioCursoServiceImpl implements UsuarioCursoService {
    @Autowired
    private UsuarioCursoRepository usuarioCursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional
    public UsuarioCursoDTO asociarUsuarioCurso(UsuarioCursoDTO dto) {
        // Verificar que existen ambos (usuario y curso)
        if (!usuarioRepository.existsById(dto.getIdUsuario())) {
            throw new RuntimeException("El usuario especificado no existe");
        }
        if (!cursoRepository.existsById(dto.getIdCurso())) {
            throw new RuntimeException("El curso especificado no existe");
        }

        // Verificar que no exista ya la asociación
        if (usuarioCursoRepository.existsByUsuarioIdAndCursoId(dto.getIdUsuario(), dto.getIdCurso())) {
            throw new RuntimeException("Esta asociación usuario-curso ya existe");
        }

        UsuarioCurso entidad = UsuarioCursoMapper.toEntity(dto);
        entidad = usuarioCursoRepository.save(entidad);
        return UsuarioCursoMapper.toDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCursoDTO> listarCursosPorUsuario(String idUsuario) {
        return usuarioCursoRepository.findByUsuarioId(idUsuario).stream()
                .map(UsuarioCursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCursoDTO> listarUsuariosPorCurso(Integer idCurso) {
        return usuarioCursoRepository.findByCursoId(idCurso).stream()
                .map(UsuarioCursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarAsociacion(String idUsuario, Integer idCurso) {
        usuarioCursoRepository.deleteByUsuarioIdAndCursoId(idUsuario, idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsociacion(String idUsuario, Integer idCurso) {
        return usuarioCursoRepository.existsByUsuarioIdAndCursoId(idUsuario, idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCursoDTO> listarTodasLasAsociaciones() {
        return usuarioCursoRepository.findAll().stream()
                .map(UsuarioCursoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
