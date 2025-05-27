package edu.espe.publicaciones.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Libro extends Publicacion{
    private String genero;
    private int numeroPaginas;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;
}
