package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa una calificación en el sistema.
 * Mapea la tabla "CALIFICACION" en la base de datos.
 * Una calificación se asigna a un usuario (estudiante) para una actividad específica,
 * incluyendo la nota obtenida y un comentario opcional.
 */
@Data
@Entity
@Table(name = "CALIFICACION")
public class Calificacion {

    /**
     * Identificador único de la calificación.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Integer id;

    /**
     * Usuario al que pertenece esta calificación (generalmente un estudiante).
     * Relación ManyToOne con la entidad Usuario.
     * - nullable: false → toda calificación debe estar asociada a un usuario.
     */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /**
     * Actividad a la cual pertenece esta calificación.
     * Relación ManyToOne con la entidad Actividad.
     * - nullable: false → toda calificación debe estar asociada a una actividad.
     */
    @ManyToOne
    @JoinColumn(name = "ID_ACTIVIDAD", nullable = false)
    private Actividad actividad;

    /**
     * Nota obtenida por el estudiante en la actividad.
     * Valor numérico que representa el desempeño del estudiante.
     * - nullable: false → toda calificación debe tener una nota.
     */
    @Column(name = "NOTA", nullable = false)
    private Double nota;

    /**
     * Comentario opcional sobre la calificación.
     * Puede contener observaciones del docente sobre el desempeño del estudiante.
     * - length: 200 → longitud máxima permitida del texto.
     */
    @Column(name = "COMENTARIO", length = 200)
    private String comentario;
}