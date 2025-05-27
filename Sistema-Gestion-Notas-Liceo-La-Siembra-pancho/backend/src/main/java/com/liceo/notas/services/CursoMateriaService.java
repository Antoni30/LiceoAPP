package com.liceo.notas.services;

import com.liceo.notas.dtos.CursoMateriaDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de asociaciones entre cursos y materias.
 * Define las operaciones básicas para crear, consultar y eliminar estas relaciones.
 */
public interface CursoMateriaService {

    /**
     * Crea una nueva asociación entre un curso y una materia.
     *
     * @param dto Objeto {@link CursoMateriaDTO} con los datos de la asociación a crear
     * @return {@link CursoMateriaDTO} con los datos registrados (incluyendo información adicional si aplica)
     */
    CursoMateriaDTO asociarCursoMateria(CursoMateriaDTO dto);

    /**
     * Obtiene todas las asociaciones de materias relacionadas con un curso específico.
     *
     * @param idCurso ID del curso para filtrar sus materias asociadas
     * @return Lista de objetos {@link CursoMateriaDTO} correspondientes al curso especificado
     */
    List<CursoMateriaDTO> listarMateriasPorCurso(Integer idCurso);

    /**
     * Obtiene todas las asociaciones de cursos relacionados con una materia específica.
     *
     * @param idMateria ID de la materia para filtrar sus cursos asociados
     * @return Lista de objetos {@link CursoMateriaDTO} correspondientes a la materia especificada
     */
    List<CursoMateriaDTO> listarCursosPorMateria(Integer idMateria);

    /**
     * Elimina una asociación existente entre un curso y una materia.
     *
     * @param idCurso   ID del curso a desasociar
     * @param idMateria ID de la materia a desasociar
     */
    void eliminarAsociacion(Integer idCurso, Integer idMateria);

    /**
     * Verifica si ya existe una asociación entre un curso y una materia específicos.
     *
     * @param idCurso   ID del curso a verificar
     * @param idMateria ID de la materia a verificar
     * @return true si la asociación existe, false en caso contrario
     */
    boolean existeAsociacion(Integer idCurso, Integer idMateria);

    /**
     * Obtiene todas las asociaciones entre cursos y materias almacenadas en el sistema.
     *
     * @return Lista de objetos {@link CursoMateriaDTO} con todas las asociaciones registradas
     */
    List<CursoMateriaDTO> listarTodasLasAsociaciones();
}