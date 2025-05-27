package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa la relación entre un Curso y una Materia.
 * Contiene los identificadores necesarios para establecer la asociación.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 * </ul>
 */
@Data
public class CursoMateriaDTO {
    /**
     * Identificador único del curso en la asociación.
     * Debe corresponder a un curso existente en el sistema.
     */
    private Integer idCurso;

    /**
     * Identificador único de la materia en la asociación.
     * Debe corresponder a una materia existente en el sistema.
     */
    private Integer idMateria;
}