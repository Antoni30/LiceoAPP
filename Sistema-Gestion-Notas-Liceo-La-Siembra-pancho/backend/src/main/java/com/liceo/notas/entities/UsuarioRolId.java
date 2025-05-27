package com.liceo.notas.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa una clave primaria compuesta para la entidad {@link UsuarioRol}.
 * Está formada por dos campos:
 * - ID del usuario (ID_USUARIO)
 * - ID del rol (ID_ROL)
 *
 * Esta estructura es necesaria para modelar la relación muchos-a-muchos entre usuarios y roles.
 */
public class UsuarioRolId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador del usuario (parte de la clave compuesta).
     * Corresponde al campo ID_USUARIO en la tabla USUARIO_ROL.
     */
    private String usuario;

    /**
     * Identificador del rol (parte de la clave compuesta).
     * Corresponde al campo ID_ROL en la tabla USUARIO_ROL.
     */
    private Integer rol;

    /**
     * Constructor por defecto requerido por JPA.
     * Se utiliza al crear instancias desde el framework.
     */
    public UsuarioRolId() {
    }

    /**
     * Constructor con parámetros para crear manualmente una clave compuesta.
     *
     * @param usuario ID del usuario
     * @param rol     ID del rol
     */
    public UsuarioRolId(String usuario, Integer rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    // Getters y Setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    /**
     * Compara esta clave compuesta con otro objeto para determinar si son iguales.
     * Dos claves son iguales si tienen el mismo usuario y el mismo rol.
     *
     * @param o Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioRolId that = (UsuarioRolId) o;

        return Objects.equals(usuario, that.usuario) &&
                Objects.equals(rol, that.rol);
    }

    /**
     * Genera un valor hash único para esta clave compuesta,
     * basado en los valores de usuario y rol.
     *
     * @return Valor hash calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(usuario, rol);
    }

    /**
     * Devuelve una representación en cadena del objeto.
     * Útil para depuración y logs.
     *
     * @return String que representa el objeto UsuarioRolId
     */
    @Override
    public String toString() {
        return "UsuarioRolId{" +
                "usuario='" + usuario + '\'' +
                ", rol=" + rol +
                '}';
    }
}