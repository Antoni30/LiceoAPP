package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.ParcialDTO;
import com.liceo.notas.dtos.mappers.ParcialMapper;
import com.liceo.notas.entities.Materia;
import com.liceo.notas.entities.Parcial;
import com.liceo.notas.repositories.MateriaRepository;
import com.liceo.notas.repositories.ParcialRepository;
import com.liceo.notas.services.ParcialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParcialServiceImpl implements ParcialService {
    @Autowired
    private ParcialRepository parcialRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    @Transactional
    public ParcialDTO crearParcial(ParcialDTO dto) {
        // Validar que no exista un parcial con el mismo número para la misma materia
        if (parcialRepository.findByMateriaIdAndNumeroParcial(dto.getIdMateria(), dto.getNumeroParcial()).isPresent()) {
            throw new RuntimeException("Ya existe un parcial con este número para la materia");
        }

        Parcial parcial = ParcialMapper.toEntity(dto);
        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        parcial.setMateria(materia);

        parcial = parcialRepository.save(parcial);
        return ParcialMapper.toDTO(parcial);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParcialDTO> obtenerPorId(Integer id) {
        return parcialRepository.findById(id)
                .map(ParcialMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcialDTO> listarTodos() {
        return parcialRepository.findAll().stream()
                .map(ParcialMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcialDTO> listarPorMateria(Integer idMateria) {
        return parcialRepository.findByMateriaId(idMateria).stream()
                .map(ParcialMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<ParcialDTO> actualizarParcial(Integer id, ParcialDTO dto) {
        return parcialRepository.findById(id)
                .map(parcial -> {
                    // Validar que el número de parcial no esté duplicado
                    if (!parcial.getNumeroParcial().equals(dto.getNumeroParcial())) {  // Se añadió el paréntesis que faltaba
                        Optional<Parcial> existente = parcialRepository.findByMateriaIdAndNumeroParcial(
                                parcial.getMateria().getId(), dto.getNumeroParcial());
                        if (existente.isPresent()) {
                            throw new RuntimeException("Ya existe un parcial con este número para la materia");
                        }
                    }

                    parcial.setNumeroParcial(dto.getNumeroParcial());
                    parcial.setFechaInicio(dto.getFechaInicio());
                    parcial.setFechaFin(dto.getFechaFin());

                    return ParcialMapper.toDTO(parcialRepository.save(parcial));
                });
    }

    @Override
    @Transactional
    public boolean eliminarParcial(Integer id) {
        return parcialRepository.findById(id)
                .map(parcial -> {
                    parcialRepository.delete(parcial);
                    return true;
                }).orElse(false);
    }
}