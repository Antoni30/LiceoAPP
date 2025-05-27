package edu.espe.publicaciones.controller;

import edu.espe.publicaciones.dto.AutorDTO;
import edu.espe.publicaciones.dto.ResponseDTO;
import edu.espe.publicaciones.services.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    @Autowired
    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> crearAutor(@RequestBody AutorDTO autorDTO) {
        ResponseDTO response = autorService.crearAutor(autorDTO);
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<ResponseDTO> obtenerTodosLosAutores() {
        ResponseDTO response = autorService.obtenerTodosLosAutores();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> actualizarAutor(@PathVariable Long id, @RequestBody AutorDTO autorDTO) {
        ResponseDTO response = autorService.actualizarAutor(id, autorDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarAutor(@PathVariable Long id) {
        ResponseDTO response = autorService.eliminarAutor(id);
        return ResponseEntity.ok(response);
    }
}
