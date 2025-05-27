package com.liceo.notas.dtos;

import java.time.LocalDate;
import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa un año lectivo en el sistema.
 * Contiene los datos necesarios para gestionar períodos académicos anuales.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 */
@Data
public class AnioLectivoDTO {
    /**
     * Identificador único del año lectivo en el sistema.
     * Valor autogenerado cuando se crea un nuevo año lectivo.
     */
    private Integer id;

    /**
     * Fecha de inicio del año lectivo.
     * Formato: ISO-8601 (yyyy-MM-dd).
     * Debe ser una fecha válida anterior a fechaFinal.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del año lectivo.
     * Formato: ISO-8601 (yyyy-MM-dd).
     * Debe ser una fecha válida posterior a fechaInicio.
     */
    private LocalDate fechaFinal;

    /**
     * Estado actual del año lectivo.
     * Valores típicos: "PLANEADO", "EN_CURSO", "FINALIZADO", "CANCELADO".
     * Define el estado de actividad del período académico.
     */
    private String estado;
}