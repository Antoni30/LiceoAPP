package edu.espe.publicaciones.services;

import edu.espe.publicaciones.dto.LibroDTO;
import edu.espe.publicaciones.dto.ResponseDTO;
import edu.espe.publicaciones.entity.Autor;
import edu.espe.publicaciones.entity.Libro;
import edu.espe.publicaciones.respository.AutorRepository;
import edu.espe.publicaciones.respository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public ResponseDTO crearLibro(LibroDTO dto) {
        if (dto == null) {
            return new ResponseDTO("Error: Libro no puede ser nulo", null);
        }

        Libro libro = new Libro();
        libro.setGenero(dto.getGenero());
        libro.setNumeroPaginas(dto.getNumeroPaginas());

        // Buscar y asignar autor si autorId es válido
        if (dto.getAutorId() != null) {
            Optional<Autor> autorOpt = autorRepository.findById(dto.getAutorId());
            if (autorOpt.isPresent()) {
                libro.setAutor(autorOpt.get());
            } else {
                return new ResponseDTO("Error: Autor no encontrado con id " + dto.getAutorId(), null);
            }
        } else {
            return new ResponseDTO("Error: AutorId es requerido para crear libro", null);
        }

        Libro libroGuardado = libroRepository.save(libro);

        LibroDTO libroGuardadoDTO = mapToDTO(libroGuardado);

        return new ResponseDTO("Libro creado exitosamente", libroGuardadoDTO);
    }

    public ResponseDTO obtenerTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();
        List<LibroDTO> dtos = libros.stream().map(this::mapToDTO).collect(Collectors.toList());
        return new ResponseDTO("Lista de libros obtenida", dtos);
    }

    public ResponseDTO actualizarLibro(Integer id, LibroDTO dto) {
        Optional<Libro> libroOpt = libroRepository.findById(id);
        if (libroOpt.isEmpty()) {
            return new ResponseDTO("Libro no encontrado con id: " + id, null);
        }

        Libro libroExistente = libroOpt.get();
        libroExistente.setGenero(dto.getGenero());
        libroExistente.setNumeroPaginas(dto.getNumeroPaginas());

        if (dto.getAutorId() != null) {
            Optional<Autor> autorOpt = autorRepository.findById(dto.getAutorId());
            if (autorOpt.isEmpty()) {
                return new ResponseDTO("Error: Autor no encontrado con id " + dto.getAutorId(), null);
            }
            libroExistente.setAutor(autorOpt.get());
        } else {
            return new ResponseDTO("Error: AutorId es requerido para actualizar libro", null);
        }

        Libro libroActualizado = libroRepository.save(libroExistente);
        LibroDTO libroDTO = mapToDTO(libroActualizado);
        return new ResponseDTO("Libro actualizado exitosamente", libroDTO);
    }

    public ResponseDTO eliminarLibro(Integer id) {
        boolean existe = libroRepository.existsById(id);
        if (!existe) {
            return new ResponseDTO("Libro no encontrado con id: " + id, null);
        }
        libroRepository.deleteById(id);
        return new ResponseDTO("Libro eliminado exitosamente", null);
    }


    private LibroDTO mapToDTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setGenero(libro.getGenero());
        dto.setNumeroPaginas(libro.getNumeroPaginas());
        if (libro.getAutor() != null) {
            dto.setAutorId(libro.getAutor().getId());
        }
        return dto;
    }
}
