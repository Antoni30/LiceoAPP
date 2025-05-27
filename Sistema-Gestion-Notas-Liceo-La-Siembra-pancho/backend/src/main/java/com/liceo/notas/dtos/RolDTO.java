package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa un rol en el sistema.
 * Contiene la información básica necesaria para la gestión de roles de usuarios.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 *   <li>Constructor sin argumentos</li>
 * </ul>
 */
@Data
public class RolDTO {
    /**
     * Identificador único del rol en el sistema.
     * Valor autogenerado cuando se crea un nuevo rol.
     */
    private Integer id;

    /**
     * Nombre del rol que identifica su función en el sistema.
     * Ejemplos: "ADMINISTRADOR", "PROFESOR", "ESTUDIANTE".
     * Debe ser único en el sistema.
     */
    private String nombre;
}