package com.liceo.notas.repositories;

import com.liceo.notas.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link Calificacion}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a las calificaciones de los estudiantes.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    /**
     * Busca todas las calificaciones asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario (estudiante) para filtrar las calificaciones
     * @return Lista de calificaciones pertenecientes al estudiante especificado
     */
    List<Calificacion> findByUsuarioIdUsuario(String idUsuario);

    /**
     * Busca todas las calificaciones asociadas a una actividad específica.
     *
     * @param idActividad ID de la actividad para filtrar las calificaciones
     * @return Lista de calificaciones asociadas a la actividad especificada
     */
    List<Calificacion> findByActividadId(Integer idActividad);

    /**
     * Busca todas las calificaciones de un estudiante en actividades pertenecientes a una materia específica.
     * Esta búsqueda cruza relaciones anidadas: usuario → actividad → parcial → materia.
     *
     * @param idUsuario  ID del usuario (estudiante)
     * @param idMateria  ID de la materia para filtrar las calificaciones
     * @return Lista de calificaciones del estudiante en la materia especificada
     */
    List<Calificacion> findByUsuarioIdUsuarioAndActividadParcialMateriaId(String idUsuario, Integer idMateria);
}