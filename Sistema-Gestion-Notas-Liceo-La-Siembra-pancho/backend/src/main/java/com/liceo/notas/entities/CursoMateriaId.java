package com.liceo.notas.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa una clave primaria compuesta para la entidad {@link CursoMateria}.
 * Está formada por dos campos:
 * - ID del curso (ID_CURSO)
 * - ID de la materia (ID_MATERIA)
 *
 * Esta clase es necesaria porque una relación entre curso y materia se identifica únicamente
 * por la combinación de estos dos IDs.
 */
public class CursoMateriaId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador del curso (parte de la clave compuesta).
     * Corresponde al campo ID_CURSO en la tabla CURSO_MATERIA.
     */
    private Integer curso;

    /**
     * Identificador de la materia (parte de la clave compuesta).
     * Corresponde al campo ID_MATERIA en la tabla CURSO_MATERIA.
     */
    private Integer materia;

    /**
     * Constructor por defecto requerido por JPA.
     * Se utiliza al crear instancias desde el framework.
     */
    public CursoMateriaId() {
    }

    /**
     * Constructor con parámetros para crear una clave compuesta manualmente.
     *
     * @param curso   ID del curso
     * @param materia ID de la materia
     */
    public CursoMateriaId(Integer curso, Integer materia) {
        this.curso = curso;
        this.materia = materia;
    }

    // Getters y Setters

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public Integer getMateria() {
        return materia;
    }

    public void setMateria(Integer materia) {
        this.materia = materia;
    }

    /**
     * Compara esta clave compuesta con otro objeto para determinar si son iguales.
     * Dos claves compuestas son iguales si tienen el mismo curso y la misma materia.
     *
     * @param o Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CursoMateriaId that = (CursoMateriaId) o;

        return Objects.equals(curso, that.curso) &&
                Objects.equals(materia, that.materia);
    }

    /**
     * Genera un valor hash único para esta clave compuesta,
     * basado en los valores de curso y materia.
     *
     * @return Valor hash calculado
     */
    @Override
    public int hashCode() {
        return Objects.hash(curso, materia);
    }

    /**
     * Devuelve una representación en cadena del objeto.
     * Útil para depuración y logs.
     *
     * @return String que representa el objeto CursoMateriaId
     */
    @Override
    public String toString() {
        return "CursoMateriaId{" +
                "curso=" + curso +
                ", materia=" + materia +
                '}';
    }
}