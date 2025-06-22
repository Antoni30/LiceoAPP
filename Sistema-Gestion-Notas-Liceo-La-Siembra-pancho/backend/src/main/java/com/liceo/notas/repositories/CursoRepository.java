package com.liceo.notas.repositories;

import com.liceo.notas.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Curso}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los cursos del sistema.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    /**
     * Busca todos los cursos asociados a un año lectivo específico.
     *
     * @param idAnioLectivo ID del año lectivo para filtrar los cursos
     * @return Lista de cursos pertenecientes al año lectivo especificado
     */
    List<Curso> findByAnioLectivoId(Integer idAnioLectivo);

    /**
     * Busca todos los cursos que pertenecen a años lectivos con estado "Activo".
     * Esta consulta utiliza JPQL para filtrar por el estado del año lectivo.
     *
     * @return Lista de cursos cuyo año lectivo asociado está activo
     */
    @Query("SELECT c FROM Curso c WHERE c.anioLectivo.estado = 'Activo'")
    List<Curso> findCursosByAnioActivo();

    boolean existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(String nombreCurso, Integer idAnioLectivo);

}