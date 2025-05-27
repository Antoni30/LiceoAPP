package com.liceo.notas.controllers;

import com.liceo.notas.dtos.PromedioGeneralesEstudianteDTO;
import com.liceo.notas.services.PromedioGeneralesEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de promedios generales de estudiantes.
 * Proporciona endpoints para crear, actualizar, eliminar, obtener y listar promedios generales de estudiantes.
 * Todos los endpoints tienen como prefijo "/api/promedios-estudiantes".
 */
@RestController
@RequestMapping("/api/promedios-estudiantes")
public class PromedioGeneralesEstudiante {

    @Autowired
    private PromedioGeneralesEstudianteService service;

    /**
     * Crea un nuevo registro de promedio general para un estudiante.
     *
     * @param dto Objeto PromedioGeneralesEstudianteDTO con los datos del promedio a crear
     * @return ResponseEntity<PromedioGeneralesEstudianteDTO> con estado HTTP 201 (CREADO) y el promedio creado,
     *         incluyendo su ID generado
     */
    @PostMapping
    public ResponseEntity<PromedioGeneralesEstudianteDTO> crear(@RequestBody PromedioGeneralesEstudianteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardarPromedio(dto));
    }

    /**
     * Actualiza un registro existente de promedio general por su ID.
     *
     * @param id  ID del promedio a actualizar
     * @param dto Objeto PromedioGeneralesEstudianteDTO con los nuevos datos
     * @return ResponseEntity<PromedioGeneralesEstudianteDTO> con estado HTTP 200 (OK) y el promedio actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromedioGeneralesEstudianteDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody PromedioGeneralesEstudianteDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.actualizarPromedio(dto));
    }

    /**
     * Elimina un registro de promedio general por su ID.
     *
     * @param id ID del promedio a eliminar
     * @return ResponseEntity<Void> con estado HTTP 204 (SIN CONTENIDO) si la eliminación fue exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarPromedio(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene un registro de promedio general por su ID.
     *
     * @param id ID del promedio a obtener
     * @return ResponseEntity<PromedioGeneralesEstudianteDTO> con estado HTTP 200 (OK) y el promedio encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromedioGeneralesEstudianteDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /**
     * Obtiene todos los registros de promedios generales de estudiantes.
     *
     * @return ResponseEntity<List<PromedioGeneralesEstudianteDTO>> con estado HTTP 200 (OK) conteniendo
     *         la lista completa de promedios generales
     */
    @GetMapping
    public ResponseEntity<List<PromedioGeneralesEstudianteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Obtiene el promedio general de un estudiante específico dentro de un curso determinado.
     *
     * @param idUsuario ID del usuario estudiante
     * @param idCurso   ID del curso en el cual se desea obtener el promedio
     * @return ResponseEntity<PromedioGeneralesEstudianteDTO> con estado HTTP 200 (OK) conteniendo
     *         el promedio general del estudiante en ese curso
     */
    @GetMapping("/estudiante/{idUsuario}/curso/{idCurso}")
    public ResponseEntity<PromedioGeneralesEstudianteDTO> obtenerPorEstudianteYCurso(
            @PathVariable String idUsuario,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(service.obtenerPorEstudianteYCurso(idUsuario, idCurso));
    }
}