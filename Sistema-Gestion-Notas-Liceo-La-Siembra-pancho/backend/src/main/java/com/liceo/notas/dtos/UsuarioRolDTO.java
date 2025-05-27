package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa la asignación de un rol a un usuario.
 * Contiene los identificadores necesarios para establecer la relación entre usuarios y roles.
 *
 * <p>Se utiliza principalmente para operaciones de asignación/desasignación
 * de roles a usuarios en el sistema.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 * </ul>
 */
@Data
public class UsuarioRolDTO {
    /**
     * Identificador único del usuario (generalmente un UUID en formato String).
     * Debe corresponder a un usuario existente en el sistema.
     * Formato: String para compatibilidad con diversos sistemas de identificación.
     */
    private String idUsuario;

    /**
     * Identificador único del rol a asignar.
     * Debe corresponder a un rol existente en el sistema.
     * Valores típicos: 1 (ADMIN), 2 (PROFESOR), 3 (ESTUDIANTE), etc.
     */
    private Integer idRol;
}