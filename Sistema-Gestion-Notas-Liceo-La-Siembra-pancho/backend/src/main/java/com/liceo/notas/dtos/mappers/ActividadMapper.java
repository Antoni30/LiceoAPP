package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.ActividadDTO;
import com.liceo.notas.entities.Actividad;

/**
 * Clase utilitaria para mapear entre entidades Actividad y DTOs ActividadDTO.
 * Proporciona métodos estáticos para conversión bidireccional.
 */
public class ActividadMapper {

    /**
     * Convierte una entidad Actividad a su correspondiente DTO.
     *
     * @param actividad Entidad Actividad a convertir
     * @return ActividadDTO con los datos mapeados
     * @throws NullPointerException si la actividad recibida es null
     */
    public static ActividadDTO toDTO(Actividad actividad) {
        if (actividad == null) {
            throw new NullPointerException("La actividad no puede ser nula");
        }

        ActividadDTO dto = new ActividadDTO();
        dto.setId(actividad.getId());
        dto.setIdParcial(actividad.getParcial().getId());
        dto.setTipoActividad(actividad.getTipoActividad());
        dto.setFecha(actividad.getFecha());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setValorMaximo(actividad.getValorMaximo());
        return dto;
    }

    /**
     * Convierte un ActividadDTO a su correspondiente entidad Actividad.
     *
     * @param dto ActividadDTO a convertir
     * @return Entidad Actividad con los datos mapeados
     * @throws NullPointerException si el DTO recibido es null
     * @apiNote El campo Parcial no se establece en el mapeo y debe asignarse posteriormente
     */
    public static Actividad toEntity(ActividadDTO dto) {
        if (dto == null) {
            throw new NullPointerException("El DTO no puede ser nulo");
        }

        Actividad actividad = new Actividad();
        actividad.setId(dto.getId());
        // Parcial se establece en el servicio mediante setIdParcial()
        actividad.setTipoActividad(dto.getTipoActividad());
        actividad.setFecha(dto.getFecha());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setValorMaximo(dto.getValorMaximo());
        return actividad;
    }
}