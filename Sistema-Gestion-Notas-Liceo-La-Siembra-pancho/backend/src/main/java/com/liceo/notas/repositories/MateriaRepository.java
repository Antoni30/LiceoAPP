package com.liceo.notas.repositories;

import com.liceo.notas.entities.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad {@link Materia}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a las materias del sistema.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    /**
     * Busca una materia por su nombre.
     * Devuelve un {@link Optional} para manejar de forma segura el caso en que no se encuentre la materia.
     *
     * @param nombreMateria Nombre de la materia a buscar
     * @return Optional conteniendo la materia si se encuentra, o vacío si no existe
     */
    Optional<Materia> findByNombreMateria(String nombreMateria);
}