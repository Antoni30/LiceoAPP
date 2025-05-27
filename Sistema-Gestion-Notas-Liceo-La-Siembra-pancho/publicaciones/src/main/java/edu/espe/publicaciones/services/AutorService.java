package edu.espe.publicaciones.services;

import edu.espe.publicaciones.dto.AutorDTO;
import edu.espe.publicaciones.dto.ResponseDTO;
import edu.espe.publicaciones.entity.Autor;
import edu.espe.publicaciones.respository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }


    public ResponseDTO crearAutor(AutorDTO dto) {
        Autor autor1= new Autor();

        autor1.setNombre(dto.getNombre());
        autor1.setApellido(dto.getApellido());
        autor1.setEmail(dto.getEmail());
        autor1.setNacionalidad(dto.getNacionalidad());
        autor1.setInstitucion(dto.getInstitucion());

        return new ResponseDTO(
                "Autor registrado exitosamente",
                autorRepository.save(autor1));
    }

    public ResponseDTO obtenerTodosLosAutores() {
        List<Autor> autores = autorRepository.findAll();

        List<AutorDTO> dtos = autores.stream().map(autor -> {
            AutorDTO dto = new AutorDTO();
            dto.setId(autor.getId());
            dto.setNombre(autor.getNombre());
            dto.setApellido(autor.getApellido());
            dto.setEmail(autor.getEmail());
            dto.setNacionalidad(autor.getNacionalidad());
            dto.setInstitucion(autor.getInstitucion());
            dto.setOrcid(autor.getOrcid());
            return dto;
        }).collect(Collectors.toList());
        return new ResponseDTO("Lista de autores obtenida", dtos);
    }

    public ResponseDTO actualizarAutor(Long id, AutorDTO dto) {
        return autorRepository.findById(id).map(autorExistente -> {
            autorExistente.setNombre(dto.getNombre());
            autorExistente.setApellido(dto.getApellido());
            autorExistente.setEmail(dto.getEmail());
            autorExistente.setNacionalidad(dto.getNacionalidad());
            autorExistente.setInstitucion(dto.getInstitucion());
            autorExistente.setOrcid(dto.getOrcid());
            // Puedes actualizar también las relaciones si quieres

            Autor autorActualizado = autorRepository.save(autorExistente);

            AutorDTO dtoActualizado = new AutorDTO();

            dtoActualizado.setId(autorActualizado.getId());
            dtoActualizado.setNombre(autorActualizado.getNombre());
            dtoActualizado.setApellido(autorActualizado.getApellido());
            dtoActualizado.setEmail(autorActualizado.getEmail());
            dtoActualizado.setNacionalidad(autorActualizado.getNacionalidad());
            dtoActualizado.setInstitucion(autorActualizado.getInstitucion());
            dtoActualizado.setOrcid(autorActualizado.getOrcid());

            return new ResponseDTO("Autor actualizado exitosamente", dtoActualizado);
        }).orElseGet(() -> new ResponseDTO("Autor no encontrado con id: " + id, null));
    }

    public ResponseDTO eliminarAutor(Long id) {
        if (autorRepository.existsById(id)) {
            autorRepository.deleteById(id);
            return new ResponseDTO("Autor eliminado exitosamente", null);
        } else {
            return new ResponseDTO("Autor no encontrado con id: " + id, null);
        }
    }
}
