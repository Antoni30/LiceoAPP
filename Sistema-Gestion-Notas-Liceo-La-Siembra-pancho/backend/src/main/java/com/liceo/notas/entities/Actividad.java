package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Entidad que representa una actividad académica en el sistema.
 * Mapea la tabla ACTIVIDAD en la base de datos.
 */
@Data
@Entity
@Table(name = "ACTIVIDAD")
public class Actividad {

    /**
     * Identificador único de la actividad.
     * Se genera automáticamente mediante una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTIVIDAD")
    private Integer id;

    /**
     * Parcial al que pertenece esta actividad.
     * Relación ManyToOne con la entidad Parcial.
     * No puede ser nulo (nullable = false).
     */
    @ManyToOne
    @JoinColumn(name = "ID_PARCIAL", nullable = false)
    private Parcial parcial;

    /**
     * Tipo de actividad académica.
     * Valores típicos: "Examen", "Tarea", "Proyecto".
     * Longitud máxima: 20 caracteres.
     * No puede ser nulo.
     */
    @Column(name = "TIPO_ACTIVIDAD", length = 20, nullable = false)
    private String tipoActividad;

    /**
     * Fecha en que se realizará o entregará la actividad.
     * No puede ser nulo.
     */
    @Column(name = "FECHA_ACTIVIDAD", nullable = false)
    private LocalDate fecha;

    /**
     * Descripción detallada de la actividad.
     * Longitud máxima: 200 caracteres.
     * Puede ser nulo.
     */
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    /**
     * Valor máximo o puntaje que puede obtenerse en esta actividad.
     * Valor por defecto: 100.0
     */
    @Column(name = "VALOR_MAXIMO")
    private Double valorMaximo = 100.0;
}