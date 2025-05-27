package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.CursoMateriaDTO;
import com.liceo.notas.dtos.mappers.CursoMateriaMapper;
import com.liceo.notas.entities.CursoMateria;
import com.liceo.notas.repositories.CursoMateriaRepository;
import com.liceo.notas.repositories.CursoRepository;
import com.liceo.notas.repositories.MateriaRepository;
import com.liceo.notas.services.CursoMateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoMateriaServiceImpl implements CursoMateriaService {

    @Autowired
    private CursoMateriaRepository cursoMateriaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    @Transactional
    public CursoMateriaDTO asociarCursoMateria(CursoMateriaDTO dto) {
        // Verificar que existen ambos (curso y materia)
        if (!cursoRepository.existsById(dto.getIdCurso())) {
            throw new RuntimeException("El curso especificado no existe");
        }
        if (!materiaRepository.existsById(dto.getIdMateria())) {
            throw new RuntimeException("La materia especificada no existe");
        }

        // Verificar que no exista ya la asociación
        if (cursoMateriaRepository.existsByCursoIdAndMateriaId(dto.getIdCurso(), dto.getIdMateria())) {
            throw new RuntimeException("Esta asociación curso-materia ya existe");
        }

        CursoMateria entidad = CursoMateriaMapper.toEntity(dto);
        entidad = cursoMateriaRepository.save(entidad);
        return CursoMateriaMapper.toDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoMateriaDTO> listarMateriasPorCurso(Integer idCurso) {
        return cursoMateriaRepository.findByCursoId(idCurso).stream()
                .map(CursoMateriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoMateriaDTO> listarCursosPorMateria(Integer idMateria) {
        return cursoMateriaRepository.findByMateriaId(idMateria).stream()
                .map(CursoMateriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarAsociacion(Integer idCurso, Integer idMateria) {
        cursoMateriaRepository.deleteByCursoIdAndMateriaId(idCurso, idMateria);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsociacion(Integer idCurso, Integer idMateria) {
        return cursoMateriaRepository.existsByCursoIdAndMateriaId(idCurso, idMateria);
    }
    // En CursoMateriaServiceImpl.java
    @Override
    @Transactional(readOnly = true)
    public List<CursoMateriaDTO> listarTodasLasAsociaciones() {
        return cursoMateriaRepository.findAll().stream()
                .map(CursoMateriaMapper::toDTO)
                .collect(Collectors.toList());
    }
}