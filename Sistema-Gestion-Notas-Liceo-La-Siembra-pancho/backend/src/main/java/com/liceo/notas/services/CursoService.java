package com.liceo.notas.services;

import com.liceo.notas.dtos.CursoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de cursos académicos.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar cursos.
 */
public interface CursoService {

    /**
     * Crea un nuevo curso con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link CursoDTO} con los datos del curso a crear
     * @return {@link CursoDTO} con los datos del curso creado, incluyendo su ID generado
     */
    CursoDTO crearCurso(CursoDTO dto);

    /**
     * Obtiene los datos de un curso por su ID.
     *
     * @param id ID del curso a obtener
     * @return Optional conteniendo el {@link CursoDTO} si se encuentra, o vacío si no existe
     */
    Optional<CursoDTO> obtenerPorId(Integer id);

    /**
     * Obtiene una lista de todos los cursos registrados en el sistema.
     *
     * @return Lista de objetos {@link CursoDTO} con todos los cursos
     */
    List<CursoDTO> listarTodos();

    /**
     * Obtiene una lista de cursos asociados a un año lectivo específico.
     *
     * @param idAnioLectivo ID del año lectivo para filtrar los cursos
     * @return Lista de objetos {@link CursoDTO} correspondientes al año lectivo especificado
     */
    List<CursoDTO> listarPorAnioLectivo(Integer idAnioLectivo);

    /**
     * Obtiene una lista de cursos cuyo estado es "Activo".
     *
     * @return Lista de objetos {@link CursoDTO} con los cursos activos
     */
    List<CursoDTO> listarCursosActivos();

    /**
     * Actualiza los datos de un curso existente identificado por su ID.
     *
     * @param id  ID del curso a actualizar
     * @param dto Objeto {@link CursoDTO} con los nuevos datos del curso
     * @return Optional conteniendo el {@link CursoDTO} actualizado si la operación fue exitosa, o vacío si no se encontró el curso
     */
    Optional<CursoDTO> actualizarCurso(Integer id, CursoDTO dto);

    /**
     * Elimina un curso existente del sistema.
     *
     * @param id ID del curso a eliminar
     * @return true si el curso fue eliminado correctamente, false si no se encontró
     */
    boolean eliminarCurso(Integer id);
}