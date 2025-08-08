package com.liceo.notas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entidad que representa una nota académica en el sistema.
 * Mapea la tabla "NOTA" en la base de datos.
 * Una nota asocia a un usuario (estudiante) con una materia específica,
 * incluyendo la calificación obtenida y el número de parcial.
 */
@Data
@Entity
@Table(name = "NOTA")
public class Nota {

    /**
     * Identificador único de la nota.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTA")
    private Integer id;

    /**
     * Usuario al que pertenece esta nota (generalmente un estudiante).
     * Relación ManyToOne con la entidad Usuario.
     * - nullable: false → toda nota debe estar asociada a un usuario.
     */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_NOTA", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;

    /**
     * Materia a la cual pertenece esta nota.
     * Relación ManyToOne con la entidad Materia.
     * - nullable: false → toda nota debe estar asociada a una materia.
     */
    @ManyToOne
    @JoinColumn(name = "ID_MATERIA_NOTA", nullable = false)
    @NotNull(message = "La materia es obligatoria")
    private Materia materia;

    /**
     * Calificación obtenida por el estudiante.
     * Valor numérico que debe estar entre 0 y 20.
     * - nullable: false → toda nota debe tener una calificación.
     */
    @Column(name = "NOTA", nullable = false)
    @NotNull(message = "La nota es obligatoria")
    @Min(value = 0, message = "La nota no puede ser menor a 0")
    @Max(value = 20, message = "La nota no puede ser mayor a 20")
    private Double nota;

    /**
     * Número del parcial al que corresponde esta nota.
     * Valor numérico que debe estar entre 1 y 3.
     * - nullable: false → toda nota debe especificar el parcial.
     */
    @Column(name = "PARCIAL", nullable = false)
    @NotNull(message = "El parcial es obligatorio")
    @Min(value = 1, message = "El parcial debe ser mínimo 1")
    @Max(value = 3, message = "El parcial no puede ser mayor a 3")
    private Integer parcial;
}