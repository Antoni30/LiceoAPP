package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.PromedioGeneralesEstudianteDTO;
import com.liceo.notas.entities.PromedioGeneralesEstudiante;

/**
 * Mapper para convertir entre la entidad {@link PromedioGeneralesEstudiante} y su DTO asociado {@link PromedioGeneralesEstudianteDTO}.
 * Proporciona métodos estáticos para mapear objetos en ambas direcciones.
 */
public class PromedioGeneralesEstudianteMapper {

    /**
     * Convierte una entidad {@link PromedioGeneralesEstudiante} en un objeto DTO {@link PromedioGeneralesEstudianteDTO}.
     *
     * @param entidad La entidad a convertir. No puede ser null.
     * @return Un objeto DTO con los datos de la entidad
     * @throws IllegalArgumentException si la entidad es null
     */
    public static PromedioGeneralesEstudianteDTO toDTO(PromedioGeneralesEstudiante entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad PromedioGeneralesEstudiante no puede ser nula");
        }

        PromedioGeneralesEstudianteDTO dto = new PromedioGeneralesEstudianteDTO();
        dto.setId(entidad.getId());
        dto.setIdUsuario(entidad.getIdUsuario());
        dto.setIdCurso(entidad.getCurso().getId());
        dto.setPromedioGeneral(entidad.getPromedioGeneral());
        dto.setComportamiento(entidad.getComportamiento());

        return dto;
    }

    /**
     * Convierte un objeto DTO {@link PromedioGeneralesEstudianteDTO} en una entidad {@link PromedioGeneralesEstudiante}.
     * No se asigna el campo del curso completo, solo idCurso desde el DTO.
     *
     * @param dto El DTO a convertir. No puede ser null.
     * @return Una entidad con los datos del DTO
     * @throws IllegalArgumentException si el DTO es null
     */
    public static PromedioGeneralesEstudiante toEntity(PromedioGeneralesEstudianteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO PromedioGeneralesEstudianteDTO no puede ser nulo");
        }

        PromedioGeneralesEstudiante entidad = new PromedioGeneralesEstudiante();
        entidad.setId(dto.getId());
        entidad.setIdUsuario(dto.getIdUsuario());
        entidad.setPromedioGeneral(dto.getPromedioGeneral());
        entidad.setComportamiento(dto.getComportamiento());

        return entidad;
    }
}