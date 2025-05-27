package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Entidad que representa una materia académica en el sistema.
 * Mapea la tabla "MATERIA" en la base de datos.
 * Una materia tiene un nombre único y puede estar asociada a múltiples cursos y parciales.
 */
@Data
@Entity
@Table(name = "MATERIA")
public class Materia {

    /**
     * Identificador único de la materia.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIA")
    private Integer id;

    /**
     * Nombre de la materia.
     * Ejemplo: "Matemáticas", "Lenguaje".
     * - nullable: false → toda materia debe tener nombre.
     * - length: 30 → longitud máxima permitida para el nombre.
     */
    @Column(name = "NOMBRE_MATERIA", nullable = false, length = 30)
    private String nombreMateria;

    /**
     * Lista de asociaciones entre esta materia y los cursos en los que se imparte.
     * Relación One-to-Many con la entidad CursoMateria (tabla intermedia).
     * - mappedBy: indica que la relación está gestionada desde la entidad CursoMateria.
     */
    @OneToMany(mappedBy = "materia")
    private List<CursoMateria> cursosAsociados;

    /**
     * Lista de parciales asociados a esta materia.
     * Relación One-to-Many con la entidad Parcial.
     * - mappedBy: indica que la relación está gestionada desde la entidad Parcial.
     */
    @OneToMany(mappedBy = "materia")
    private List<Parcial> parciales;
}