package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa una materia académica en el sistema.
 * Contiene la información básica necesaria para la gestión de materias.
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
public class MateriaDTO {
    /**
     * Identificador único de la materia en el sistema.
     * Valor autogenerado cuando se crea una nueva materia.
     */
    private Integer id;

    /**
     * Nombre de la materia académica.
     * Ejemplos: "Matemáticas", "Lengua y Literatura", "Ciencias Naturales".
     * Debe ser único en el sistema.
     */
    private String nombreMateria;
}