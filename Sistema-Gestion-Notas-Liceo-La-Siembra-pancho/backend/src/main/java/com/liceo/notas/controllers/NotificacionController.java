package com.liceo.notas.controllers;

import com.liceo.notas.dtos.NotificacionDTO;
import com.liceo.notas.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de notificaciones del sistema.
 * Permite crear, consultar y eliminar notificaciones para usuarios.
 * Todos los endpoints están bajo la ruta base /api/notificaciones.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service; // Servicio para la lógica de negocio de notificaciones

    /**
     * Crea una nueva notificación en el sistema.
     *
     * @param dto Objeto NotificacionDTO con los datos de la notificación a crear
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y la notificación creada
     * @apiNote El DTO debe incluir el ID del usuario destinatario, mensaje y otros datos requeridos
     */
    @PostMapping
    public ResponseEntity<NotificacionDTO> crear(@RequestBody NotificacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearNotificacion(dto));
    }

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     *
     * @param idUsuario Identificador único del usuario
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de notificaciones del usuario
     *         - Lista vacía si el usuario no tiene notificaciones
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    /**
     * Obtiene todas las notificaciones de una fecha específica.
     *
     * @param fecha Fecha de las notificaciones a buscar (formato ISO: YYYY-MM-DD)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de notificaciones de la fecha
     *         - Lista vacía si no hay notificaciones en esa fecha
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<NotificacionDTO>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(service.listarPorFecha(fecha));
    }

    /**
     * Elimina una notificación del sistema.
     *
     * @param id ID de la notificación a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si la notificación no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarNotificacion(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}