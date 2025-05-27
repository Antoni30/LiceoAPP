package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.CalificacionDTO;
import com.liceo.notas.entities.Calificacion;

/**
 * Clase utilitaria para mapear entre entidades Calificacion y DTOs CalificacionDTO.
 * Proporciona conversión bidireccional para la gestión de calificaciones.
 */
public class CalificacionMapper {

    /**
     * Convierte una entidad Calificacion a su representación DTO.
     *
     * @param calificacion La entidad Calificacion a convertir (no debe ser null)
     * @return CalificacionDTO con los datos mapeados
     * @throws IllegalArgumentException si la calificación recibida es null
     * @throws IllegalStateException si la calificación no tiene usuario o actividad asociada
     */
    public static CalificacionDTO toDTO(Calificacion calificacion) {
        if (calificacion == null) {
            throw new IllegalArgumentException("La entidad Calificacion no puede ser nula");
        }
        if (calificacion.getUsuario() == null || calificacion.getActividad() == null) {
            throw new IllegalStateException("La calificación debe tener usuario y actividad asociados");
        }

        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setIdUsuario(calificacion.getUsuario().getIdUsuario());
        dto.setIdActividad(calificacion.getActividad().getId());
        dto.setNota(calificacion.getNota());
        dto.setComentario(calificacion.getComentario());
        return dto;
    }

    /**
     * Convierte un CalificacionDTO a entidad Calificacion.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Calificacion con los datos mapeados
     * @throws IllegalArgumentException si el DTO recibido es null
     * @apiNote Los campos usuario y actividad deben establecerse posteriormente en el servicio
     */
    public static Calificacion toEntity(CalificacionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO CalificacionDTO no puede ser nulo");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setId(dto.getId());
        // Usuario y Actividad se establecen en el servicio mediante sus setters
        calificacion.setNota(dto.getNota());
        calificacion.setComentario(dto.getComentario());
        return calificacion;
    }
}