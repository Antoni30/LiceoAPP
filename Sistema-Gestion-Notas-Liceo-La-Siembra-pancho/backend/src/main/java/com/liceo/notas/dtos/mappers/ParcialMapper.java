package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.ParcialDTO;
import com.liceo.notas.entities.Parcial;

/**
 * Clase utilitaria para mapear entre entidades Parcial y DTOs ParcialDTO.
 * Proporciona conversión bidireccional para la gestión de períodos parciales académicos.
 */
public class ParcialMapper {

    /**
     * Convierte una entidad Parcial a su representación DTO.
     *
     * @param entidad La entidad Parcial a convertir (no debe ser null)
     * @return ParcialDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @throws IllegalStateException si la entidad no tiene materia asociada
     */
    public static ParcialDTO toDTO(Parcial entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Parcial no puede ser nula");
        }
        if (entidad.getMateria() == null) {
            throw new IllegalStateException("El parcial debe tener una materia asociada");
        }

        ParcialDTO dto = new ParcialDTO();
        dto.setId(entidad.getId());
        dto.setIdMateria(entidad.getMateria().getId());
        dto.setNumeroParcial(entidad.getNumeroParcial());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFin(entidad.getFechaFin());
        return dto;
    }

    /**
     * Convierte un ParcialDTO a entidad Parcial para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Parcial con los datos mapeados
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote La materia debe establecerse posteriormente en el servicio
     */
    public static Parcial toEntity(ParcialDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO ParcialDTO no puede ser nulo");
        }

        Parcial entidad = new Parcial();
        entidad.setId(dto.getId()); // Relevante solo para actualizaciones
        // La materia se establece en el servicio mediante setMateria()
        entidad.setNumeroParcial(dto.getNumeroParcial());
        entidad.setFechaInicio(dto.getFechaInicio());
        entidad.setFechaFin(dto.getFechaFin());
        return entidad;
    }
}