package com.liceo.notas.services;

import com.liceo.notas.dtos.MateriaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de materias académicas.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar materias del sistema.
 */
public interface MateriaService {

    /**
     * Crea una nueva materia con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link MateriaDTO} que contiene los datos de la materia a crear
     * @return {@link MateriaDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    MateriaDTO crearMateria(MateriaDTO dto);

    /**
     * Obtiene los datos de una materia por su identificador único.
     *
     * @param id ID de la materia a obtener
     * @return Optional conteniendo el {@link MateriaDTO} si se encuentra, o vacío si no existe
     */
    Optional<MateriaDTO> obtenerPorId(Integer id);

    /**
     * Obtiene una lista con todas las materias registradas en el sistema.
     *
     * @return Lista de objetos {@link MateriaDTO} con todas las materias disponibles
     */
    List<MateriaDTO> listarTodas();

    /**
     * Actualiza los datos de una materia existente identificada por su ID.
     *
     * @param id  ID de la materia a actualizar
     * @param dto Objeto {@link MateriaDTO} con los nuevos datos de la materia
     * @return Optional conteniendo el {@link MateriaDTO} actualizado si la operación fue exitosa, o vacío si no se encontró la materia
     */
    Optional<MateriaDTO> actualizarMateria(Integer id, MateriaDTO dto);

    /**
     * Elimina una materia del sistema por su ID.
     *
     * @param id ID de la materia a eliminar
     * @return true si la materia fue eliminada correctamente, false si no se encontró
     */
    boolean eliminarMateria(Integer id);
}