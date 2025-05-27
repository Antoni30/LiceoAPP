package com.liceo.notas.controllers;

import com.liceo.notas.dtos.CalificacionDTO;
import com.liceo.notas.services.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de calificaciones académicas.
 * Proporciona endpoints para registrar, consultar y actualizar calificaciones.
 * Todos los endpoints tienen como prefijo "/api/calificaciones".
 */
@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService service; // Servicio inyectado para la lógica de negocio de calificaciones

    /**
     * Registra una nueva calificación en el sistema.
     *
     * @param dto Objeto CalificacionDTO con los datos de la calificación a registrar
     * @return ResponseEntity con estado HTTP 201 (CREADO) y la calificación registrada
     *         incluyendo su ID generado
     */
    @PostMapping
    public ResponseEntity<CalificacionDTO> registrar(@RequestBody CalificacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrarCalificacion(dto));
    }

    /**
     * Obtiene todas las calificaciones asociadas a un usuario específico.
     *
     * @param idUsuario Identificador único del usuario (estudiante)
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de calificaciones
     *         del usuario solicitado
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CalificacionDTO>> listarPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    /**
     * Obtiene todas las calificaciones asociadas a una actividad específica.
     *
     * @param idActividad ID de la actividad académica
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de calificaciones
     *         para la actividad solicitada
     */
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<List<CalificacionDTO>> listarPorActividad(@PathVariable Integer idActividad) {
        return ResponseEntity.ok(service.listarPorActividad(idActividad));
    }

    /**
     * Actualiza una calificación existente.
     *
     * @param id ID de la calificación a actualizar
     * @param dto Objeto CalificacionDTO con los nuevos datos de la calificación
     * @return ResponseEntity con estado HTTP 200 (OK) y la calificación actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody CalificacionDTO dto) {
        return ResponseEntity.ok(service.actualizarCalificacion(id, dto));
    }
}