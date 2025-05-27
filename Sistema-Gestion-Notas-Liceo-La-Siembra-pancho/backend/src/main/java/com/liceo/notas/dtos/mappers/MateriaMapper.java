package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.MateriaDTO;
import com.liceo.notas.entities.Materia;

/**
 * Clase utilitaria para mapear entre entidades Materia y DTOs MateriaDTO.
 * Proporciona conversión bidireccional para la gestión de materias académicas.
 */
public class MateriaMapper {

    /**
     * Convierte una entidad Materia a su representación DTO.
     *
     * @param entidad La entidad Materia a convertir (no debe ser null)
     * @return MateriaDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad recibida es null
     */
    public static MateriaDTO toDTO(Materia entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Materia no puede ser nula");
        }

        MateriaDTO dto = new MateriaDTO();
        dto.setId(entidad.getId());
        dto.setNombreMateria(entidad.getNombreMateria());
        return dto;
    }

    /**
     * Convierte un MateriaDTO a entidad Materia para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Materia con los datos mapeados
     * @throws IllegalArgumentException si el DTO recibido es null
     * @apiNote El campo ID solo se usa para operaciones de actualización
     */
    public static Materia toEntity(MateriaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO MateriaDTO no puede ser nulo");
        }

        Materia entidad = new Materia();
        entidad.setId(dto.getId()); // Relevante solo para actualizaciones
        entidad.setNombreMateria(dto.getNombreMateria());
        return entidad;
    }
}