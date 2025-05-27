package edu.espe.publicaciones.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Paper extends Publicacion{
    private String orcid;
    private Date fechaPublicacion;
    private String revista;
    private String areaInvestigacion;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;
}
