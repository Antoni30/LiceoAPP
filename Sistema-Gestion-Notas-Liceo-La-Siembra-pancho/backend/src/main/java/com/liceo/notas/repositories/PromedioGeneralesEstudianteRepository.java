package com.liceo.notas.repositories;

import com.liceo.notas.entities.PromedioGeneralesEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad {@link PromedioGeneralesEstudiante}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los promedios generales de estudiantes.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
@Repository
public interface PromedioGeneralesEstudianteRepository extends JpaRepository<PromedioGeneralesEstudiante, Integer> {

    /**
     * Busca un registro de promedio general de un estudiante en un curso específico.
     *
     * @param idUsuario ID del usuario estudiante
     * @param idCurso   ID del curso en el cual se desea obtener el promedio
     * @return Optional conteniendo el promedio si existe, o vacío si no se encuentra
     */
    @Query("SELECT p FROM PromedioGeneralesEstudiante p WHERE p.idUsuario = :idUsuario AND p.curso.id = :idCurso")
    Optional<PromedioGeneralesEstudiante> findByIdUsuarioAndCursoId(
            @Param("idUsuario") String idUsuario,
            @Param("idCurso") Integer idCurso);

    /**
     * Verifica si existe un registro de promedio general para un estudiante y curso específicos.
     *
     * @param idUsuario ID del usuario estudiante
     * @param idCurso   ID del curso a verificar
     * @return true si el registro existe, false en caso contrario
     */
    boolean existsByIdUsuarioAndCursoId(String idUsuario, Integer idCurso);
}