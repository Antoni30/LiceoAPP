package edu.espe.publicaciones.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibroDTO {
    private Long id;             // Id heredado de Publicacion (asumo que Publicacion tiene un id Long)
    private String titulo;       // Si Publicacion tiene título u otros campos, incluirlos aquí también
    private String genero;
    private int numeroPaginas;
    private Long autorId;        // Solo el ID del autor, no toda la entidad
}
