package com.liceo.notas.repositories;

import com.liceo.notas.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Notificacion}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a las notificaciones de los usuarios.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    /**
     * Busca todas las notificaciones asociadas a un usuario específico por su ID.
     * Este método utiliza una consulta JPQL explícita para navegar por la relación.
     *
     * @param idUsuario ID del usuario para filtrar sus notificaciones
     * @return Lista de notificaciones pertenecientes al usuario especificado
     */
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario")
    List<Notificacion> findByUsuarioId(@Param("idUsuario") String idUsuario);

    /**
     * Busca todas las notificaciones asociadas a un usuario específico por su ID.
     * Este método utiliza la convención de nombres de Spring Data JPA para relaciones anidadas.
     *
     * @param idUsuario ID del usuario para filtrar sus notificaciones
     * @return Lista de notificaciones pertenecientes al usuario especificado
     */
    List<Notificacion> findByUsuario_IdUsuario(String idUsuario);

    /**
     * Busca todas las notificaciones generadas en una fecha específica.
     *
     * @param fecha Fecha de generación de las notificaciones a buscar
     * @return Lista de notificaciones con la fecha especificada
     */
    List<Notificacion> findByFechaNotificacion(LocalDate fecha);
}