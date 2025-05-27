package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa la relación entre un usuario y un rol.
 * Mapea la tabla "USUARIO_ROL" en la base de datos.
 * Esta entidad utiliza una clave primaria compuesta (UsuarioRolId) formada por:
 * - ID del usuario
 * - ID del rol
 */
@Data
@Entity
@Table(name = "USUARIO_ROL")
@IdClass(UsuarioRolId.class) // Especifica la clase que define la clave compuesta
public class UsuarioRol {

    /**
     * Usuario involucrado en la relación con el rol.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /**
     * Rol asignado al usuario.
     * Forma parte de la clave primaria compuesta y no puede ser null.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_ROL", nullable = false)
    private Rol rol;
}