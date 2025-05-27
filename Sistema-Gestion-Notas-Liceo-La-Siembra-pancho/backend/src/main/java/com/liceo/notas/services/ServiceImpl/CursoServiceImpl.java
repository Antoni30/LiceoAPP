package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.CursoDTO;
import com.liceo.notas.dtos.mappers.CursoMapper;
import com.liceo.notas.entities.AnioLectivo;
import com.liceo.notas.entities.Curso;
import com.liceo.notas.repositories.AnioLectivoRepository;
import com.liceo.notas.repositories.CursoRepository;
import com.liceo.notas.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio de Spring
public class CursoServiceImpl implements CursoService {

    @Autowired // Inyección de dependencia del repositorio de cursos
    private CursoRepository repository;

    @Autowired // Inyección de dependencia del repositorio de años lectivos
    private AnioLectivoRepository anioLectivoRepository;

    // Crea un nuevo curso
    @Override
    @Transactional // Asegura que toda la operación sea atómica
    public CursoDTO crearCurso(CursoDTO dto) {
        // Convierte el DTO a entidad
        Curso entidad = CursoMapper.toEntity(dto);

        // Busca el año lectivo en la base de datos
        AnioLectivo anioLectivo = anioLectivoRepository.findById(dto.getIdAnioLectivo())
                .orElseThrow(() -> new RuntimeException("Año lectivo no encontrado"));
        // Establece la relación con el año lectivo
        entidad.setAnioLectivo(anioLectivo);

        // Guarda la entidad y convierte el resultado a DTO
        entidad = repository.save(entidad);
        return CursoMapper.toDTO(entidad);
    }

    // Obtiene un curso por su ID
    @Override
    @Transactional(readOnly = true) // Transacción optimizada para lectura
    public Optional<CursoDTO> obtenerPorId(Integer id) {
        // Busca por ID y convierte a DTO si existe
        return repository.findById(id)
                .map(CursoMapper::toDTO);
    }

    // Obtiene todos los cursos
    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> listarTodos() {
        // Obtiene todos los cursos y los convierte a DTOs
        return repository.findAll().stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtiene cursos por año lectivo
    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> listarPorAnioLectivo(Integer idAnioLectivo) {
        // Busca cursos por ID de año lectivo y convierte a DTOs
        return repository.findByAnioLectivoId(idAnioLectivo).stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtiene cursos de años lectivos activos
    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursosActivos() {
        // Usa la consulta personalizada y convierte resultados a DTOs
        return repository.findCursosByAnioActivo().stream()
                .map(CursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Actualiza un curso existente
    @Override
    @Transactional
    public Optional<CursoDTO> actualizarCurso(Integer id, CursoDTO dto) {
        return repository.findById(id)
                .map(entidad -> {
                    // Actualiza el nombre del curso
                    entidad.setNombreCurso(dto.getNombreCurso());

                    // Actualiza el año lectivo si es diferente
                    if (!entidad.getAnioLectivo().getId().equals(dto.getIdAnioLectivo())) {
                        AnioLectivo nuevoAnio = anioLectivoRepository.findById(dto.getIdAnioLectivo())
                                .orElseThrow(() -> new RuntimeException("Año lectivo no encontrado"));
                        entidad.setAnioLectivo(nuevoAnio);
                    }

                    // Guarda los cambios y devuelve el DTO actualizado
                    return CursoMapper.toDTO(repository.save(entidad));
                });
    }

    // Elimina un curso
    @Override
    @Transactional
    public boolean eliminarCurso(Integer id) {
        return repository.findById(id)
                .map(entidad -> {
                    repository.delete(entidad); // Elimina la entidad
                    return true; // Indica éxito
                }).orElse(false); // Retorna false si no existe el curso
    }
}