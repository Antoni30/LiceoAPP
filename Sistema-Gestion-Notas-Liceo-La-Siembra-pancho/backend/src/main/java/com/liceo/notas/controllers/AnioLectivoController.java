package com.liceo.notas.controllers;

import com.liceo.notas.dtos.AnioLectivoDTO;
import com.liceo.notas.services.AnioLectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de años lectivos.
 * Expone endpoints para operaciones CRUD de años académicos.
 *
 * Ruta base: /api/anios-lectivos
 */
@RestController
@RequestMapping("/api/anios-lectivos")
public class AnioLectivoController {

    @Autowired
    private AnioLectivoService service;  // Inyección de la interfaz del servicio

    /**
     * Obtiene la lista de años lectivos activos.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de años activos si existen
     *         - Código 204 (Sin contenido) si no hay años activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<AnioLectivoDTO>> obtenerAniosActivos() {
        List<AnioLectivoDTO> activos = service.obtenerAniosActivos();
        return activos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(activos);
    }

    /**
     * Crea un nuevo año lectivo.
     *
     * @param dto Datos del año lectivo a crear
     * @return ResponseEntity con código 201 (Creado) y el año lectivo creado
     */
    @PostMapping
    public ResponseEntity<AnioLectivoDTO> crear(@RequestBody AnioLectivoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearAnioLectivo(dto));
    }

    /**
     * Obtiene un año lectivo por su ID.
     *
     * @param id ID del año lectivo a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el año lectivo si existe
     *         - Código 404 (No encontrado) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnioLectivoDTO> obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los años lectivos registrados.
     *
     * @return ResponseEntity con código 200 (OK) y lista completa de años lectivos
     */
    @GetMapping
    public ResponseEntity<List<AnioLectivoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Actualiza un año lectivo existente.
     *
     * @param id ID del año lectivo a actualizar
     * @param dto Nuevos datos para el año lectivo
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el año actualizado si existe
     *         - Código 404 (No encontrado) si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnioLectivoDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody AnioLectivoDTO dto) {
        return service.actualizarAnioLectivo(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/estado/{id}")
    public ResponseEntity<AnioLectivoDTO> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody AnioLectivoDTO dto) {
        return service.actualizarAnioLectivoEstado(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un año lectivo por su ID.
     *
     * @param id ID del año lectivo a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (Sin contenido) si la eliminación fue exitosa
     *         - Código 404 (No encontrado) si el año no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarAnioLectivo(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}