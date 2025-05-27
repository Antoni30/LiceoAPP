package com.liceo.notas.controllers;

import com.liceo.notas.dtos.CursoDTO;
import com.liceo.notas.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de cursos académicos.
 * Proporciona operaciones CRUD para la entidad Curso.
 * Todos los endpoints están bajo la ruta base /api/cursos.
 */
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService service; // Servicio para la lógica de negocio de cursos

    /**
     * Crea un nuevo curso en el sistema.
     *
     * @param dto Datos del curso a crear (en formato DTO)
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y el curso creado en el cuerpo
     */
    @PostMapping
    public ResponseEntity<CursoDTO> crear(@RequestBody CursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearCurso(dto));
    }

    /**
     * Obtiene un curso por su ID.
     *
     * @param id ID del curso a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el curso si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los cursos registrados.
     *
     * @return ResponseEntity con código 200 (OK) y lista de cursos
     *         (la lista puede estar vacía si no hay cursos)
     */
    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Obtiene los cursos asociados a un año lectivo específico.
     *
     * @param idAnioLectivo ID del año lectivo
     * @return ResponseEntity con código 200 (OK) y lista de cursos
     *         del año lectivo especificado
     */
    @GetMapping("/por-anio/{idAnioLectivo}")
    public ResponseEntity<List<CursoDTO>> listarPorAnioLectivo(@PathVariable Integer idAnioLectivo) {
        return ResponseEntity.ok(service.listarPorAnioLectivo(idAnioLectivo));
    }

    /**
     * Obtiene los cursos de años lectivos activos.
     *
     * @return ResponseEntity con código 200 (OK) y lista de cursos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<CursoDTO>> listarCursosActivos() {
        return ResponseEntity.ok(service.listarCursosActivos());
    }

    /**
     * Actualiza los datos de un curso existente.
     *
     * @param id ID del curso a actualizar
     * @param dto Datos actualizados del curso
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el curso actualizado si existe
     *         - Código 404 (NOT FOUND) si el curso no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody CursoDTO dto) {
        return service.actualizarCurso(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un curso del sistema.
     *
     * @param id ID del curso a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si el curso no existía
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarCurso(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}