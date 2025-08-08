package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.NotaDTO;
import com.liceo.notas.entities.Nota;

/**
 * Mapper para convertir entre entidades {@link Nota} y objetos DTO {@link NotaDTO}.
 * Proporciona métodos estáticos para transformar datos entre las capas de presentación y persistencia.
 */
public class NotaMapper {

    /**
     * Convierte una entidad {@link Nota} en un objeto DTO {@link NotaDTO}.
     *
     * @param nota La entidad Nota a convertir
     * @return El objeto NotaDTO correspondiente, o null si la entidad es null
     */
    public static NotaDTO toDTO(Nota nota) {
        if (nota == null) {
            return null;
        }

        NotaDTO dto = new NotaDTO();
        dto.setId(nota.getId());
        dto.setIdUsuario(nota.getUsuario() != null ? nota.getUsuario().getIdUsuario() : null);
        dto.setIdMateria(nota.getMateria() != null ? nota.getMateria().getId() : null);
        dto.setNota(nota.getNota());
        dto.setParcial(nota.getParcial());

        return dto;
    }

    /**
     * Convierte un objeto DTO {@link NotaDTO} en una entidad {@link Nota}.
     * 
     * Nota: Este método no establece las relaciones con Usuario y Materia,
     * ya que estas deben ser resueltas desde el repositorio correspondiente.
     *
     * @param dto El objeto NotaDTO a convertir
     * @return La entidad Nota correspondiente, o null si el DTO es null
     */
    public static Nota toEntity(NotaDTO dto) {
        if (dto == null) {
            return null;
        }

        Nota nota = new Nota();
        nota.setId(dto.getId());
        nota.setNota(dto.getNota());
        nota.setParcial(dto.getParcial());

        return nota;
    }
}