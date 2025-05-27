package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa el promedio general de un estudiante en un curso específico.
 * Mapea la tabla "PROMEDIOGENERALESTUDIANTE" en la base de datos.
 * Incluye el promedio académico y una descripción del comportamiento del estudiante.
 */
@Entity
@Table(name = "PROMEDIOGENERALESTUDIANTE")
@Data
public class PromedioGeneralesEstudiante {

    /**
     * Identificador único del registro de promedio general del estudiante.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROMEDIO_GENERAL")
    private Integer id;

    /**
     * Identificador del usuario estudiante al que pertenece este promedio.
     * Generalmente corresponde al ID o nickname del estudiante.
     * - nullable: false → todo promedio debe estar asociado a un estudiante.
     * - length: 10 → longitud máxima permitida para el identificador.
     */
    @Column(name = "ID_USUARIO", nullable = false, length = 10)
    private String idUsuario;

    /**
     * Curso en el cual se calcula el promedio general del estudiante.
     * Relación ManyToOne con la entidad Curso.
     * - nullable: false → todo promedio debe estar asociado a un curso.
     */
    @ManyToOne
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    /**
     * Valor numérico del promedio general del estudiante en el curso especificado.
     * Este valor representa el rendimiento académico global del estudiante.
     * - nullable: false → todo promedio debe tener un valor asignado.
     */
    @Column(name = "PROMEDIO_GENERAL", nullable = false)
    private Double promedioGeneral;

    /**
     * Descripción del comportamiento del estudiante en el curso.
     * Puede contener valores como "B" (Bueno), "R" (Regular), "E" (Excelente), etc.
     * - nullable: false → todo registro debe incluir una descripción de comportamiento.
     * - length: 1 → se espera un solo carácter como código de comportamiento.
     */
    @Column(name = "COMPORTAMIENTO", nullable = false, length = 1)
    private String comportamiento;
}