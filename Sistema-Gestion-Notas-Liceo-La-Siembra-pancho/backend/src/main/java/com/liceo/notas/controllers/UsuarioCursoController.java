package com.liceo.notas.controllers;

import com.liceo.notas.dtos.UsuarioCursoDTO;
import com.liceo.notas.services.UsuarioCursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de asociaciones entre usuarios y cursos.
 * Permite crear, consultar y eliminar relaciones entre usuarios del sistema y cursos académicos.
 * Todos los endpoints están bajo la ruta base /api/usuarios-cursos.
 */
@RestController
@RequestMapping("/api/usuarios-cursos")
public class UsuarioCursoController {

    @Autowired
    private UsuarioCursoService service; // Servicio para la lógica de asociaciones usuario-curso

    /**
     * Crea una nueva asociación entre un usuario y un curso.
     *
     * @param dto Objeto UsuarioCursoDTO con los IDs del usuario y curso a asociar
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y la asociación creada
     * @apiNote El DTO debe incluir los IDs válidos de usuario y curso
     */
    @PostMapping
    public ResponseEntity<UsuarioCursoDTO> asociar(@RequestBody UsuarioCursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.asociarUsuarioCurso(dto));
    }

    /**
     * Obtiene todos los cursos asociados a un usuario específico.
     *
     * @param idUsuario ID del usuario (generalmente un String/UUID)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de asociaciones que incluyen al usuario
     *         - Lista vacía si el usuario no tiene cursos asociados
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<UsuarioCursoDTO>> listarCursosPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(service.listarCursosPorUsuario(idUsuario));
    }

    /**
     * Obtiene todos los usuarios asociados a un curso específico.
     *
     * @param idCurso ID del curso (Integer)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista de asociaciones que incluyen el curso
     *         - Lista vacía si el curso no tiene usuarios asociados
     */
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<UsuarioCursoDTO>> listarUsuariosPorCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(service.listarUsuariosPorCurso(idCurso));
    }

    /**
     * Elimina una asociación específica entre usuario y curso.
     *
     * @param idUsuario ID del usuario de la asociación (String/UUID)
     * @param idCurso ID del curso de la asociación (Integer)
     * @return ResponseEntity con:
     *         - Código 204 (NO CONTENT) si la eliminación fue exitosa
     * @apiNote No retorna error si la asociación no existía
     */
    @DeleteMapping("/usuario/{idUsuario}/curso/{idCurso}")
    public ResponseEntity<Void> eliminarAsociacion(
            @PathVariable String idUsuario,
            @PathVariable Integer idCurso) {
        service.eliminarAsociacion(idUsuario, idCurso);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe una asociación entre un usuario y un curso específicos.
     *
     * @param idUsuario ID del usuario a verificar (String/UUID)
     * @param idCurso ID del curso a verificar (Integer)
     * @return ResponseEntity con:
     *         - Código 200 (OK) y valor booleano:
     *           - true si existe la asociación
     *           - false si no existe
     */
    @GetMapping("/existe/usuario/{idUsuario}/curso/{idCurso}")
    public ResponseEntity<Boolean> existeAsociacion(
            @PathVariable String idUsuario,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(service.existeAsociacion(idUsuario, idCurso));
    }

    /**
     * Obtiene todas las asociaciones usuario-curso registradas en el sistema.
     *
     * @return ResponseEntity con:
     *         - Código 200 (OK) y lista completa de todas las asociaciones
     *         - Lista vacía si no hay asociaciones registradas
     */
    @GetMapping
    public ResponseEntity<List<UsuarioCursoDTO>> listarTodasLasAsociaciones() {
        return ResponseEntity.ok(service.listarTodasLasAsociaciones());
    }
}