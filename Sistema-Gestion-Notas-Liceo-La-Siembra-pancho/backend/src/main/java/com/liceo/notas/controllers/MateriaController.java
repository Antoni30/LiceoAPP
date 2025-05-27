package com.liceo.notas.controllers;

import com.liceo.notas.dtos.MateriaDTO;
import com.liceo.notas.services.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de materias académicas.
 * Proporciona operaciones CRUD para la entidad Materia.
 * Todos los endpoints están bajo la ruta base /api/materias.
 */
@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService; // Servicio para la lógica de negocio de materias

    /**
     * Crea una nueva materia en el sistema.
     *
     * @param dto Objeto MateriaDTO con los datos de la materia a crear
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y la materia creada en el cuerpo de la respuesta
     * @apiNote Requiere los datos completos de la materia en el cuerpo de la petición
     */
    @PostMapping
    public ResponseEntity<MateriaDTO> crear(@RequestBody MateriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materiaService.crearMateria(dto));
    }

    /**
     * Obtiene una materia por su ID.
     *
     * @param id ID de la materia a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y la materia si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> obtenerPorId(@PathVariable Integer id) {
        return materiaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las materias registradas en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de todas las materias
     *         - Lista vacía si no hay materias registradas
     */
    @GetMapping
    public ResponseEntity<List<MateriaDTO>> listarTodas() {
        return ResponseEntity.ok(materiaService.listarTodas());
    }

    /**
     * Actualiza los datos de una materia existente.
     *
     * @param id ID de la materia a actualizar
     * @param dto Objeto MateriaDTO con los nuevos datos
     * @return ResponseEntity con:
     *         - Código 200 (OK) y la materia actualizada si existe
     *         - Código 404 (NOT FOUND) si no existe la materia
     */
    @PutMapping("/{id}")
    public ResponseEntity<MateriaDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody MateriaDTO dto) {
        return materiaService.actualizarMateria(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una materia del sistema.
     *
     * @param id ID de la materia a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si la materia no existe
     *         - Código 409 (CONFLICT) si la materia no puede eliminarse
     *           (ej: tiene relaciones con otras entidades)
     * @throws RuntimeException Cuando existen restricciones de integridad referencial
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            return materiaService.eliminarMateria(id) ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("ErrorMessage", e.getMessage())
                    .build();
        }
    }
}