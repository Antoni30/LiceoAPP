package com.liceo.notas.dtos;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que representa una actividad académica.
 * Contiene los datos necesarios para gestionar actividades en el sistema.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 */
@Data
public class ActividadDTO {
    /**
     * Identificador único de la actividad en el sistema.
     * Valor autogenerado cuando se crea una nueva actividad.
     */
    private Integer id;

    /**
     * Identificador del parcial al que pertenece esta actividad.
     * Debe corresponder a un parcial existente en el sistema.
     */
    private Integer idParcial;

    /**
     * Tipo de actividad (ej: "Examen", "Tarea", "Proyecto", etc.).
     * Define la categoría de la actividad académica.
     */
    private String tipoActividad;

    /**
     * Fecha en la que se realizará o entregará la actividad.
     * Formato: ISO-8601 (yyyy-MM-dd).
     */
    private LocalDate fecha;

    /**
     * Descripción detallada de la actividad.
     * Incluye instrucciones, requisitos u observaciones importantes.
     */
    private String descripcion;

    /**
     * Valor máximo o puntaje que puede obtenerse en esta actividad.
     * Representa el 100% de la calificación posible.
     */
    private Double valorMaximo;
}