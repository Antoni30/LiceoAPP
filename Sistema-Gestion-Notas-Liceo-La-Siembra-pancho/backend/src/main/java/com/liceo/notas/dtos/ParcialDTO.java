package com.liceo.notas.dtos;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que representa un período parcial académico.
 * Contiene los datos necesarios para gestionar los diferentes períodos evaluativos
 * de una materia durante el año lectivo.
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
 */
@Data
public class ParcialDTO {
    /**
     * Identificador único del parcial en el sistema.
     * Valor autogenerado cuando se crea un nuevo parcial.
     */
    private Integer id;

    /**
     * Identificador de la materia asociada al parcial.
     * Debe corresponder a una materia existente en el sistema.
     */
    private Integer idMateria;

    /**
     * Número secuencial del parcial (1, 2, 3, etc.).
     * Representa el orden del parcial dentro del año lectivo.
     * Debe ser único para cada materia.
     */
    private Integer numeroParcial;

    /**
     * Fecha de inicio del período parcial.
     * Formato: ISO-8601 (yyyy-MM-dd).
     * Debe ser anterior o igual a fechaFin.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del período parcial.
     * Formato: ISO-8601 (yyyy-MM-dd).
     * Debe ser posterior o igual a fechaInicio.
     */
    private LocalDate fechaFin;
}