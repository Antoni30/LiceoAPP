package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.CursoMateriaDTO;
import com.liceo.notas.entities.Curso;
import com.liceo.notas.entities.CursoMateria;
import com.liceo.notas.entities.Materia;

/**
 * Clase utilitaria para mapear entre entidades CursoMateria y DTOs CursoMateriaDTO.
 * Proporciona conversión bidireccional para la gestión de relaciones entre cursos y materias.
 */
public class CursoMateriaMapper {

    /**
     * Convierte una entidad CursoMateria a su representación DTO.
     *
     * @param entidad La entidad CursoMateria a convertir (no debe ser null)
     * @return CursoMateriaDTO con los IDs mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @throws IllegalStateException si la entidad no tiene curso o materia asociada
     */
    public static CursoMateriaDTO toDTO(CursoMateria entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad CursoMateria no puede ser nula");
        }
        if (entidad.getCurso() == null || entidad.getMateria() == null) {
            throw new IllegalStateException("La relación debe tener curso y materia asociados");
        }

        CursoMateriaDTO dto = new CursoMateriaDTO();
        dto.setIdCurso(entidad.getCurso().getId());
        dto.setIdMateria(entidad.getMateria().getId());
        return dto;
    }

    /**
     * Convierte un CursoMateriaDTO a entidad CursoMateria.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad CursoMateria con las referencias básicas establecidas
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote Las entidades completas de Curso y Materia deben cargarse posteriormente
     */
    public static CursoMateria toEntity(CursoMateriaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO CursoMateriaDTO no puede ser nulo");
        }

        CursoMateria entidad = new CursoMateria();

        // Establecemos solo los IDs (entidades proxy)
        Curso curso = new Curso();
        curso.setId(dto.getIdCurso());
        entidad.setCurso(curso);

        Materia materia = new Materia();
        materia.setId(dto.getIdMateria());
        entidad.setMateria(materia);

        return entidad;
    }
}