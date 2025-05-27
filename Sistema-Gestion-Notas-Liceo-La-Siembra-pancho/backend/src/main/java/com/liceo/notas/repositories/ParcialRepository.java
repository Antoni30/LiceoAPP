package com.liceo.notas.repositories;

import com.liceo.notas.entities.Parcial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad {@link Parcial}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los parciales del sistema.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
@Repository
public interface ParcialRepository extends JpaRepository<Parcial, Integer> {

    /**
     * Busca todos los parciales asociados a una materia específica.
     *
     * @param idMateria ID de la materia para filtrar los parciales
     * @return Lista de parciales pertenecientes a la materia especificada
     */
    List<Parcial> findByMateriaId(Integer idMateria);

    /**
     * Busca un parcial específico dentro de una materia por su número.
     *
     * @param idMateria      ID de la materia a la cual pertenece el parcial
     * @param numeroParcial  Número del parcial (ej: 1, 2, 3)
     * @return Optional conteniendo el parcial si existe, o vacío si no se encuentra
     */
    Optional<Parcial> findByMateriaIdAndNumeroParcial(Integer idMateria, Integer numeroParcial);
}