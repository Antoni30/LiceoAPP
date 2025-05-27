package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.CalificacionDTO;
import com.liceo.notas.entities.Calificacion;
import com.liceo.notas.entities.Actividad;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.repositories.CalificacionRepository;
import com.liceo.notas.repositories.ActividadRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.dtos.mappers.CalificacionMapper;
import com.liceo.notas.services.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionServiceImpl implements CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Override
    @Transactional
    public CalificacionDTO registrarCalificacion(CalificacionDTO dto) {
        Calificacion calificacion = CalificacionMapper.toEntity(dto);

        // Buscar y establecer usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        calificacion.setUsuario(usuario);

        // Buscar y establecer actividad
        Actividad actividad = actividadRepository.findById(dto.getIdActividad())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        calificacion.setActividad(actividad);

        // Validar que la nota no supere el valor máximo
        if (calificacion.getNota() > actividad.getValorMaximo()) {
            throw new RuntimeException("La nota no puede ser mayor al valor máximo de la actividad");
        }

        calificacion = calificacionRepository.save(calificacion);
        return CalificacionMapper.toDTO(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> listarPorUsuario(String idUsuario) {
        return calificacionRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(CalificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionDTO> listarPorActividad(Integer idActividad) {
        return calificacionRepository.findByActividadId(idActividad).stream()
                .map(CalificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CalificacionDTO actualizarCalificacion(Integer id, CalificacionDTO dto) {
        return calificacionRepository.findById(id)
                .map(calificacionExistente -> {
                    // Actualizar solo los campos permitidos
                    calificacionExistente.setNota(dto.getNota());
                    calificacionExistente.setComentario(dto.getComentario());

                    // Validar la nota contra el valor máximo de la actividad
                    if (dto.getNota() > calificacionExistente.getActividad().getValorMaximo()) {
                        throw new RuntimeException("La nota no puede ser mayor al valor máximo de la actividad");
                    }

                    calificacionExistente = calificacionRepository.save(calificacionExistente);
                    return CalificacionMapper.toDTO(calificacionExistente);
                })
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada"));
    }
}