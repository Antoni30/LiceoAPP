package edu.espe.publicaciones.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AutorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String institucion;
    private String email;
    private String orcid;
    private List<Long> libroIds;    // IDs de libros relacionados
    private List<Long> articuloIds; // IDs de artículos relacionados
}
