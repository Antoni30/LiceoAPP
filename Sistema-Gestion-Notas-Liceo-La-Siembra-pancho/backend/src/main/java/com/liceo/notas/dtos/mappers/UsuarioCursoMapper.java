package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.UsuarioCursoDTO;
import com.liceo.notas.entities.Curso;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.UsuarioCurso;

/**
 * Clase utilitaria para mapear entre entidades UsuarioCurso y DTOs UsuarioCursoDTO.
 * Proporciona conversión bidireccional para la gestión de relaciones usuario-curso.
 */
public class UsuarioCursoMapper {

    /**
     * Convierte una entidad UsuarioCurso a su representación DTO.
     *
     * @param entidad La entidad UsuarioCurso a convertir (no debe ser null)
     * @return UsuarioCursoDTO con los IDs mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @throws IllegalStateException si la entidad no tiene usuario o curso asociado
     */
    public static UsuarioCursoDTO toDTO(UsuarioCurso entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad UsuarioCurso no puede ser nula");
        }
        if (entidad.getUsuario() == null || entidad.getCurso() == null) {
            throw new IllegalStateException("La relación debe tener usuario y curso asociados");
        }

        UsuarioCursoDTO dto = new UsuarioCursoDTO();
        dto.setIdUsuario(entidad.getUsuario().getIdUsuario());
        dto.setIdCurso(entidad.getCurso().getId());
        return dto;
    }

    /**
     * Convierte un UsuarioCursoDTO a entidad UsuarioCurso para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad UsuarioCurso con las referencias básicas establecidas
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote Las entidades completas de Usuario y Curso deben cargarse posteriormente
     */
    public static UsuarioCurso toEntity(UsuarioCursoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO UsuarioCursoDTO no puede ser nulo");
        }

        UsuarioCurso entidad = new UsuarioCurso();

        // Establecemos solo los IDs (entidades proxy)
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        entidad.setUsuario(usuario);

        Curso curso = new Curso();
        curso.setId(dto.getIdCurso());
        entidad.setCurso(curso);

        return entidad;
    }
}