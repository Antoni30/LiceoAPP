package com.liceo.notas.services;

import com.liceo.notas.dtos.CalificacionDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de calificaciones.
 * Define las operaciones básicas para registrar, listar y actualizar calificaciones de estudiantes.
 */
public interface CalificacionService {

    /**
     * Registra una nueva calificación en el sistema.
     *
     * @param dto Objeto {@link CalificacionDTO} con los datos de la calificación a registrar
     * @return {@link CalificacionDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    CalificacionDTO registrarCalificacion(CalificacionDTO dto);

    /**
     * Obtiene todas las calificaciones asociadas a un usuario (estudiante) específico.
     *
     * @param idUsuario ID del estudiante para filtrar sus calificaciones
     * @return Lista de objetos {@link CalificacionDTO} correspondientes al estudiante especificado
     */
    List<CalificacionDTO> listarPorUsuario(String idUsuario);

    /**
     * Obtiene todas las calificaciones asociadas a una actividad específica.
     *
     * @param idActividad ID de la actividad para filtrar las calificaciones
     * @return Lista de objetos {@link CalificacionDTO} correspondientes a la actividad especificada
     */
    List<CalificacionDTO> listarPorActividad(Integer idActividad);

    /**
     * Actualiza una calificación existente por su ID.
     *
     * @param id  ID de la calificación a actualizar
     * @param dto Objeto {@link CalificacionDTO} con los nuevos datos de la calificación
     * @return {@link CalificacionDTO} con los datos actualizados
     */
    CalificacionDTO actualizarCalificacion(Integer id, CalificacionDTO dto);
}