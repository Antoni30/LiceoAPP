package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.entities.Usuario;

/**
 * Clase utilitaria para mapear entre entidades Usuario y DTOs UsuarioDTO.
 * Proporciona conversión bidireccional para la gestión de usuarios del sistema.
 */
public class UsuarioMapper {

    /**
     * Convierte una entidad Usuario a su representación DTO.
     *
     * @param entidad La entidad Usuario a convertir (no debe ser null)
     * @return UsuarioDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @apiNote La contraseña se incluye en el DTO (usar con precaución)
     */
    public static UsuarioDTO toDTO(Usuario entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Usuario no puede ser nula");
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(entidad.getIdUsuario());
        dto.setNombres(entidad.getNombres());
        dto.setApellidos(entidad.getApellidos());
        dto.setNickname(entidad.getNickname());
        dto.setContrasena(entidad.getContrasena()); // Precaución: solo para uso interno
        dto.setEstado(entidad.getEstado());
        dto.setMfaHabilitado(entidad.getMfaHabilitado());
        dto.setEmail(entidad.getEmail());
        dto.setEmailVerificado(entidad.isEmailVerificado());



        // Los roles se manejan en el servicio correspondiente
        return dto;
    }

    /**
     * Convierte un UsuarioDTO a entidad Usuario para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Usuario con los datos mapeados
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote La contraseña debe encriptarse antes de persistir
     * @apiNote Los roles deben establecerse en el servicio
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO UsuarioDTO no puede ser nulo");
        }

        Usuario entidad = new Usuario();
        entidad.setIdUsuario(dto.getIdUsuario());
        entidad.setNombres(dto.getNombres());
        entidad.setApellidos(dto.getApellidos());
        entidad.setNickname(dto.getNickname());
        entidad.setContrasena(dto.getContrasena()); // Debe encriptarse antes de guardar
        entidad.setEstado(dto.getEstado());
        entidad.setMfaHabilitado(dto.isMfaHabilitado());
        entidad.setEmail(dto.getEmail());
        entidad.setMfaSecret(dto.getMfaSecret());
        entidad.setEmailVerificado(dto.isEmailVerificado());

        // Los roles se manejan en el servicio correspondiente
        return entidad;
    }

    /**
     * Método comentado para futura implementación:
     * Convierte entidad Usuario a DTO de respuesta sin datos sensibles.
     *
     * @param entidad La entidad Usuario a convertir
     * @return UsuarioResponseDTO sin contraseña y con nombres de roles
     */
    /*
    public static UsuarioResponseDTO toResponseDTO(Usuario entidad) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setIdUsuario(entidad.getIdUsuario());
        response.setNombres(entidad.getNombres());
        response.setApellidos(entidad.getApellidos());
        response.setNickname(entidad.getNickname());
        response.setEstado(entidad.getEstado());

        if(entidad.getRoles() != null) {
            List<String> rolesNombres = entidad.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toList());
            response.setRoles(rolesNombres);
        }

        return response;
    }
    */
}