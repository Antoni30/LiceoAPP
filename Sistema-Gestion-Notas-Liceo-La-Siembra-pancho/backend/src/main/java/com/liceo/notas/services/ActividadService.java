package com.liceo.notas.services;

import com.liceo.notas.dtos.ActividadDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de actividades académicas.
 * Define las operaciones básicas para crear, consultar y eliminar actividades.
 */
public interface ActividadService {

    /**
     * Crea una nueva actividad con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link ActividadDTO} con los datos de la actividad a crear
     * @return {@link ActividadDTO} con los datos de la actividad creada, incluyendo su ID generado
     */
    ActividadDTO crearActividad(ActividadDTO dto);

    /**
     * Obtiene una lista de todas las actividades asociadas a un parcial específico.
     *
     * @param idParcial ID del parcial para filtrar las actividades
     * @return Lista de objetos {@link ActividadDTO} correspondientes al parcial especificado
     */
    List<ActividadDTO> listarPorParcial(Integer idParcial);

    /**
     * Obtiene los datos de una actividad por su ID.
     *
     * @param id ID de la actividad a obtener
     * @return {@link ActividadDTO} con los datos de la actividad encontrada
     */
    ActividadDTO obtenerPorId(Integer id);

    /**
     * Elimina una actividad existente del sistema.
     *
     * @param id ID de la actividad a eliminar
     */
    void eliminarActividad(Integer id);
}