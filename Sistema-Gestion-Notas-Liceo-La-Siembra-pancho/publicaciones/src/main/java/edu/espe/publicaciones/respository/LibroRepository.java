package edu.espe.publicaciones.respository;

import edu.espe.publicaciones.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
}
