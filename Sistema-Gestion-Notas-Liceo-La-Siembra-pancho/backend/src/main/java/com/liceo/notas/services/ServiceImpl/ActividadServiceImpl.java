package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.ActividadDTO;
import com.liceo.notas.entities.Actividad;
import com.liceo.notas.entities.Parcial;
import com.liceo.notas.repositories.ActividadRepository;
import com.liceo.notas.repositories.ParcialRepository;
import com.liceo.notas.dtos.mappers.ActividadMapper;
import com.liceo.notas.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ParcialRepository parcialRepository;

    @Override
    @Transactional
    public ActividadDTO crearActividad(ActividadDTO dto) {
        Actividad actividad = ActividadMapper.toEntity(dto);

        // Buscar y establecer el parcial
        Parcial parcial = parcialRepository.findById(dto.getIdParcial())
                .orElseThrow(() -> new RuntimeException("Parcial no encontrado"));
        actividad.setParcial(parcial);

        actividad = actividadRepository.save(actividad);
        return ActividadMapper.toDTO(actividad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadDTO> listarPorParcial(Integer idParcial) {
        return actividadRepository.findByParcialId(idParcial).stream()
                .map(ActividadMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActividadDTO obtenerPorId(Integer id) {
        return actividadRepository.findById(id)
                .map(ActividadMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }

    @Override
    @Transactional
    public void eliminarActividad(Integer id) {
        if (!actividadRepository.existsById(id)) {
            throw new RuntimeException("Actividad no encontrada ");
        }
        actividadRepository.deleteById(id);
    }
}