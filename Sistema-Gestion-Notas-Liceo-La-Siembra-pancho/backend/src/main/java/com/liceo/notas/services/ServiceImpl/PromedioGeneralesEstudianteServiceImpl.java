package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.PromedioGeneralesEstudianteDTO;
import com.liceo.notas.dtos.mappers.PromedioGeneralesEstudianteMapper;
import com.liceo.notas.entities.Curso;
import com.liceo.notas.entities.PromedioGeneralesEstudiante;
import com.liceo.notas.repositories.CursoRepository;
import com.liceo.notas.repositories.PromedioGeneralesEstudianteRepository;
import com.liceo.notas.services.PromedioGeneralesEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromedioGeneralesEstudianteServiceImpl implements PromedioGeneralesEstudianteService {

    @Autowired
    private PromedioGeneralesEstudianteRepository promedioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    @Transactional
    public PromedioGeneralesEstudianteDTO guardarPromedio(PromedioGeneralesEstudianteDTO dto) {
        // Validar que no exista ya un promedio para este estudiante en el curso
        if (promedioRepository.existsByIdUsuarioAndCursoId(dto.getIdUsuario(), dto.getIdCurso())) {
            throw new IllegalStateException("Ya existe un registro de promedio para este estudiante en el curso especificado");
        }

        // Validar que el curso exista
        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new IllegalArgumentException("El curso especificado no existe"));

        PromedioGeneralesEstudiante entidad = PromedioGeneralesEstudianteMapper.toEntity(dto);
        entidad.setCurso(curso);

        PromedioGeneralesEstudiante savedEntity = promedioRepository.save(entidad);
        return PromedioGeneralesEstudianteMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public PromedioGeneralesEstudianteDTO actualizarPromedio(PromedioGeneralesEstudianteDTO dto) {
        PromedioGeneralesEstudiante entidad = promedioRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el promedio general con ID: " + dto.getId()));

        // Validar que el curso exista si se va a modificar
        if (!entidad.getCurso().getId().equals(dto.getIdCurso())) {
            Curso curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new IllegalArgumentException("El nuevo curso especificado no existe"));
            entidad.setCurso(curso);
        }

        // Actualizar campos editables
        entidad.setPromedioGeneral(dto.getPromedioGeneral());
        entidad.setComportamiento(dto.getComportamiento());

        PromedioGeneralesEstudiante updatedEntity = promedioRepository.save(entidad);
        return PromedioGeneralesEstudianteMapper.toDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void eliminarPromedio(Integer id) {
        if (!promedioRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el promedio general con ID: " + id);
        }
        promedioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PromedioGeneralesEstudianteDTO obtenerPorId(Integer id) {
        return promedioRepository.findById(id)
                .map(PromedioGeneralesEstudianteMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el promedio general con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromedioGeneralesEstudianteDTO> listarTodos() {
        return promedioRepository.findAll()
                .stream()
                .map(PromedioGeneralesEstudianteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromedioGeneralesEstudianteDTO obtenerPorEstudianteYCurso(String idUsuario, Integer idCurso) {
        return promedioRepository.findByIdUsuarioAndCursoId(idUsuario, idCurso)
                .map(PromedioGeneralesEstudianteMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró promedio para el estudiante " + idUsuario + " en el curso " + idCurso));
    }
}