package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entidad que representa un curso académico en el sistema.
 * Mapea la tabla "CURSO" en la base de datos.
 * Un curso pertenece a un año lectivo específico y puede estar asociado a varias materias.
 */
@Data // Genera automáticamente getters, setters, equals, hashCode, etc.
@Entity // Indica que esta clase es una entidad JPA (mapeada a una tabla en BD)
@Table(name = "CURSO") // Nombre de la tabla en la base de datos
public class Curso {

    /**
     * Identificador único del curso.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID (autoincremental)
    @Column(name = "ID_CURSO") // Nombre de la columna en la tabla
    private Integer id;

    /**
     * Año lectivo al cual pertenece este curso.
     * Relación ManyToOne con la entidad AnioLectivo:
     * - varios cursos pueden pertenecer al mismo año lectivo.
     * - nullable: false → todo curso debe tener un año lectivo asignado.
     */
    @ManyToOne // Muchos cursos pueden pertenecer a un año lectivo
    @JoinColumn(name = "ID_ANIOLECTIVO", nullable = false) // Columna FK y no nula
    private AnioLectivo anioLectivo;

    /**
     * Nombre del curso.
     * Ejemplo: "1° Bachillerato", "2° Secundaria".
     * - nullable: false → todo curso debe tener nombre.
     * - length: 15 → longitud máxima permitida para el nombre.
     */
    @Column(name = "NOMBRE_CURSO", nullable = false, length = 15)
    private String nombreCurso;

    /**
     * Lista de asociaciones entre este curso y las materias que imparte.
     * Relación One-to-Many con la entidad CursoMateria (tabla intermedia).
     * - mappedBy: indica que la relación está gestionada desde la entidad CursoMateria.
     */
    @OneToMany(mappedBy = "curso")
    private List<CursoMateria> materiasAsociadas;
}