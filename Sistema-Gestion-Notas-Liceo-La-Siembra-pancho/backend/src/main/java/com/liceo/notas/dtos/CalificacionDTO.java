package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa una calificación académica.
 * Contiene los datos necesarios para gestionar las calificaciones de los estudiantes.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 */
@Data
public class CalificacionDTO {
    /**
     * Identificador único de la calificación en el sistema.
     * Valor autogenerado cuando se crea una nueva calificación.
     */
    private Integer id;

    /**
     * Identificador del usuario (estudiante) evaluado.
     * Debe corresponder a un usuario existente en el sistema.
     */
    private String idUsuario;

    /**
     * Identificador de la actividad evaluada.
     * Debe corresponder a una actividad existente en el sistema.
     */
    private Integer idActividad;

    /**
     * Valor numérico de la calificación obtenida.
     * Debe ser un valor entre 0 y el valor máximo definido en la actividad.
     */
    private Double nota;

    /**
     * Comentarios u observaciones adicionales sobre la calificación.
     * Puede contener retroalimentación para el estudiante.
     */
    private String comentario;
}