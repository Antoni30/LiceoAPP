package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.AnioLectivoDTO;
import com.liceo.notas.entities.AnioLectivo;
import com.liceo.notas.repositories.AnioLectivoRepository;
import com.liceo.notas.dtos.mappers.AnioLectivoMapper;
import com.liceo.notas.services.AnioLectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service  // Marca esta clase como un componente de servicio de Spring
public class AnioLectivoServiceImpl implements AnioLectivoService {


    @Autowired // Inyecta automáticamente el repositorio
    private AnioLectivoRepository repository;

    // CREATE: Convierte DTO a entidad, la guarda y devuelve DTO
    @Override
    @Transactional // Asegura que toda la operación sea atómica
    public AnioLectivoDTO crearAnioLectivo(AnioLectivoDTO dto) {
        // 1. Convierte el DTO a entidad JPA
        AnioLectivo entidad = AnioLectivoMapper.toEntity(dto);

        // 2. Guarda la entidad en BD
        entidad = repository.save(entidad);

        // 3. Convierte la entidad guardada a DTO para la respuesta
        return AnioLectivoMapper.toDTO(entidad);
    }

    // READ: Busca por ID y devuelve Optional (puede estar vacío)
    @Override
    @Transactional(readOnly = true) // Transacción optimizada para lectura
    public Optional<AnioLectivoDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(AnioLectivoMapper::toDTO); // Convierte Optional<Entidad> a Optional<DTO>
    }

    // READ: Lista todos los años lectivos como DTOs
    @Override
    @Transactional(readOnly = true)
    public List<AnioLectivoDTO> listarTodos() {
        return repository.findAll().stream() // Obtiene todas las entidades
                .map(AnioLectivoMapper::toDTO) // Convierte cada una a DTO
                .collect(Collectors.toList()); // Devuelve como lista
    }

    // READ: Busca el año con estado "ACTIVO"
    @Override
    @Transactional(readOnly = true)
    public List<AnioLectivoDTO> obtenerAniosActivos() {
        return repository.findByEstado("Activo")
                .stream()
                .map(AnioLectivoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE: Actualiza si existe el ID, devuelve el DTO actualizado
    @Override
    @Transactional
    public Optional<AnioLectivoDTO> actualizarAnioLectivo(Integer id, AnioLectivoDTO dto) {
        return repository.findById(id)
                .map(entidad -> {
                    // Actualiza los campos de la entidad
                    entidad.setFechaInicio(dto.getFechaInicio());
                    entidad.setFechaFinal(dto.getFechaFinal());
                    entidad.setEstado(dto.getEstado());

                    // Guarda los cambios y convierte a DTO
                    return AnioLectivoMapper.toDTO(repository.save(entidad));
                });
    }

    // DELETE: Elimina si existe el ID, devuelve true/false
    @Override
    @Transactional
    public boolean eliminarAnioLectivo(Integer id) {
        return repository.findById(id)
                .map(entidad -> {
                    repository.delete(entidad); // Elimina la entidad
                    return true; // Indica éxito
                }).orElse(false); // Si no existe, devuelve false
    }

}
