package com.liceo.notas.dtos;

import lombok.Data;

/**
 * DTO para representar el promedio general de un estudiante en un curso específico.
 * Contiene información sobre el promedio académico y comportamiento del estudiante.
 */
@Data
public class PromedioGeneralesEstudianteDTO {
    /**
     * Identificador único del registro de promedio general del estudiante.
     * Puede ser null si el objeto se usa para crear un nuevo registro.
     */
    private Integer id;

    /**
     * Identificador único del usuario estudiante al que pertenece este promedio.
     * Generalmente corresponde al ID o nickname del estudiante.
     */
    private String idUsuario;

    /**
     * Identificador del curso en el cual se calcula el promedio general del estudiante.
     */
    private Integer idCurso;

    /**
     * Valor numérico del promedio general del estudiante en el curso especificado.
     * Este valor representa el rendimiento académico global del estudiante.
     */
    private Double promedioGeneral;

    /**
     * Descripción del comportamiento del estudiante en el curso.
     * Puede contener términos como "Bueno", "Regular", "Excelente", etc.
     */
    private String comportamiento;
}