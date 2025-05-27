package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa la relación entre un usuario y un curso.
 * Mapea la tabla "USUARIO_CURSO" en la base de datos.
 * Esta entidad utiliza una clave primaria compuesta (UsuarioCursoId) formada por:
 * - ID del usuario
 * - ID del curso
 */
@Data
@Entity
@Table(name = "USUARIO_CURSO")
@IdClass(UsuarioCursoId.class) // Especifica la clase que define la clave compuesta
public class UsuarioCurso {

    /**
     * Usuario involucrado en la relación con el curso.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /**
     * Curso al que está asociado el usuario.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;
}