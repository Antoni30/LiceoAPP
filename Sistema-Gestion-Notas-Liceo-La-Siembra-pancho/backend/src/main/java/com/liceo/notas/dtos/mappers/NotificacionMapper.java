package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.NotificacionDTO;
import com.liceo.notas.entities.Notificacion;

/**
 * Clase utilitaria para mapear entre entidades Notificacion y DTOs NotificacionDTO.
 * Proporciona conversión bidireccional para la gestión de notificaciones del sistema.
 */
public class NotificacionMapper {

    /**
     * Convierte una entidad Notificacion a su representación DTO.
     *
     * @param entidad La entidad Notificacion a convertir (no debe ser null)
     * @return NotificacionDTO con los datos mapeados
     * @throws IllegalArgumentException si la entidad es null
     * @throws IllegalStateException si la notificación no tiene usuario asociado
     */
    public static NotificacionDTO toDTO(Notificacion entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad Notificacion no puede ser nula");
        }
        if (entidad.getUsuario() == null) {
            throw new IllegalStateException("La notificación debe tener un usuario asociado");
        }

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entidad.getId());
        dto.setIdUsuario(entidad.getUsuario().getIdUsuario());
        dto.setMensajeNotificacion(entidad.getMensaje());
        dto.setFechaNotificacion(entidad.getFechaNotificacion());
        return dto;
    }

    /**
     * Convierte un NotificacionDTO a entidad Notificacion para persistencia.
     *
     * @param dto El DTO a convertir (no debe ser null)
     * @return Entidad Notificacion con los datos mapeados
     * @throws IllegalArgumentException si el DTO es null
     * @apiNote El usuario debe establecerse posteriormente en el servicio
     */
    public static Notificacion toEntity(NotificacionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO NotificacionDTO no puede ser nulo");
        }

        Notificacion entidad = new Notificacion();
        entidad.setId(dto.getId());
        // El usuario se establece en el servicio mediante setUsuario()
        entidad.setMensaje(dto.getMensajeNotificacion());
        entidad.setFechaNotificacion(dto.getFechaNotificacion());
        return entidad;
    }
}