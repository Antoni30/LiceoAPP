package com.liceo.notas.repositories;

import com.liceo.notas.entities.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Nota}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a las notas de los estudiantes.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface NotaRepository extends JpaRepository<Nota, Integer> {

    /**
     * Busca todas las notas asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario (estudiante) para filtrar las notas
     * @return Lista de notas pertenecientes al estudiante especificado
     */
    List<Nota> findByUsuarioIdUsuario(String idUsuario);

    /**
     * Busca todas las notas asociadas a una materia específica.
     *
     * @param idMateria ID de la materia para filtrar las notas
     * @return Lista de notas asociadas a la materia especificada
     */
    List<Nota> findByMateriaId(Integer idMateria);

    /**
     * Busca todas las notas de un estudiante en una materia específica.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param idMateria ID de la materia para filtrar las notas
     * @return Lista de notas del estudiante en la materia especificada
     */
    List<Nota> findByUsuarioIdUsuarioAndMateriaId(String idUsuario, Integer idMateria);

    /**
     * Busca todas las notas de un estudiante en un parcial específico.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param parcial Número del parcial (1, 2 o 3)
     * @return Lista de notas del estudiante en el parcial especificado
     */
    List<Nota> findByUsuarioIdUsuarioAndParcial(String idUsuario, Integer parcial);

    /**
     * Busca todas las notas de una materia en un parcial específico.
     *
     * @param idMateria ID de la materia
     * @param parcial Número del parcial (1, 2 o 3)
     * @return Lista de notas de la materia en el parcial especificado
     */
    List<Nota> findByMateriaIdAndParcial(Integer idMateria, Integer parcial);

    /**
     * Busca la nota específica de un estudiante en una materia y parcial determinado.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param idMateria ID de la materia
     * @param parcial Número del parcial
     * @return La nota específica, si existe
     */
    Nota findByUsuarioIdUsuarioAndMateriaIdAndParcial(String idUsuario, Integer idMateria, Integer parcial);
}