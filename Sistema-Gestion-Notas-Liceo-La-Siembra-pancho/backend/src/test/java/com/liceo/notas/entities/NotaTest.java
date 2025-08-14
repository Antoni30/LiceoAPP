package com.liceo.notas.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad Nota")
class NotaTest {

    private Nota nota;
    private Validator validator;

    @BeforeEach
    void setUp() {
        nota = new Nota();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        nota.setId(id);
        
        assertThat(nota.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener usuario correctamente")
    void deberiaEstablecerYObtenerUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("1234567890");
        usuario.setNombres("Juan");
        
        nota.setUsuario(usuario);
        
        assertThat(nota.getUsuario()).isEqualTo(usuario);
        assertThat(nota.getUsuario().getIdUsuario()).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("Debería establecer y obtener materia correctamente")
    void deberiaEstablecerYObtenerMateria() {
        Materia materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Matemáticas");
        
        nota.setMateria(materia);
        
        assertThat(nota.getMateria()).isEqualTo(materia);
        assertThat(nota.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
    }

    @Test
    @DisplayName("Debería establecer y obtener nota válida correctamente")
    void deberiaEstablecerYObtenerNotaValida() {
        Double notaValor = 15.5;
        nota.setNota(notaValor);
        
        assertThat(nota.getNota()).isEqualTo(notaValor);
    }

    @Test
    @DisplayName("Debería establecer y obtener parcial válido correctamente")
    void deberiaEstablecerYObtenerParcialValido() {
        Integer parcial = 2;
        nota.setParcial(parcial);
        
        assertThat(nota.getParcial()).isEqualTo(parcial);
    }

    @Test
    @DisplayName("Debería validar nota mínima de 0")
    void deberiaValidarNotaMinimaDeZero() {
        nota.setNota(0.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería validar nota máxima de 20")
    void deberiaValidarNotaMaximaDe20() {
        nota.setNota(20.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería fallar validación con nota menor a 0")
    void deberiaFallarValidacionConNotaMenorACero() {
        nota.setNota(-1.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La nota no puede ser menor a 0");
    }

    @Test
    @DisplayName("Debería fallar validación con nota mayor a 20")
    void deberiaFallarValidacionConNotaMayorA20() {
        nota.setNota(21.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La nota no puede ser mayor a 20");
    }

    @Test
    @DisplayName("Debería validar parcial mínimo de 1")
    void deberiaValidarParcialMinimoDe1() {
        nota.setNota(15.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería validar parcial máximo de 3")
    void deberiaValidarParcialMaximoDe3() {
        nota.setNota(15.0);
        nota.setParcial(3);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería fallar validación con parcial menor a 1")
    void deberiaFallarValidacionConParcialMenorA1() {
        nota.setNota(15.0);
        nota.setParcial(0);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El parcial debe ser mínimo 1");
    }

    @Test
    @DisplayName("Debería fallar validación con parcial mayor a 3")
    void deberiaFallarValidacionConParcialMayorA3() {
        nota.setNota(15.0);
        nota.setParcial(4);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El parcial no puede ser mayor a 3");
    }

    @Test
    @DisplayName("Debería fallar validación con usuario nulo")
    void deberiaFallarValidacionConUsuarioNulo() {
        nota.setNota(15.0);
        nota.setParcial(1);
        nota.setUsuario(null);
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El usuario es obligatorio");
    }

    @Test
    @DisplayName("Debería fallar validación con materia nula")
    void deberiaFallarValidacionConMateriaNula() {
        nota.setNota(15.0);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(null);
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La materia es obligatoria");
    }

    @Test
    @DisplayName("Debería fallar validación con nota nula")
    void deberiaFallarValidacionConNotaNula() {
        nota.setNota(null);
        nota.setParcial(1);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La nota es obligatoria");
    }

    @Test
    @DisplayName("Debería fallar validación con parcial nulo")
    void deberiaFallarValidacionConParcialNulo() {
        nota.setNota(15.0);
        nota.setParcial(null);
        nota.setUsuario(crearUsuarioValido());
        nota.setMateria(crearMateriaValida());
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El parcial es obligatorio");
    }

    @Test
    @DisplayName("Debería crear nota completa válida")
    void deberiaCrearNotaCompletaValida() {
        Usuario usuario = crearUsuarioValido();
        Materia materia = crearMateriaValida();
        
        nota.setId(1);
        nota.setUsuario(usuario);
        nota.setMateria(materia);
        nota.setNota(18.5);
        nota.setParcial(2);
        
        Set<ConstraintViolation<Nota>> violations = validator.validate(nota);
        assertThat(violations).isEmpty();
        
        assertThat(nota.getId()).isEqualTo(1);
        assertThat(nota.getUsuario()).isEqualTo(usuario);
        assertThat(nota.getMateria()).isEqualTo(materia);
        assertThat(nota.getNota()).isEqualTo(18.5);
        assertThat(nota.getParcial()).isEqualTo(2);
    }

    private Usuario crearUsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("1234567890");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setNickname("jperez");
        usuario.setContrasena("password123");
        usuario.setEstado("Activo");
        return usuario;
    }

    private Materia crearMateriaValida() {
        Materia materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Matemáticas");
        // materia.setEstadoMateria("Activa"); // Campo no disponible en la entidad
        return materia;
    }
}