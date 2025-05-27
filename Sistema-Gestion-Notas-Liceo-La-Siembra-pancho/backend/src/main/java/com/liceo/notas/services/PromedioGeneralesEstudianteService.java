package com.liceo.notas.services;

import com.liceo.notas.dtos.PromedioGeneralesEstudianteDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión del promedio general de estudiantes.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar promedios generales.
 */
public interface PromedioGeneralesEstudianteService {

    /**
     * Registra un nuevo promedio general del estudiante en el sistema.
     *
     * @param dto Objeto {@link PromedioGeneralesEstudianteDTO} con los datos del promedio a registrar
     * @return {@link PromedioGeneralesEstudianteDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    PromedioGeneralesEstudianteDTO guardarPromedio(PromedioGeneralesEstudianteDTO dto);

    /**
     * Actualiza los datos de un promedio general existente.
     *
     * @param dto Objeto {@link PromedioGeneralesEstudianteDTO} con los nuevos datos del promedio
     * @return {@link PromedioGeneralesEstudianteDTO} con los datos actualizados
     */
    PromedioGeneralesEstudianteDTO actualizarPromedio(PromedioGeneralesEstudianteDTO dto);

    /**
     * Elimina un registro de promedio general por su ID.
     *
     * @param id ID del promedio a eliminar
     */
    void eliminarPromedio(Integer id);

    /**
     * Obtiene los datos de un promedio general por su ID.
     *
     * @param id ID del promedio a obtener
     * @return {@link PromedioGeneralesEstudianteDTO} con los datos del promedio encontrado
     */
    PromedioGeneralesEstudianteDTO obtenerPorId(Integer id);

    /**
     * Obtiene una lista con todos los promedios generales registrados en el sistema.
     *
     * @return Lista de objetos {@link PromedioGeneralesEstudianteDTO} con todos los promedios
     */
    List<PromedioGeneralesEstudianteDTO> listarTodos();

    /**
     * Obtiene el promedio general de un estudiante específico dentro de un curso determinado.
     *
     * @param idUsuario ID del usuario estudiante
     * @param idCurso   ID del curso en el cual se desea obtener el promedio
     * @return {@link PromedioGeneralesEstudianteDTO} con el promedio general del estudiante en ese curso
     */
    PromedioGeneralesEstudianteDTO obtenerPorEstudianteYCurso(String idUsuario, Integer idCurso);
}