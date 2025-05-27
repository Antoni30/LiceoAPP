package com.liceo.notas.services;

import com.liceo.notas.dtos.AnioLectivoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de años lectivos.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar años lectivos.
 */
public interface AnioLectivoService {

    /**
     * Crea un nuevo año lectivo con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link AnioLectivoDTO} con los datos del año lectivo a crear
     * @return {@link AnioLectivoDTO} con los datos del año creado, incluyendo su ID generado
     */
    AnioLectivoDTO crearAnioLectivo(AnioLectivoDTO dto);

    /**
     * Obtiene los datos de un año lectivo por su ID.
     *
     * @param id ID del año lectivo a obtener
     * @return Optional conteniendo el {@link AnioLectivoDTO} si se encuentra, o vacío si no existe
     */
    Optional<AnioLectivoDTO> obtenerPorId(Integer id);

    /**
     * Obtiene una lista de todos los años lectivos registrados en el sistema.
     *
     * @return Lista de objetos {@link AnioLectivoDTO} con todos los años lectivos
     */
    List<AnioLectivoDTO> listarTodos();

    /**
     * Obtiene una lista de todos los años lectivos que están activos.
     *
     * @return Lista de objetos {@link AnioLectivoDTO} con los años lectivos cuyo estado es "Activo"
     */
    List<AnioLectivoDTO> obtenerAniosActivos();

    /**
     * Actualiza los datos de un año lectivo existente identificado por su ID.
     *
     * @param id  ID del año lectivo a actualizar
     * @param dto Objeto {@link AnioLectivoDTO} con los nuevos datos del año lectivo
     * @return Optional conteniendo el {@link AnioLectivoDTO} actualizado si la operación fue exitosa, o vacío si no se encontró el año
     */
    Optional<AnioLectivoDTO> actualizarAnioLectivo(Integer id, AnioLectivoDTO dto);

    /**
     * Elimina un año lectivo existente del sistema.
     *
     * @param id ID del año lectivo a eliminar
     * @return true si el año lectivo fue eliminado correctamente, false si no se encontró
     */
    boolean eliminarAnioLectivo(Integer id);
}