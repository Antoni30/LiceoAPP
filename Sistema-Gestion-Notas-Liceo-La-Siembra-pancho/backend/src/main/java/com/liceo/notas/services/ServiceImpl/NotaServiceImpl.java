package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.NotaDTO;
import com.liceo.notas.entities.Nota;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.Materia;
import com.liceo.notas.repositories.NotaRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.repositories.MateriaRepository;
import com.liceo.notas.dtos.mappers.NotaMapper;
import com.liceo.notas.services.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotaServiceImpl implements NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    @Transactional
    public NotaDTO registrarNota(NotaDTO dto) {
        Nota nota = NotaMapper.toEntity(dto);

        // Buscar y establecer usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        nota.setUsuario(usuario);

        // Buscar y establecer materia
        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        nota.setMateria(materia);

        // Validar nota
        if (nota.getNota() < 0 || nota.getNota() > 20) {
            throw new RuntimeException("La nota debe estar entre 0 y 20");
        }

        // Validar parcial
        if (nota.getParcial() < 1 || nota.getParcial() > 3) {
            throw new RuntimeException("El parcial debe estar entre 1 y 3");
        }

        // Verificar si ya existe una nota para este estudiante, materia y parcial
        Nota notaExistente = notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial(
                dto.getIdUsuario(), dto.getIdMateria(), dto.getParcial());
        
        if (notaExistente != null) {
            throw new RuntimeException("Ya existe una nota para este estudiante en esta materia y parcial");
        }

        Nota notaGuardada = notaRepository.save(nota);
        return NotaMapper.toDTO(notaGuardada);
    }

    @Override
    public List<NotaDTO> listarPorUsuario(String idUsuario) {
        List<Nota> notas = notaRepository.findByUsuarioIdUsuario(idUsuario);
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaDTO> listarPorMateria(Integer idMateria) {
        List<Nota> notas = notaRepository.findByMateriaId(idMateria);
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaDTO> listarPorUsuarioYMateria(String idUsuario, Integer idMateria) {
        List<Nota> notas = notaRepository.findByUsuarioIdUsuarioAndMateriaId(idUsuario, idMateria);
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaDTO> listarPorUsuarioYParcial(String idUsuario, Integer parcial) {
        List<Nota> notas = notaRepository.findByUsuarioIdUsuarioAndParcial(idUsuario, parcial);
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaDTO> listarPorMateriaYParcial(Integer idMateria, Integer parcial) {
        List<Nota> notas = notaRepository.findByMateriaIdAndParcial(idMateria, parcial);
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotaDTO obtenerNotaEspecifica(String idUsuario, Integer idMateria, Integer parcial) {
        Nota nota = notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial(idUsuario, idMateria, parcial);
        return NotaMapper.toDTO(nota);
    }

    @Override
    @Transactional
    public NotaDTO actualizarNota(Integer id, NotaDTO dto) {
        Nota notaExistente = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada"));

        // Validar nota
        if (dto.getNota() < 0 || dto.getNota() > 20) {
            throw new RuntimeException("La nota debe estar entre 0 y 20");
        }

        // Validar parcial
        if (dto.getParcial() < 1 || dto.getParcial() > 3) {
            throw new RuntimeException("El parcial debe estar entre 1 y 3");
        }

        // Si se cambia usuario, materia o parcial, verificar que no exista otra nota con esa combinación
        if (!notaExistente.getUsuario().getIdUsuario().equals(dto.getIdUsuario()) ||
            !notaExistente.getMateria().getId().equals(dto.getIdMateria()) ||
            !notaExistente.getParcial().equals(dto.getParcial())) {
            
            Nota otraNota = notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial(
                    dto.getIdUsuario(), dto.getIdMateria(), dto.getParcial());
            
            if (otraNota != null && !otraNota.getId().equals(id)) {
                throw new RuntimeException("Ya existe una nota para este estudiante en esta materia y parcial");
            }
        }

        // Actualizar usuario si es necesario
        if (!notaExistente.getUsuario().getIdUsuario().equals(dto.getIdUsuario())) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            notaExistente.setUsuario(usuario);
        }

        // Actualizar materia si es necesario
        if (!notaExistente.getMateria().getId().equals(dto.getIdMateria())) {
            Materia materia = materiaRepository.findById(dto.getIdMateria())
                    .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
            notaExistente.setMateria(materia);
        }

        // Actualizar campos
        notaExistente.setNota(dto.getNota());
        notaExistente.setParcial(dto.getParcial());

        Nota notaActualizada = notaRepository.save(notaExistente);
        return NotaMapper.toDTO(notaActualizada);
    }

    @Override
    @Transactional
    public void eliminarNota(Integer id) {
        if (!notaRepository.existsById(id)) {
            throw new RuntimeException("Nota no encontrada");
        }
        notaRepository.deleteById(id);
    }

    @Override
    public List<NotaDTO> listarTodas() {
        List<Nota> notas = notaRepository.findAll();
        return notas.stream()
                .map(NotaMapper::toDTO)
                .collect(Collectors.toList());
    }
}