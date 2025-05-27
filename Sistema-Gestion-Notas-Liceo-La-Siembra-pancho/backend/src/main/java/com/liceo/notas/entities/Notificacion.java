package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Entidad que representa una notificación dirigida a un usuario del sistema.
 * Mapea la tabla "NOTIFICACIONES" en la base de datos.
 * Una notificación contiene un mensaje, la fecha en que se genera y está asociada a un usuario específico.
 */
@Data
@Entity
@Table(name = "NOTIFICACIONES")
public class Notificacion {

    /**
     * Identificador único de la notificación.
     * Es la clave primaria de la tabla y se genera automáticamente como autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Integer id;

    /**
     * Usuario al que está dirigida esta notificación.
     * Relación ManyToOne con la entidad Usuario.
     * - nullable: false → toda notificación debe estar asociada a un usuario.
     */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /**
     * Mensaje contenido en la notificación.
     * Ejemplo: "Tienes una nueva calificación disponible".
     * - nullable: false → toda notificación debe tener un mensaje.
     * - length: 100 → longitud máxima permitida del texto.
     */
    @Column(name = "MENSAJE_NOTIFICACION", nullable = false, length = 100)
    private String mensaje;

    /**
     * Fecha en la cual se generó la notificación.
     * - nullable: false → toda notificación debe tener una fecha asignada.
     */
    @Column(name = "FECHA_NOTIFICACION", nullable = false)
    private LocalDate fechaNotificacion;
}