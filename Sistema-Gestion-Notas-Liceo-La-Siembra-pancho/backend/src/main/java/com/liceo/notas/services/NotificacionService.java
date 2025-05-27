package com.liceo.notas.services;

import com.liceo.notas.dtos.NotificacionDTO;
import com.liceo.notas.entities.Notificacion;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de notificaciones del sistema.
 * Define las operaciones básicas para crear, consultar y eliminar notificaciones dirigidas a usuarios.
 */
public interface NotificacionService {

    /**
     * Crea una nueva notificación con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link NotificacionDTO} con los datos de la notificación a crear
     * @return {@link NotificacionDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    NotificacionDTO crearNotificacion(NotificacionDTO dto);

    /**
     * Obtiene todas las notificaciones asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario (estudiante/docente) para filtrar sus notificaciones
     * @return Lista de objetos {@link NotificacionDTO} correspondientes al usuario especificado
     */
    List<NotificacionDTO> listarPorUsuario(String idUsuario);

    /**
     * Obtiene todas las notificaciones generadas en una fecha específica.
     *
     * @param fecha Fecha de generación de las notificaciones a buscar
     * @return Lista de objetos {@link NotificacionDTO} con las notificaciones de la fecha especificada
     */
    List<NotificacionDTO> listarPorFecha(LocalDate fecha);

    /**
     * Elimina una notificación existente del sistema por su ID.
     *
     * @param id ID de la notificación a eliminar
     * @return true si la notificación fue eliminada correctamente, false si no se encontró
     */
    boolean eliminarNotificacion(Integer id);
}