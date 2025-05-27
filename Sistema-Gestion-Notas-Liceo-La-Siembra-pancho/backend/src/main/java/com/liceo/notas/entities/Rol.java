package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entidad que representa un rol dentro del sistema (ej: Administrador, Docente, Estudiante).
 * Mapea la tabla "ROL" en la base de datos.
 * Un rol puede estar asociado a múltiples usuarios a través de la entidad UsuarioRol.
 */
@Data
@Entity
@Table(name = "ROL")
public class Rol {

    /**
     * Identificador único del rol.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer id;

    /**
     * Nombre del rol asignado en el sistema.
     * Ejemplos: "Administrador", "Docente", "Estudiante".
     * - nullable: false → todo rol debe tener nombre.
     * - length: 20 → longitud máxima permitida para el nombre del rol.
     */
    @Column(name = "NOMBRE_ROL", nullable = false, length = 20)
    private String nombre;

    /**
     * Lista de asociaciones entre este rol y los usuarios que lo poseen.
     * Relación One-to-Many con la entidad UsuarioRol (tabla intermedia).
     * - mappedBy: indica que la relación está gestionada desde la entidad UsuarioRol.
     */
    @OneToMany(mappedBy = "rol")
    private List<UsuarioRol> usuarioRoles;
}