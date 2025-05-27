package com.liceo.notas.services;

import com.liceo.notas.dtos.UsuarioCursoDTO;
import java.util.List;

/**
 * Interfaz de servicio para gestionar las asociaciones entre usuarios y cursos.
 * Define las operaciones básicas para crear, consultar y eliminar estas relaciones.
 */
public interface UsuarioCursoService {

    /**
     * Crea una nueva asociación entre un usuario y un curso.
     *
     * @param dto Objeto {@link UsuarioCursoDTO} con los datos de la asociación a crear
     * @return {@link UsuarioCursoDTO} con los datos registrados, incluyendo información adicional si aplica
     */
    UsuarioCursoDTO asociarUsuarioCurso(UsuarioCursoDTO dto);

    /**
     * Obtiene todas las asociaciones de cursos relacionadas con un usuario específico.
     *
     * @param idUsuario ID del usuario para filtrar sus cursos asociados
     * @return Lista de objetos {@link UsuarioCursoDTO} correspondientes al usuario especificado
     */
    List<UsuarioCursoDTO> listarCursosPorUsuario(String idUsuario);

    /**
     * Obtiene todas las asociaciones de usuarios relacionadas con un curso específico.
     *
     * @param idCurso ID del curso para filtrar sus usuarios asociados
     * @return Lista de objetos {@link UsuarioCursoDTO} correspondientes al curso especificado
     */
    List<UsuarioCursoDTO> listarUsuariosPorCurso(Integer idCurso);

    /**
     * Elimina una asociación existente entre un usuario y un curso.
     *
     * @param idUsuario ID del usuario a desasociar
     * @param idCurso   ID del curso a desasociar
     */
    void eliminarAsociacion(String idUsuario, Integer idCurso);

    /**
     * Verifica si ya existe una asociación entre un usuario y un curso específicos.
     *
     * @param idUsuario ID del usuario a verificar
     * @param idCurso   ID del curso a verificar
     * @return true si la asociación existe, false en caso contrario
     */
    boolean existeAsociacion(String idUsuario, Integer idCurso);

    /**
     * Obtiene todas las asociaciones entre usuarios y cursos almacenadas en el sistema.
     *
     * @return Lista de objetos {@link UsuarioCursoDTO} con todas las asociaciones registradas
     */
    List<UsuarioCursoDTO> listarTodasLasAsociaciones();
}