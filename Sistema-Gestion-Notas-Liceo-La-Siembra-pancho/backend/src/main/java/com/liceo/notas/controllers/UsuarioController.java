package com.liceo.notas.controllers;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios del sistema.
 * Permite realizar operaciones CRUD sobre los usuarios registrados.
 * Todos los endpoints están bajo la ruta base /api/usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Servicio para la lógica de negocio de usuarios

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuarioDTO Objeto UsuarioDTO con los datos del usuario a crear
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y el usuario creado
     * @apiNote El DTO debe incluir los datos obligatorios del usuario (nombre, email, etc.)
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(usuarioDTO));
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID único del usuario (generalmente un UUID)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el usuario si existe
     *         - Código 404 (NOT FOUND) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable String id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista completa de usuarios
     *         - Lista vacía si no hay usuarios registrados
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Objeto UsuarioDTO con los nuevos datos
     * @return ResponseEntity con:
     *         - Código 200 (OK) y el usuario actualizado si existe
     *         - Código 404 (NOT FOUND) si el usuario no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(
            @PathVariable String id,
            @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizarUsuario(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param id ID del usuario a eliminar
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     *         - Código 404 (NOT FOUND) si el usuario no existe
     * @apiNote La eliminación puede ser lógica o física según la implementación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        return usuarioService.eliminarUsuario(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Obtiene usuarios filtrados por estado (activo/inactivo).
     *
     * @param estado Estado del usuario para filtrar (ej: "ACTIVO", "INACTIVO")
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de usuarios con el estado especificado
     *         - Lista vacía si no hay usuarios con ese estado
     */
    @GetMapping("/por-estado/{estado}")
    public ResponseEntity<List<UsuarioDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(usuarioService.listarPorEstado(estado));
    }
}