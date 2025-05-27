package com.liceo.notas.repositories;

import com.liceo.notas.entities.CursoMateria;
import com.liceo.notas.entities.CursoMateriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link CursoMateria}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para gestionar las relaciones entre cursos y materias.
 * Utiliza consultas JPQL personalizadas para operaciones avanzadas.
 */
public interface CursoMateriaRepository extends JpaRepository<CursoMateria, CursoMateriaId> {

    /**
     * Elimina una asociación específica entre un curso y una materia.
     *
     * @param idCurso    ID del curso a desasociar
     * @param idMateria  ID de la materia a desasociar
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM CursoMateria cm WHERE cm.curso.id = :idCurso AND cm.materia.id = :idMateria")
    void deleteByCursoIdAndMateriaId(@Param("idCurso") Integer idCurso,
                                     @Param("idMateria") Integer idMateria);

    /**
     * Busca todas las asociaciones entre cursos y materias para un curso específico.
     *
     * @param idCurso ID del curso para filtrar las asociaciones
     * @return Lista de objetos CursoMateria asociados al curso especificado
     */
    @Query("SELECT cm FROM CursoMateria cm WHERE cm.curso.id = :idCurso")
    List<CursoMateria> findByCursoId(@Param("idCurso") Integer idCurso);

    /**
     * Verifica si existe una asociación entre un curso y una materia específicos.
     *
     * @param idCurso    ID del curso a verificar
     * @param idMateria  ID de la materia a verificar
     * @return true si la asociación existe, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(cm) > 0 THEN true ELSE false END " +
            "FROM CursoMateria cm WHERE cm.curso.id = :idCurso AND cm.materia.id = :idMateria")
    boolean existsByCursoIdAndMateriaId(@Param("idCurso") Integer idCurso,
                                        @Param("idMateria") Integer idMateria);

    /**
     * Busca todas las asociaciones entre cursos y materias para una materia específica.
     *
     * @param idMateria ID de la materia para filtrar las asociaciones
     * @return Lista de objetos CursoMateria asociados a la materia especificada
     */
    @Query("SELECT cm FROM CursoMateria cm WHERE cm.materia.id = :idMateria")
    List<CursoMateria> findByMateriaId(@Param("idMateria") Integer idMateria);
}