package edu.espe.publicaciones.controller;

import edu.espe.publicaciones.dto.LibroDTO;
import edu.espe.publicaciones.dto.ResponseDTO;
import edu.espe.publicaciones.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // Crear un nuevo libro
    @PostMapping
    public ResponseEntity<ResponseDTO> crearLibro(@RequestBody LibroDTO libroDTO) {
        ResponseDTO response = libroService.crearLibro(libroDTO);
        return ResponseEntity.ok(response);
    }

    // Obtener todos los libros
    @GetMapping
    public ResponseEntity<ResponseDTO> obtenerTodosLosLibros() {
        ResponseDTO response = libroService.obtenerTodosLosLibros();
        return ResponseEntity.ok(response);
    }

    // Actualizar un libro por ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> actualizarLibro(@PathVariable Integer id, @RequestBody LibroDTO libroDTO) {
        ResponseDTO response = libroService.actualizarLibro(id, libroDTO);
        return ResponseEntity.ok(response);
    }

    // Eliminar un libro por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarLibro(@PathVariable Integer id) {
        ResponseDTO response = libroService.eliminarLibro(id);
        return ResponseEntity.ok(response);
    }
}
