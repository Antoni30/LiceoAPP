package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa la relación entre un curso y una materia.
 * Mapea la tabla "CURSO_MATERIA" en la base de datos.
 * Esta entidad utiliza una clave primaria compuesta (CursoMateriaId) formada por:
 * - ID del curso
 * - ID de la materia
 */
@Data
@Entity
@Table(name = "CURSO_MATERIA")
@IdClass(CursoMateriaId.class)  // Especifica la clase que define la clave compuesta
public class CursoMateria {

    /**
     * Curso al que se le asigna la materia.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    /**
     * Materia que se asigna al curso.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_MATERIA", nullable = false)
    private Materia materia;
}