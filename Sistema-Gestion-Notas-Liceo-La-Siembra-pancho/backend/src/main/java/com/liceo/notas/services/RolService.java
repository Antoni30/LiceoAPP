package com.liceo.notas.services;

import com.liceo.notas.dtos.RolDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de roles del sistema.
 * Define las operaciones básicas para crear, consultar, actualizar, eliminar y buscar roles por nombre.
 */
public interface RolService {

    /**
     * Crea un nuevo rol con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link RolDTO} con los datos del rol a crear
     * @return {@link RolDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    RolDTO crearRol(RolDTO dto);

    /**
     * Obtiene los datos de un rol por su identificador único.
     *
     * @param id ID del rol a obtener
     * @return Optional conteniendo el {@link RolDTO} si se encuentra, o vacío si no existe
     */
    Optional<RolDTO> obtenerPorId(Integer id);

    /**
     * Obtiene una lista con todos los roles registrados en el sistema.
     *
     * @return Lista de objetos {@link RolDTO} con todos los roles disponibles
     */
    List<RolDTO> listarTodos();

    /**
     * Actualiza los datos de un rol existente identificado por su ID.
     *
     * @param id  ID del rol a actualizar
     * @param dto Objeto {@link RolDTO} con los nuevos datos del rol
     * @return Optional conteniendo el {@link RolDTO} actualizado si la operación fue exitosa, o vacío si no se encontró el rol
     */
    Optional<RolDTO> actualizarRol(Integer id, RolDTO dto);

    /**
     * Elimina un rol del sistema por su ID.
     *
     * @param id ID del rol a eliminar
     * @return true si el rol fue eliminado correctamente, false si no se encontró
     */
    boolean eliminarRol(Integer id);

    /**
     * Busca un rol por su nombre (por ejemplo: "Docente", "Estudiante").
     *
     * @param nombre Nombre del rol a buscar
     * @return Optional conteniendo el {@link RolDTO} si se encuentra, o vacío si no existe
     */
    Optional<RolDTO> obtenerPorNombre(String nombre);
}