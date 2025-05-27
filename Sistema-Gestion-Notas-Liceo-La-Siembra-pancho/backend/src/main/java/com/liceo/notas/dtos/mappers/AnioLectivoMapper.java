package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.AnioLectivoDTO;
import com.liceo.notas.entities.AnioLectivo;

/**
 * Clase utilitaria para mapear entre entidades AnioLectivo y DTOs AnioLectivoDTO.
 * Proporciona conversión bidireccional para la gestión de años lectivos.
 */
public class AnioLectivoMapper {

    /**
     * Convierte una entidad AnioLectivo a su representación DTO para respuestas API.
     *
     * @param entidad La entidad AnioLectivo a convertir (no debe ser null)
     * @return AnioLectivoDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad recibida es null
     */
    public static AnioLectivoDTO toDTO(AnioLectivo entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad AnioLectivo no puede ser nula");
        }

        AnioLectivoDTO dto = new AnioLectivoDTO();
        dto.setId(entidad.getId());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFinal(entidad.getFechaFinal());
        dto.setEstado(entidad.getEstado());
        return dto;
    }

    /**
     * Convierte un AnioLectivoDTO a entidad AnioLectivo para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad AnioLectivo con los datos mapeados
     * @throws IllegalArgumentException si el DTO recibido es null
     * @apiNote El campo ID solo se usa para operaciones de actualización
     */
    public static AnioLectivo toEntity(AnioLectivoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO AnioLectivoDTO no puede ser nulo");
        }

        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(dto.getId()); // Relevante solo para operaciones de update
        entidad.setFechaInicio(dto.getFechaInicio());
        entidad.setFechaFinal(dto.getFechaFinal());
        entidad.setEstado(dto.getEstado());
        return entidad;
    }
}