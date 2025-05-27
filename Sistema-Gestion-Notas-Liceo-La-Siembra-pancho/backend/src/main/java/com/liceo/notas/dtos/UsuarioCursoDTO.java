package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa la relación entre un Usuario y un Curso.
 * Contiene los identificadores necesarios para establecer la asociación en el sistema.
 *
 * <p>Se utiliza principalmente para operaciones de vinculación/desvinculación
 * entre usuarios y cursos académicos.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 * </ul>
 */
@Data
public class UsuarioCursoDTO {
    /**
     * Identificador único del usuario (generalmente un UUID en formato String).
     * Debe corresponder a un usuario existente en el sistema.
     */
    private String idUsuario;

    /**
     * Identificador único del curso académico.
     * Debe corresponder a un curso existente en el sistema.
     */
    private Integer idCurso;
}