package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.CursoDTO;
import com.liceo.notas.entities.Curso;

/**
 * Clase utilitaria para mapear entre entidades Curso y DTOs CursoDTO.
 * Proporciona conversión bidireccional para la gestión de cursos académicos.
 */
public class CursoMapper {

    /**
     * Convierte una entidad Curso a su representación DTO para transferencia de datos.
     *
     * @param entidad La entidad Curso a convertir (no debe ser null)
     * @return CursoDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad recibida es null
     * @throws IllegalStateException si la entidad no tiene año lectivo asociado
     */
    public static CursoDTO toDTO(Curso entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Curso no puede ser nula");
        }
        if (entidad.getAnioLectivo() == null) {
            throw new IllegalStateException("El curso debe tener un año lectivo asociado");
        }

        CursoDTO dto = new CursoDTO();
        dto.setId(entidad.getId());
        dto.setIdAnioLectivo(entidad.getAnioLectivo().getId());
        dto.setNombreCurso(entidad.getNombreCurso());
        return dto;
    }

    /**
     * Convierte un CursoDTO a entidad Curso para persistencia en base de datos.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Curso con los datos mapeados
     * @throws IllegalArgumentException si el DTO recibido es null
     * @apiNote El año lectivo debe establecerse posteriormente en el servicio
     *          mediante setAnioLectivo()
     */
    public static Curso toEntity(CursoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO CursoDTO no puede ser nulo");
        }

        Curso entidad = new Curso();
        entidad.setId(dto.getId()); // Relevante solo para operaciones de actualización
        // AnioLectivo se establece en el servicio correspondiente
        entidad.setNombreCurso(dto.getNombreCurso());
        return entidad;
    }
}