package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.RolDTO;
import com.liceo.notas.entities.Rol;

/**
 * Clase utilitaria para mapear entre entidades Rol y DTOs RolDTO.
 * Proporciona conversión bidireccional para la gestión de roles del sistema.
 */
public class RolMapper {

    /**
     * Convierte una entidad Rol a su representación DTO.
     *
     * @param entidad La entidad Rol a convertir (no debe ser null)
     * @return RolDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad recibida es null
     */
    public static RolDTO toDTO(Rol entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Rol no puede ser nula");
        }

        RolDTO dto = new RolDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        return dto;
    }

    /**
     * Convierte un RolDTO a entidad Rol para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Rol con los datos mapeados
     * @throws IllegalArgumentException si el DTO recibido es null
     * @apiNote El campo ID solo se usa para operaciones de actualización
     */
    public static Rol toEntity(RolDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO RolDTO no puede ser nulo");
        }

        Rol entidad = new Rol();
        entidad.setId(dto.getId()); // Relevante solo para actualizaciones
        entidad.setNombre(dto.getNombre());
        return entidad;
    }
}