package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa una nota académica.
 * Contiene los datos necesarios para gestionar las notas de los estudiantes.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 */
@Data
public class NotaDTO {
    /**
     * Identificador único de la nota en el sistema.
     * Valor autogenerado cuando se crea una nueva nota.
     */
    private Integer id;

    /**
     * Identificador del usuario (estudiante) evaluado.
     * Debe corresponder a un usuario existente en el sistema.
     */
    private String idUsuario;

    /**
     * Identificador de la materia evaluada.
     * Debe corresponder a una materia existente en el sistema.
     */
    private Integer idMateria;

    /**
     * Valor numérico de la calificación obtenida.
     * Debe ser un valor entre 0 y 20.
     */
    private Double nota;

    /**
     * Número del parcial al que corresponde esta nota.
     * Debe ser un valor entre 1 y 3.
     */
    private Integer parcial;
}