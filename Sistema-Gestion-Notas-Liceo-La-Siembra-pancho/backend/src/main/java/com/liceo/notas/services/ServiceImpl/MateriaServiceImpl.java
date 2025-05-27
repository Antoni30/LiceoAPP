package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.MateriaDTO;
import com.liceo.notas.dtos.mappers.MateriaMapper;
import com.liceo.notas.entities.Materia;
import com.liceo.notas.repositories.MateriaRepository;
import com.liceo.notas.services.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private MateriaRepository repository;

    @Override
    @Transactional
    public MateriaDTO crearMateria(MateriaDTO dto) {
        // Verificar si el nombre de materia ya existe
        if(repository.findByNombreMateria(dto.getNombreMateria()).isPresent()) {
            throw new RuntimeException("Ya existe una materia con ese nombre");
        }

        Materia materia = MateriaMapper.toEntity(dto);
        materia = repository.save(materia);
        return MateriaMapper.toDTO(materia);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MateriaDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(MateriaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listarTodas() {
        return repository.findAll().stream()
                .map(MateriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<MateriaDTO> actualizarMateria(Integer id, MateriaDTO dto) {
        return repository.findById(id)
                .map(materia -> {
                    // Verificar si el nombre cambió y es único
                    if(!materia.getNombreMateria().equals(dto.getNombreMateria()) &&
                            repository.findByNombreMateria(dto.getNombreMateria()).isPresent()) {
                        throw new RuntimeException("Ya existe una materia con ese nombre");
                    }

                    materia.setNombreMateria(dto.getNombreMateria());
                    materia = repository.save(materia);
                    return MateriaMapper.toDTO(materia);
                });
    }

    @Override
    @Transactional
    public boolean eliminarMateria(Integer id) {
        return repository.findById(id)
                .map(materia -> {
                    // Verificar si la materia tiene cursos asociados
                    if (!materia.getCursosAsociados().isEmpty()) {
                        throw new IllegalStateException(
                                "No se puede eliminar la materia con ID " + id +
                                        " porque está asignada a " + materia.getCursosAsociados().size() +
                                        " curso(s)");
                    }
                    repository.delete(materia);
                    return true;
                })
                .orElse(false); // Retorna false si la materia no existe
    }
}