package com.liceo.notas.controllers;

import com.liceo.notas.dtos.NotaDTO;
import com.liceo.notas.services.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de notas académicas.
 * Proporciona endpoints para registrar, consultar, actualizar y eliminar notas.
 * Todos los endpoints tienen como prefijo "/api/notas".
 */
@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaService service;

    /**
     * Registra una nueva nota en el sistema.
     *
     * @param dto Objeto NotaDTO con los datos de la nota a registrar
     * @return ResponseEntity con estado HTTP 201 (CREADO) y la nota registrada
     *         incluyendo su ID generado
     */
    @PostMapping
    public ResponseEntity<NotaDTO> registrar(@Valid @RequestBody NotaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrarNota(dto));
    }

    /**
     * Obtiene todas las notas del sistema.
     *
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de todas las notas
     */
    @GetMapping
    public ResponseEntity<List<NotaDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    /**
     * Obtiene todas las notas asociadas a un usuario específico.
     *
     * @param idUsuario Identificador único del usuario (estudiante)
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de notas
     *         del usuario solicitado
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotaDTO>> listarPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    /**
     * Obtiene todas las notas asociadas a una materia específica.
     *
     * @param idMateria ID de la materia
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de notas
     *         para la materia solicitada
     */
    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<List<NotaDTO>> listarPorMateria(@PathVariable Integer idMateria) {
        return ResponseEntity.ok(service.listarPorMateria(idMateria));
    }

    /**
     * Obtiene todas las notas de un estudiante en una materia específica.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param idMateria ID de la materia
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de notas
     *         del estudiante en la materia especificada
     */
    @GetMapping("/usuario/{idUsuario}/materia/{idMateria}")
    public ResponseEntity<List<NotaDTO>> listarPorUsuarioYMateria(
            @PathVariable String idUsuario, 
            @PathVariable Integer idMateria) {
        return ResponseEntity.ok(service.listarPorUsuarioYMateria(idUsuario, idMateria));
    }

    /**
     * Obtiene todas las notas de un estudiante en un parcial específico.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param parcial Número del parcial (1, 2 o 3)
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de notas
     *         del estudiante en el parcial especificado
     */
    @GetMapping("/usuario/{idUsuario}/parcial/{parcial}")
    public ResponseEntity<List<NotaDTO>> listarPorUsuarioYParcial(
            @PathVariable String idUsuario, 
            @PathVariable Integer parcial) {
        return ResponseEntity.ok(service.listarPorUsuarioYParcial(idUsuario, parcial));
    }

    /**
     * Obtiene todas las notas de una materia en un parcial específico.
     *
     * @param idMateria ID de la materia
     * @param parcial Número del parcial (1, 2 o 3)
     * @return ResponseEntity con estado HTTP 200 (OK) y lista de notas
     *         de la materia en el parcial especificado
     */
    @GetMapping("/materia/{idMateria}/parcial/{parcial}")
    public ResponseEntity<List<NotaDTO>> listarPorMateriaYParcial(
            @PathVariable Integer idMateria, 
            @PathVariable Integer parcial) {
        return ResponseEntity.ok(service.listarPorMateriaYParcial(idMateria, parcial));
    }

    /**
     * Obtiene la nota específica de un estudiante en una materia y parcial determinado.
     *
     * @param idUsuario ID del usuario (estudiante)
     * @param idMateria ID de la materia
     * @param parcial Número del parcial
     * @return ResponseEntity con estado HTTP 200 (OK) y la nota específica,
     *         o HTTP 404 (NOT FOUND) si no existe
     */
    @GetMapping("/usuario/{idUsuario}/materia/{idMateria}/parcial/{parcial}")
    public ResponseEntity<NotaDTO> obtenerNotaEspecifica(
            @PathVariable String idUsuario,
            @PathVariable Integer idMateria,
            @PathVariable Integer parcial) {
        NotaDTO nota = service.obtenerNotaEspecifica(idUsuario, idMateria, parcial);
        if (nota != null) {
            return ResponseEntity.ok(nota);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza una nota existente.
     *
     * @param id ID de la nota a actualizar
     * @param dto Objeto NotaDTO con los nuevos datos de la nota
     * @return ResponseEntity con estado HTTP 200 (OK) y la nota actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody NotaDTO dto) {
        return ResponseEntity.ok(service.actualizarNota(id, dto));
    }

    /**
     * Elimina una nota del sistema.
     *
     * @param id ID de la nota a eliminar
     * @return ResponseEntity con estado HTTP 204 (NO CONTENT) si se eliminó exitosamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarNota(id);
        return ResponseEntity.noContent().build();
    }
}