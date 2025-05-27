package com.liceo.notas.entities;

import jakarta.persistence.*;  // Importa anotaciones de JPA (Jakarta Persistence API)
import lombok.Data;           // Lombok: genera getters, setters, toString

import java.time.LocalDate;
import java.util.List;
/**
 * Entidad que representa un año lectivo en el sistema.
 * Mapea la tabla "ANIO_LECTIVO" en la base de datos.
 * Un año lectivo tiene fechas de inicio y fin, un estado, y puede contener múltiples cursos asociados.
 */
@Data  // Genera automáticamente getters, setters, toString, etc.
@Entity  // Indica que esta clase es una entidad JPA (mapeada a una tabla en la base de datos)
@Table(name = "ANIO_LECTIVO")  // Nombre de la tabla en la base de datos
public class AnioLectivo {

    /**
     * Identificador único del año lectivo.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id  // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID (autoincremental)
    @Column(name = "ID_ANIOLECTIVO")  // Nombre de la columna en la base de datos
    private Integer id;

    /**
     * Fecha de inicio del año lectivo.
     * No puede ser null.
     */
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del año lectivo.
     * No puede ser null.
     */
    @Column(name = "FECHA_FINAL", nullable = false)
    private LocalDate fechaFinal;

    /**
     * Estado actual del año lectivo.
     * Ejemplos: "Activo", "Inactivo".
     * No puede ser null.
     */
    @Column(name = "ESTADO_LECTIVO", nullable = false)
    private String estado;

    /**
     * Lista de cursos asociados a este año lectivo.
     * Relación uno a muchos con la entidad Curso.
     * - mappedBy: atributo en la clase Curso que referencia a este AnioLectivo.
     * - fetch: carga perezosa (lazy), para mejorar rendimiento hasta que sea necesario acceder a los cursos.
     */
    @OneToMany(mappedBy = "anioLectivo", fetch = FetchType.LAZY)
    private List<Curso> cursos;
}