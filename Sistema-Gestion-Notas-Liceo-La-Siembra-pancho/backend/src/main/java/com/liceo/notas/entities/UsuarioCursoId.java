package com.liceo.notas.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa una clave primaria compuesta para la entidad {@link UsuarioCurso}.
 * Está formada por dos campos:
 * - ID del usuario (ID_USUARIO)
 * - ID del curso (ID_CURSO)
 *
 * Esta estructura es necesaria para modelar la relación muchos-a-muchos entre usuarios y cursos.
 */
public class UsuarioCursoId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador del usuario (parte de la clave compuesta).
     * Corresponde al campo ID_USUARIO en la tabla USUARIO_CURSO.
     */
    private String usuario;

    /**
     * Identificador del curso (parte de la clave compuesta).
     * Corresponde al campo ID_CURSO en la tabla USUARIO_CURSO.
     */
    private Integer curso;

    /**
     * Constructor por defecto requerido por JPA.
     * Se utiliza al crear instancias desde el framework.
     */
    public UsuarioCursoId() {
    }

    /**
     * Constructor con parámetros para crear manualmente una clave compuesta.
     *
     * @param usuario ID del usuario
     * @param curso   ID del curso
     */
    public UsuarioCursoId(String usuario, Integer curso) {
        this.usuario = usuario;
        this.curso = curso;
    }

    // Getters y Setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    /**
     * Compara esta clave compuesta con otro objeto para determinar si son iguales.
     * Dos claves son iguales si tienen el mismo usuario y el mismo curso.
     *
     * @param o Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioCursoId that = (UsuarioCursoId) o;

        return Objects.equals(usuario, that.usuario) &&
                Objects.equals(curso, that.curso);
    }

    /**
     * Genera un valor hash único para esta clave compuesta,
     * basado en los valores de usuario y curso.
     *
     * @return Valor hash calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(usuario, curso);
    }

    /**
     * Devuelve una representación en cadena del objeto.
     * Útil para depuración y logs.
     *
     * @return String que representa el objeto UsuarioCursoId
     */
    @Override
    public String toString() {
        return "UsuarioCursoId{" +
                "usuario='" + usuario + '\'' +
                ", curso=" + curso +
                '}';
    }
}