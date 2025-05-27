package com.liceo.notas.repositories;

import com.liceo.notas.entities.UsuarioRol;
import com.liceo.notas.entities.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link UsuarioRol}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para gestionar las relaciones entre usuarios y roles.
 * Utiliza consultas JPQL personalizadas para operaciones avanzadas.
 */
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {

    /**
     * Elimina una asociación específica entre un usuario y un rol.
     *
     * @param idUsuario ID del usuario a desasociar
     * @param idRol     ID del rol a desasociar
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM UsuarioRol ur WHERE ur.usuario.idUsuario = :idUsuario AND ur.rol.id = :idRol")
    void deleteByUsuarioIdAndRolId(@Param("idUsuario") String idUsuario,
                                   @Param("idRol") Integer idRol);

    /**
     * Busca todas las asociaciones entre roles y usuarios para un usuario específico.
     *
     * @param idUsuario ID del usuario para filtrar las asociaciones
     * @return Lista de objetos UsuarioRol asociados al usuario especificado
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.idUsuario = :idUsuario")
    List<UsuarioRol> findByUsuarioId(@Param("idUsuario") String idUsuario);

    /**
     * Busca todas las asociaciones entre usuarios y roles para un rol específico.
     *
     * @param idRol ID del rol para filtrar las asociaciones
     * @return Lista de objetos UsuarioRol asociados al rol especificado
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.rol.id = :idRol")
    List<UsuarioRol> findByRolId(@Param("idRol") Integer idRol);

    /**
     * Verifica si existe una asociación entre un usuario y un rol específicos.
     *
     * @param idUsuario ID del usuario a verificar
     * @param idRol     ID del rol a verificar
     * @return true si la asociación existe, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END " +
            "FROM UsuarioRol ur WHERE ur.usuario.idUsuario = :idUsuario AND ur.rol.id = :idRol")
    boolean existsByUsuarioIdAndRolId(@Param("idUsuario") String idUsuario,
                                      @Param("idRol") Integer idRol);
}