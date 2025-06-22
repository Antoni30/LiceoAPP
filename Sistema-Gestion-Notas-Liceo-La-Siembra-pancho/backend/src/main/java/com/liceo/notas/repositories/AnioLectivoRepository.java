package com.liceo.notas.repositories;

import com.liceo.notas.entities.AnioLectivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link AnioLectivo}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los datos de años lectivos.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface AnioLectivoRepository extends JpaRepository<AnioLectivo, Integer> {

    /**
     * Busca todos los años lectivos que tengan el estado especificado.
     * Este método utiliza una Query Method de Spring Data JPA para filtrar por el campo "estado".
     *
     * @param estado Estado del año lectivo a buscar (ej: "Activo", "Inactivo")
     * @return Lista de años lectivos que coinciden con el estado dado
     */
    List<AnioLectivo> findByEstado(String estado);

    @Query("""
    SELECT a FROM AnioLectivo a
    WHERE (:fechaInicio <= a.fechaFinal AND :fechaFinal >= a.fechaInicio)
    """)
    List<AnioLectivo> findConflictingAnioLectivo(LocalDate fechaInicio, LocalDate fechaFinal);

}