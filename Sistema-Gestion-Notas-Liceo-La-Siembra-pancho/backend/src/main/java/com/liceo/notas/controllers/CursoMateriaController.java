package com.liceo.notas.controllers;

import com.liceo.notas.dtos.CursoMateriaDTO;
import com.liceo.notas.services.CursoMateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de asociaciones entre cursos y materias.
 * Permite crear, consultar y eliminar relaciones entre cursos académicos y materias.
 * Todos los endpoints están bajo la ruta base /api/cursos-materias.
 */
@RestController
@RequestMapping("/api/cursos-materias")
public class CursoMateriaController {

    @Autowired
    private CursoMateriaService service; // Servicio para la lógica de asociaciones curso-materia

    /**
     * Crea una nueva asociación entre un curso y una materia.
     *
     * @param dto Objeto CursoMateriaDTO con los IDs del curso y materia a asociar
     * @return ResponseEntity con:
     *         - Código 201 (CREATED) y la asociación creada
     */
    @PostMapping
    public ResponseEntity<CursoMateriaDTO> asociar(@RequestBody CursoMateriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.asociarCursoMateria(dto));
    }

    /**
     * Obtiene todas las materias asociadas a un curso específico.
     *
     * @param idCurso ID del curso a consultar
     * @return ResponseEntity con código 200 (OK) y lista de asociaciones
     *         que incluyen el curso especificado
     */
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<CursoMateriaDTO>> listarMateriasPorCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(service.listarMateriasPorCurso(idCurso));
    }

    /**
     * Obtiene todos los cursos asociados a una materia específica.
     *
     * @param idMateria ID de la materia a consultar
     * @return ResponseEntity con código 200 (OK) y lista de asociaciones
     *         que incluyen la materia especificada
     */
    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<List<CursoMateriaDTO>> listarCursosPorMateria(@PathVariable Integer idMateria) {
        return ResponseEntity.ok(service.listarCursosPorMateria(idMateria));
    }

    /**
     * Elimina una asociación específica entre curso y materia.
     *
     * @param idCurso ID del curso de la asociación
     * @param idMateria ID de la materia de la asociación
     * @return ResponseEntity con código 204 (NO CONTENT) si la eliminación fue exitosa
     */
    @DeleteMapping("/curso/{idCurso}/materia/{idMateria}")
    public ResponseEntity<Void> eliminarAsociacion(
            @PathVariable Integer idCurso,
            @PathVariable Integer idMateria) {
        service.eliminarAsociacion(idCurso, idMateria);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe una asociación entre un curso y una materia específicos.
     *
     * @param idCurso ID del curso a verificar
     * @param idMateria ID de la materia a verificar
     * @return ResponseEntity con código 200 (OK) y valor booleano:
     *         - true si existe la asociación
     *         - false si no existe
     */
    @GetMapping("/existe/curso/{idCurso}/materia/{idMateria}")
    public ResponseEntity<Boolean> existeAsociacion(
            @PathVariable Integer idCurso,
            @PathVariable Integer idMateria) {
        return ResponseEntity.ok(service.existeAsociacion(idCurso, idMateria));
    }

    /**
     * Obtiene todas las asociaciones curso-materia registradas en el sistema.
     *
     * @return ResponseEntity con código 200 (OK) y lista completa
     *         de todas las asociaciones existentes
     */
    @GetMapping
    public ResponseEntity<List<CursoMateriaDTO>> listarTodasLasAsociaciones() {
        return ResponseEntity.ok(service.listarTodasLasAsociaciones());
    }
}