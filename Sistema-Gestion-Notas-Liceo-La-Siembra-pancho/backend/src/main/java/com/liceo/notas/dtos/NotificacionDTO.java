package com.liceo.notas.dtos;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que representa una notificación en el sistema.
 * Contiene la información necesaria para gestionar las notificaciones de usuarios.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 *   <li>Constructor sin argumentos</li>
 * </ul>
 */
@Data
public class NotificacionDTO {
    /**
     * Identificador único de la notificación en el sistema.
     * Valor autogenerado cuando se crea una nueva notificación.
     */
    private Integer id;

    /**
     * Identificador del usuario destinatario de la notificación.
     * Debe corresponder a un usuario existente en el sistema.
     * Tipo String para coincidir con el esquema USUARIO(ID_USUARIO VARCHAR).
     */
    private String idUsuario;

    /**
     * Contenido del mensaje de notificación.
     * Debe contener la información relevante para el usuario.
     * Ejemplo: "Tienes una nueva calificación en Matemáticas".
     */
    private String mensajeNotificacion;

    /**
     * Fecha en que se generó la notificación.
     * Formato: ISO-8601 (yyyy-MM-dd).
     * Se establece automáticamente al momento de creación.
     */
    private LocalDate fechaNotificacion;
}