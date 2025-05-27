package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Entidad que representa un parcial académico dentro de una materia.
 * Mapea la tabla "PARCIAL" en la base de datos.
 * Un parcial tiene un número identificador, fechas de inicio y fin,
 * y está asociado a una materia específica.
 */
@Data
@Entity
@Table(name = "PARCIAL")
public class Parcial {

    /**
     * Identificador único del parcial.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARCIAL")
    private Integer id;

    /**
     * Materia a la cual pertenece este parcial.
     * Relación ManyToOne con la entidad Materia.
     * - nullable: false → todo parcial debe estar asociado a una materia.
     */
    @ManyToOne
    @JoinColumn(name = "ID_MATERIA", nullable = false)
    private Materia materia;

    /**
     * Número del parcial dentro de la materia.
     * Ejemplo: 1 (primer parcial), 2 (segundo parcial), etc.
     * - nullable: false → todo parcial debe tener un número asignado.
     */
    @Column(name = "NUMERO_PARCIAL", nullable = false)
    private Integer numeroParcial;

    /**
     * Fecha de inicio del periodo correspondiente al parcial.
     * - nullable: false → toda fecha de inicio debe estar definida.
     */
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del periodo correspondiente al parcial.
     * - nullable: false → toda fecha de fin debe estar definida.
     */
    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;

    /**
     * Lista de actividades asociadas a este parcial.
     * Relación One-to-Many con la entidad Actividad.
     * - mappedBy: indica que la relación está gestionada desde la entidad Actividad.
     */
    /*@OneToMany(mappedBy = "parcial")
    private List<Actividad> actividades;*/
}