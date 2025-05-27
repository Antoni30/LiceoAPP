package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa un curso académico en el sistema.
 * Contiene los datos básicos necesarios para la gestión de cursos.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 *
 * <p>La anotación @Data de Lombok genera automáticamente:
 * <ul>
 *   <li>Getters para todos los campos</li>
 *   <li>Setters para todos los campos no finales</li>
 *   <li>toString()</li>
 *   <li>equals() y hashCode()</li>
 *   <li>Constructor sin argumentos</li>
 * </ul>
 * </p>
 */
@Data
public class CursoDTO {
    /**
     * Identificador único del curso en el sistema.
     * Valor autogenerado cuando se crea un nuevo curso.
     */
    private Integer id;

    /**
     * Identificador del año lectivo al que pertenece este curso.
     * Debe corresponder a un año lectivo existente en el sistema.
     */
    private Integer idAnioLectivo;

    /**
     * Nombre del curso que identifica al grupo académico.
     * Ejemplos comunes: "1° A", "2° B", "3° C", etc.
     * Debe ser único dentro del mismo año lectivo.
     */
    private String nombreCurso;
}