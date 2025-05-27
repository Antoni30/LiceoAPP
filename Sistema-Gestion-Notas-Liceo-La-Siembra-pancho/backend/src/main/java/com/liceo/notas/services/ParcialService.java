package com.liceo.notas.services;

import com.liceo.notas.dtos.ParcialDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de parciales académicos.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar parciales del sistema.
 */
public interface ParcialService {

    /**
     * Crea un nuevo parcial con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link ParcialDTO} con los datos del parcial a crear
     * @return {@link ParcialDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    ParcialDTO crearParcial(ParcialDTO dto);

    /**
     * Obtiene los datos de un parcial por su identificador único.
     *
     * @param id ID del parcial a obtener
     * @return Optional conteniendo el {@link ParcialDTO} si se encuentra, o vacío si no existe
     */
    Optional<ParcialDTO> obtenerPorId(Integer id);

    /**
     * Obtiene una lista con todos los parciales registrados en el sistema.
     *
     * @return Lista de objetos {@link ParcialDTO} con todos los parciales disponibles
     */
    List<ParcialDTO> listarTodos();

    /**
     * Obtiene una lista de parciales asociados a una materia específica.
     *
     * @param idMateria ID de la materia para filtrar los parciales
     * @return Lista de objetos {@link ParcialDTO} correspondientes a la materia especificada
     */
    List<ParcialDTO> listarPorMateria(Integer idMateria);

    /**
     * Actualiza los datos de un parcial existente identificado por su ID.
     *
     * @param id  ID del parcial a actualizar
     * @param dto Objeto {@link ParcialDTO} con los nuevos datos del parcial
     * @return Optional conteniendo el {@link ParcialDTO} actualizado si la operación fue exitosa, o vacío si no se encontró el parcial
     */
    Optional<ParcialDTO> actualizarParcial(Integer id, ParcialDTO dto);

    /**
     * Elimina un parcial del sistema por su ID.
     *
     * @param id ID del parcial a eliminar
     * @return true si el parcial fue eliminado correctamente, false si no se encontró
     */
    boolean eliminarParcial(Integer id);
}