package com.liceo.notas.repositories;

import com.liceo.notas.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad {@link Rol}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los roles del sistema.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre.
     * Retorna un {@link Optional} para manejar de forma segura el caso en que no se encuentre el rol.
     *
     * @param nombre Nombre del rol a buscar (ej: "Docente", "Estudiante")
     * @return Optional conteniendo el rol si se encuentra, o vacío si no existe
     */
    Optional<Rol> findByNombre(String nombre);
}