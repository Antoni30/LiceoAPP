package com.liceo.notas.services;

import com.liceo.notas.dtos.NotaDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de notas académicas.
 * Define las operaciones básicas para registrar, listar y actualizar notas de estudiantes.
 */
public interface NotaService {

    /**
     * Registra una nueva nota en el sistema.
     *
     * @param dto Objeto {@link NotaDTO} con los datos de la nota a registrar
     * @return {@link NotaDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    NotaDTO registrarNota(NotaDTO dto);

    /**
     * Obtiene todas las notas asociadas a un usuario (estudiante) específico.
     *
     * @param idUsuario ID del estudiante para filtrar sus notas
     * @return Lista de objetos {@link NotaDTO} correspondientes al estudiante especificado
     */
    List<NotaDTO> listarPorUsuario(String idUsuario);

    /**
     * Obtiene todas las notas asociadas a una materia específica.
     *
     * @param idMateria ID de la materia para filtrar las notas
     * @return Lista de objetos {@link NotaDTO} correspondientes a la materia especificada
     */
    List<NotaDTO> listarPorMateria(Integer idMateria);

    /**
     * Obtiene todas las notas de un estudiante en una materia específica.
     *
     * @param idUsuario ID del estudiante
     * @param idMateria ID de la materia
     * @return Lista de objetos {@link NotaDTO} del estudiante en la materia especificada
     */
    List<NotaDTO> listarPorUsuarioYMateria(String idUsuario, Integer idMateria);

    /**
     * Obtiene todas las notas de un estudiante en un parcial específico.
     *
     * @param idUsuario ID del estudiante
     * @param parcial Número del parcial (1, 2 o 3)
     * @return Lista de objetos {@link NotaDTO} del estudiante en el parcial especificado
     */
    List<NotaDTO> listarPorUsuarioYParcial(String idUsuario, Integer parcial);

    /**
     * Obtiene todas las notas de una materia en un parcial específico.
     *
     * @param idMateria ID de la materia
     * @param parcial Número del parcial (1, 2 o 3)
     * @return Lista de objetos {@link NotaDTO} de la materia en el parcial especificado
     */
    List<NotaDTO> listarPorMateriaYParcial(Integer idMateria, Integer parcial);

    /**
     * Obtiene la nota específica de un estudiante en una materia y parcial determinado.
     *
     * @param idUsuario ID del estudiante
     * @param idMateria ID de la materia
     * @param parcial Número del parcial
     * @return {@link NotaDTO} con la nota específica, o null si no existe
     */
    NotaDTO obtenerNotaEspecifica(String idUsuario, Integer idMateria, Integer parcial);

    /**
     * Actualiza una nota existente por su ID.
     *
     * @param id ID de la nota a actualizar
     * @param dto Objeto {@link NotaDTO} con los nuevos datos de la nota
     * @return {@link NotaDTO} con los datos actualizados
     */
    NotaDTO actualizarNota(Integer id, NotaDTO dto);

    /**
     * Elimina una nota del sistema por su ID.
     *
     * @param id ID de la nota a eliminar
     */
    void eliminarNota(Integer id);

    /**
     * Obtiene todas las notas del sistema.
     *
     * @return Lista de objetos {@link NotaDTO} con todas las notas
     */
    List<NotaDTO> listarTodas();
}