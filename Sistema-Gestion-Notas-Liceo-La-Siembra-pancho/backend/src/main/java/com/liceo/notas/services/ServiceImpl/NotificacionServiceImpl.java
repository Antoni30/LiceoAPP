package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.NotificacionDTO;
import com.liceo.notas.dtos.mappers.NotificacionMapper;
import com.liceo.notas.entities.Notificacion;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.repositories.NotificacionRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        Notificacion notificacion = NotificacionMapper.toEntity(dto);

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        notificacion.setUsuario(usuario);

        notificacion = notificacionRepository.save(notificacion);
        return NotificacionMapper.toDTO(notificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorUsuario(String idUsuario) {
        return notificacionRepository.findByUsuarioId(idUsuario).stream()
                .map(NotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorFecha(LocalDate fecha) {
        return notificacionRepository.findByFechaNotificacion(fecha).stream()
                .map(NotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean eliminarNotificacion(Integer id) {
        return notificacionRepository.findById(id)
                .map(notificacion -> {
                    notificacionRepository.delete(notificacion);
                    return true;
                }).orElse(false);
    }
}