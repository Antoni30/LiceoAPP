package com.liceo.notas.controllers;

import com.liceo.notas.dtos.ActividadDTO;
import com.liceo.notas.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de actividades del sistema de notas.
 * Proporciona endpoints para crear, consultar y eliminar actividades.
 * Todos los endpoints tienen como prefijo "/api/actividades".
 */
@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadService service;

    /**
     * Crea una nueva actividad en el sistema.
     *
     * @param dto Objeto ActividadDTO con los datos de la actividad a crear
     * @return ResponseEntity<ActividadDTO> con estado HTTP 201 (CREADO) y la actividad creada,
     *         incluyendo su ID generado
     */
    @PostMapping
    public ResponseEntity<ActividadDTO> crear(@RequestBody ActividadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearActividad(dto));
    }

    /**
     * Obtiene todas las actividades asociadas a un parcial específico.
     *
     * @param idParcial ID del parcial para filtrar las actividades
     * @return ResponseEntity<List<ActividadDTO>> con estado HTTP 200 (OK) conteniendo
     *         la lista de actividades para el parcial especificado
     */
    @GetMapping("/parcial/{idParcial}")
    public ResponseEntity<List<ActividadDTO>> listarPorParcial(@PathVariable Integer idParcial) {
        return ResponseEntity.ok(service.listarPorParcial(idParcial));
    }

    /**
     * Elimina una actividad existente del sistema.
     *
     * @param id ID de la actividad a eliminar
     * @return ResponseEntity<Void> con estado HTTP 204 (SIN CONTENIDO) si la eliminación fue exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }
}