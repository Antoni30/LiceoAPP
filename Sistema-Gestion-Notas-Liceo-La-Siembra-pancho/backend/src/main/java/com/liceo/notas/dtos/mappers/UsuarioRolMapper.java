package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.UsuarioRolDTO;
import com.liceo.notas.entities.Rol;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.UsuarioRol;

/**
 * Clase utilitaria para mapear entre entidades UsuarioRol y DTOs UsuarioRolDTO.
 * Proporciona conversión bidireccional para la gestión de asignaciones de roles a usuarios.
 */
public class UsuarioRolMapper {

    /**
     * Convierte una entidad UsuarioRol a su representación DTO.
     *
     * @param entidad La entidad UsuarioRol a convertir (no debe ser null)
     * @return UsuarioRolDTO con los IDs mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @throws IllegalStateException si la entidad no tiene usuario o rol asociado
     */
    public static UsuarioRolDTO toDTO(UsuarioRol entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad UsuarioRol no puede ser nula");
        }
        if (entidad.getUsuario() == null || entidad.getRol() == null) {
            throw new IllegalStateException("La asignación debe tener usuario y rol asociados");
        }

        UsuarioRolDTO dto = new UsuarioRolDTO();
        dto.setIdUsuario(entidad.getUsuario().getIdUsuario());
        dto.setIdRol(entidad.getRol().getId());
        return dto;
    }

    /**
     * Convierte un UsuarioRolDTO a entidad UsuarioRol para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad UsuarioRol con las referencias básicas establecidas
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote Las entidades completas de Usuario y Rol deben cargarse posteriormente
     */
    public static UsuarioRol toEntity(UsuarioRolDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO UsuarioRolDTO no puede ser nulo");
        }

        UsuarioRol entidad = new UsuarioRol();

        // Establecemos solo los IDs (entidades proxy)
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        entidad.setUsuario(usuario);

        Rol rol = new Rol();
        rol.setId(dto.getIdRol());
        entidad.setRol(rol);

        return entidad;
    }
}