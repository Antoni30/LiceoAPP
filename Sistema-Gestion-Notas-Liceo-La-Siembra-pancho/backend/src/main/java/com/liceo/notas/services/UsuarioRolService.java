package com.liceo.notas.services;

import com.liceo.notas.dtos.UsuarioRolDTO;
import java.util.List;

/**
 * Interfaz de servicio para gestionar las asignaciones entre usuarios y roles.
 * Define operaciones básicas para crear, consultar y eliminar estas relaciones.
 */
public interface UsuarioRolService {

    /**
     * Asigna un rol a un usuario utilizando los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link UsuarioRolDTO} con los datos de la asignación a crear
     * @return {@link UsuarioRolDTO} con los datos registrados, incluyendo información adicional si aplica
     */
    UsuarioRolDTO asignarRolAUsuario(UsuarioRolDTO dto);

    /**
     * Obtiene todas las asignaciones de roles asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario para filtrar sus roles asignados
     * @return Lista de objetos {@link UsuarioRolDTO} correspondientes al usuario especificado
     */
    List<UsuarioRolDTO> listarRolesPorUsuario(String idUsuario);

    /**
     * Obtiene todas las asignaciones de usuarios asociadas a un rol específico.
     *
     * @param idRol ID del rol para filtrar sus usuarios asignados
     * @return Lista de objetos {@link UsuarioRolDTO} correspondientes al rol especificado
     */
    List<UsuarioRolDTO> listarUsuariosPorRol(Integer idRol);

    /**
     * Elimina una asignación existente entre un usuario y un rol.
     *
     * @param idUsuario ID del usuario a desasociar
     * @param idRol     ID del rol a desasociar
     */
    void eliminarAsignacion(String idUsuario, Integer idRol);

    /**
     * Verifica si ya existe una asignación entre un usuario y un rol específicos.
     *
     * @param idUsuario ID del usuario a verificar
     * @param idRol     ID del rol a verificar
     * @return true si la asignación existe, false en caso contrario
     */
    boolean existeAsignacion(String idUsuario, Integer idRol);

    /**
     * Obtiene todas las asignaciones entre usuarios y roles almacenadas en el sistema.
     *
     * @return Lista de objetos {@link UsuarioRolDTO} con todas las asignaciones registradas
     */
    List<UsuarioRolDTO> listarTodasLasAsignaciones();
}