package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad Materia")
class MateriaTest {

    private Materia materia;

    @BeforeEach
    void setUp() {
        materia = new Materia();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        materia.setId(id);
        
        assertThat(materia.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre de materia correctamente")
    void deberiaEstablecerYObtenerNombreMateria() {
        String nombreMateria = "Matemáticas";
        materia.setNombreMateria(nombreMateria);
        
        assertThat(materia.getNombreMateria()).isEqualTo(nombreMateria);
    }

    @Test
    @DisplayName("Debería manejar nombre de materia largo")
    void deberiaManejarNombreDeMateriaLargo() {
        String nombreLargo = "Historia Universal Contempor";
        materia.setNombreMateria(nombreLargo);
        
        assertThat(materia.getNombreMateria()).isEqualTo(nombreLargo);
        assertThat(materia.getNombreMateria().length()).isLessThanOrEqualTo(30);
    }

    @Test
    @DisplayName("Debería manejar lista de cursos asociados correctamente")
    void deberiaManejarListaDeCursosAsociados() {
        List<CursoMateria> cursosAsociados = new ArrayList<>();
        materia.setCursosAsociados(cursosAsociados);
        
        assertThat(materia.getCursosAsociados()).isEmpty();
    }

    @Test
    @DisplayName("Debería agregar cursos asociados correctamente")
    void deberiaAgregarCursosAsociadosCorrectamente() {
        List<CursoMateria> cursosAsociados = new ArrayList<>();
        
        CursoMateria cursoMateria1 = new CursoMateria();
        CursoMateria cursoMateria2 = new CursoMateria();
        
        cursosAsociados.add(cursoMateria1);
        cursosAsociados.add(cursoMateria2);
        
        materia.setCursosAsociados(cursosAsociados);
        
        assertThat(materia.getCursosAsociados()).hasSize(2);
        assertThat(materia.getCursosAsociados()).containsExactly(cursoMateria1, cursoMateria2);
    }

    @Test
    @DisplayName("Debería crear materia completa correctamente")
    void deberiaCrearMateriaCompletaCorrectamente() {
        Integer id = 1;
        String nombreMateria = "Física";
        List<CursoMateria> cursosAsociados = new ArrayList<>();
        
        materia.setId(id);
        materia.setNombreMateria(nombreMateria);
        materia.setCursosAsociados(cursosAsociados);
        
        assertThat(materia.getId()).isEqualTo(id);
        assertThat(materia.getNombreMateria()).isEqualTo(nombreMateria);
        assertThat(materia.getCursosAsociados()).isEqualTo(cursosAsociados);
    }

    @Test
    @DisplayName("Debería permitir nombres de materia con caracteres especiales")
    void deberiaPermitirNombresConCaracteresEspeciales() {
        String nombreConAcentos = "Educación Física";
        materia.setNombreMateria(nombreConAcentos);
        
        assertThat(materia.getNombreMateria()).isEqualTo(nombreConAcentos);
    }

    @Test
    @DisplayName("Debería manejar materias comunes del sistema educativo")
    void deberiaManejarMateriasComunes() {
        String[] materiasComunes = {
            "Matemáticas", "Lenguaje", "Ciencias", "Historia", 
            "Geografía", "Inglés", "Educación Física", "Arte"
        };
        
        for (String nombreMateria : materiasComunes) {
            materia.setNombreMateria(nombreMateria);
            assertThat(materia.getNombreMateria()).isEqualTo(nombreMateria);
            assertThat(materia.getNombreMateria().length()).isLessThanOrEqualTo(30);
        }
    }

    @Test
    @DisplayName("Debería permitir actualizar lista de cursos asociados")
    void deberiaPermitirActualizarListaDeCursosAsociados() {
        // Lista inicial vacía
        List<CursoMateria> cursosIniciales = new ArrayList<>();
        materia.setCursosAsociados(cursosIniciales);
        assertThat(materia.getCursosAsociados()).isEmpty();
        
        // Agregar cursos
        List<CursoMateria> cursosActualizados = new ArrayList<>();
        CursoMateria curso1 = new CursoMateria();
        CursoMateria curso2 = new CursoMateria();
        cursosActualizados.add(curso1);
        cursosActualizados.add(curso2);
        
        materia.setCursosAsociados(cursosActualizados);
        assertThat(materia.getCursosAsociados()).hasSize(2);
    }
}