package com.liceo.notas.controllers;

import com.liceo.notas.dtos.UsuarioRolDTO;
import com.liceo.notas.services.UsuarioRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de asignaciones de roles a usuarios.
 * Permite asignar, consultar y eliminar relaciones entre usuarios y roles del sistema.
 * Todos los endpoints están bajo la ruta base /api/usuarios-roles.
 */
@RestController
@RequestMapping("/api/usuarios-roles")
public class UsuarioRolController {

    @Autowired
    private UsuarioRolService service; // Servicio para la lógica de asignación de roles

    /**
     * Asigna un rol a un usuario en el sistema.
     *
     * @param dto Objeto UsuarioRolDTO con los IDs del usuario y rol a asignar
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y la asignación creada
     * @apiNote El DTO debe incluir IDs válidos de usuario (String) y rol (Integer)
     * @throws IllegalArgumentException Si los IDs no existen o la asignación ya existe
     */
    @PostMapping
    public ResponseEntity<UsuarioRolDTO> asignarRol(@RequestBody UsuarioRolDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.asignarRolAUsuario(dto));
    }

    /**
     * Obtiene todos los roles asignados a un usuario específico.
     *
     * @param idUsuario ID del usuario (String/UUID)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de asignaciones del usuario
     *         - Lista vacía si el usuario no tiene roles asignados
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<UsuarioRolDTO>> listarRolesPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.listarRolesPorUsuario(idUsuario));
    }

    /**
     * Obtiene todos los usuarios que tienen asignado un rol específico.
     *
     * @param idRol ID del rol (Integer)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de asignaciones del rol
     *         - Lista vacía si el rol no tiene usuarios asignados
     */
    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<UsuarioRolDTO>> listarUsuariosPorRol(@PathVariable Integer idRol) {
        return ResponseEntity.ok(service.listarUsuariosPorRol(idRol));
    }

    /**
     * Elimina la asignación de un rol a un usuario.
     *
     * @param idUsuario ID del usuario (String/UUID)
     * @param idRol ID del rol (Integer)
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     * @apiNote No genera error si la asignación no existía (operación idempotente)
     */
    @DeleteMapping("/usuario/{idUsuario}/rol/{idRol}")
    public ResponseEntity<Void> eliminarAsignacion(
            @PathVariable String idUsuario,
            @PathVariable Integer idRol) {
        service.eliminarAsignacion(idUsuario, idRol);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe una asignación específica entre usuario y rol.
     *
     * @param idUsuario ID del usuario (String/UUID)
     * @param idRol ID del rol (Integer)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y valor booleano:
     *           - true si existe la asignación
     *           - false si no existe
     */
    @GetMapping("/existe/usuario/{idUsuario}/rol/{idRol}")
    public ResponseEntity<Boolean> existeAsignacion(
            @PathVariable String idUsuario,
            @PathVariable Integer idRol) {
        return ResponseEntity.ok(service.existeAsignacion(idUsuario, idRol));
    }

    /**
     * Obtiene todas las asignaciones usuario-rol registradas en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista completa de asignaciones
     *         - Lista vacía si no hay asignaciones registradas
     */
    @GetMapping
    public ResponseEntity<List<UsuarioRolDTO>> listarTodasLasAsignaciones() {
        return ResponseEntity.ok(service.listarTodasLasAsignaciones());
    }
}