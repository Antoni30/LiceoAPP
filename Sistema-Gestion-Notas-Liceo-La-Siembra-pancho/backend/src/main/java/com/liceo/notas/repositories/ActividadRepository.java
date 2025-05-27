package com.liceo.notas.repositories;

import com.liceo.notas.entities.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Actividad}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los datos de las actividades.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {

    /**
     * Busca todas las actividades asociadas a un parcial específico.
     *
     * @param idParcial ID del parcial para filtrar las actividades
     * @return Lista de actividades pertenecientes al parcial especificado
     */
    List<Actividad> findByParcialId(Integer idParcial);
}