package com.liceo.notas.controllers;

import com.liceo.notas.dtos.ParcialDTO;
import com.liceo.notas.services.ParcialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de períodos parciales académicos.
 * Permite realizar operaciones CRUD sobre los parciales del sistema.
 * Todos los endpoints están bajo la ruta base /api/parciales.
 */
@RestController
@RequestMapping("/api/parciales")
public class ParcialController {

    @Autowired
    private ParcialService service; // Servicio para la lógica de negocio de parciales

    /**
     * Crea un nuevo período parcial en el sistema.
     *
     * @param dto Objeto ParcialDTO con los datos del parcial a crear
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y el parcial creado
     * @apiNote El DTO debe incluir nombre, fechas y materia asociada
     */
    @PostMapping
    public ResponseEntity<ParcialDTO> crear(@RequestBody ParcialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearParcial(dto));
    }

    /**
     * Obtiene un parcial por su ID.
     *
     * @param id ID del parcial a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el parcial si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParcialDTO> obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los parciales registrados en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de todos los parciales
     *         - Lista vacía si no hay parciales registrados
     */
    @GetMapping
    public ResponseEntity<List<ParcialDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Obtiene los parciales asociados a una materia específica.
     *
     * @param idMateria ID de la materia para filtrar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de parciales de la materia
     *         - Lista vacía si la materia no tiene parciales
     */
    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<List<ParcialDTO>> listarPorMateria(@PathVariable Integer idMateria) {
        return ResponseEntity.ok(service.listarPorMateria(idMateria));
    }

    /**
     * Actualiza los datos de un parcial existente.
     *
     * @param id ID del parcial a actualizar
     * @param dto Objeto ParcialDTO con los nuevos datos
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el parcial actualizado si existe
     *         - Código 404 (NOT FOUND) si el parcial no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParcialDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody ParcialDTO dto) {
        return service.actualizarParcial(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un parcial del sistema.
     *
     * @param id ID del parcial a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si el parcial no existe
     * @apiNote La eliminación puede fallar si el parcial tiene actividades asociadas
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarParcial(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}