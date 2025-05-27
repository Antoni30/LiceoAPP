package com.liceo.notas.repositories;

import com.liceo.notas.entities.UsuarioCurso;
import com.liceo.notas.entities.UsuarioCursoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link UsuarioCurso}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para gestionar las relaciones entre usuarios y cursos.
 * Utiliza consultas JPQL personalizadas para operaciones avanzadas.
 */
public interface UsuarioCursoRepository extends JpaRepository<UsuarioCurso, UsuarioCursoId> {

    /**
     * Elimina una asociación específica entre un usuario y un curso.
     *
     * @param idUsuario ID del usuario a desasociar
     * @param idCurso   ID del curso a desasociar
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM UsuarioCurso uc WHERE uc.usuario.idUsuario = :idUsuario AND uc.curso.id = :idCurso")
    void deleteByUsuarioIdAndCursoId(@Param("idUsuario") String idUsuario,
                                     @Param("idCurso") Integer idCurso);

    /**
     * Busca todas las asociaciones entre cursos y usuarios para un usuario específico.
     *
     * @param idUsuario ID del usuario para filtrar las asociaciones
     * @return Lista de objetos UsuarioCurso asociados al usuario especificado
     */
    @Query("SELECT uc FROM UsuarioCurso uc WHERE uc.usuario.idUsuario = :idUsuario")
    List<UsuarioCurso> findByUsuarioId(@Param("idUsuario") String idUsuario);

    /**
     * Busca todas las asociaciones entre usuarios y cursos para un curso específico.
     *
     * @param idCurso ID del curso para filtrar las asociaciones
     * @return Lista de objetos UsuarioCurso asociados al curso especificado
     */
    @Query("SELECT uc FROM UsuarioCurso uc WHERE uc.curso.id = :idCurso")
    List<UsuarioCurso> findByCursoId(@Param("idCurso") Integer idCurso);

    /**
     * Verifica si existe una asociación entre un usuario y un curso específicos.
     *
     * @param idUsuario ID del usuario a verificar
     * @param idCurso   ID del curso a verificar
     * @return true si la asociación existe, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END " +
            "FROM UsuarioCurso uc WHERE uc.usuario.idUsuario = :idUsuario AND uc.curso.id = :idCurso")
    boolean existsByUsuarioIdAndCursoId(@Param("idUsuario") String idUsuario,
                                        @Param("idCurso") Integer idCurso);
}