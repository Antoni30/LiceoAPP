package com.liceo.notas.controllers;

import com.liceo.notas.dtos.RolDTO;
import com.liceo.notas.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de roles de usuario en el sistema.
 * Permite realizar operaciones CRUD sobre los roles disponibles.
 * Todos los endpoints están bajo la ruta base /api/roles.
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService service; // Servicio para la lógica de negocio de roles

    /**
     * Crea un nuevo rol en el sistema.
     *
     * @param dto Objeto RolDTO con los datos del rol a crear
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y el rol creado
     * @apiNote El DTO debe incluir al menos el nombre del rol
     */
    @PostMapping
    public ResponseEntity<RolDTO> crear(@RequestBody RolDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearRol(dto));
    }

    /**
     * Obtiene un rol por su ID.
     *
     * @param id ID del rol a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el rol si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los roles registrados en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista completa de roles
     *         - Lista vacía si no hay roles registrados
     */
    @GetMapping
    public ResponseEntity<List<RolDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Obtiene un rol por su nombre exacto.
     *
     * @param nombre Nombre exacto del rol a buscar
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el rol si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolDTO> obtenerPorNombre(@PathVariable String nombre) {
        return service.obtenerPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de un rol existente.
     *
     * @param id ID del rol a actualizar
     * @param dto Objeto RolDTO con los nuevos datos
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el rol actualizado si existe
     *         - Código 404 (NOT FOUND) si el rol no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody RolDTO dto) {
        return service.actualizarRol(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un rol del sistema.
     *
     * @param id ID del rol a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si el rol no existe
     * @apiNote La eliminación fallará si el rol está asignado a usuarios
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarRol(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}