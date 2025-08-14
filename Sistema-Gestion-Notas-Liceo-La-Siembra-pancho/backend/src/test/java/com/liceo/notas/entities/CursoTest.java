package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad Curso")
class CursoTest {

    private Curso curso;
    private AnioLectivo anioLectivo;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        
        anioLectivo = new AnioLectivo();
        anioLectivo.setId(1);
        anioLectivo.setFechaInicio(LocalDate.of(2024, 1, 15));
        anioLectivo.setFechaFinal(LocalDate.of(2024, 12, 15));
        anioLectivo.setEstado("Activo");
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        curso.setId(id);
        
        assertThat(curso.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre de curso correctamente")
    void deberiaEstablecerYObtenerNombreCurso() {
        String nombreCurso = "1° Bachillerato";
        curso.setNombreCurso(nombreCurso);
        
        assertThat(curso.getNombreCurso()).isEqualTo(nombreCurso);
    }

    @Test
    @DisplayName("Debería manejar nombres de curso cortos")
    void deberiaManejarNombresDeCursoCortos() {
        String nombreCorto = "1°A";
        curso.setNombreCurso(nombreCorto);
        
        assertThat(curso.getNombreCurso()).isEqualTo(nombreCorto);
        assertThat(curso.getNombreCurso().length()).isLessThanOrEqualTo(15);
    }

    @Test
    @DisplayName("Debería manejar nombres de curso con longitud máxima")
    void deberiaManejarNombresDeCursoConLongitudMaxima() {
        String nombreMaximo = "3° Bachillerato"; // 15 caracteres
        curso.setNombreCurso(nombreMaximo);
        
        assertThat(curso.getNombreCurso()).isEqualTo(nombreMaximo);
        assertThat(curso.getNombreCurso().length()).isEqualTo(15);
    }

    @Test
    @DisplayName("Debería establecer y obtener año lectivo correctamente")
    void deberiaEstablecerYObtenerAnioLectivo() {
        curso.setAnioLectivo(anioLectivo);
        
        assertThat(curso.getAnioLectivo()).isEqualTo(anioLectivo);
        assertThat(curso.getAnioLectivo().getId()).isEqualTo(1);
        assertThat(curso.getAnioLectivo().getEstado()).isEqualTo("Activo");
    }

    @Test
    @DisplayName("Debería manejar lista de materias asociadas correctamente")
    void deberiaManejarListaDeMateriasAsociadas() {
        List<CursoMateria> materiasAsociadas = new ArrayList<>();
        curso.setMateriasAsociadas(materiasAsociadas);
        
        assertThat(curso.getMateriasAsociadas()).isEmpty();
    }

    @Test
    @DisplayName("Debería agregar materias asociadas correctamente")
    void deberiaAgregarMateriasAsociadasCorrectamente() {
        List<CursoMateria> materiasAsociadas = new ArrayList<>();
        
        CursoMateria cursoMateria1 = new CursoMateria();
        CursoMateria cursoMateria2 = new CursoMateria();
        
        materiasAsociadas.add(cursoMateria1);
        materiasAsociadas.add(cursoMateria2);
        
        curso.setMateriasAsociadas(materiasAsociadas);
        
        assertThat(curso.getMateriasAsociadas()).hasSize(2);
        assertThat(curso.getMateriasAsociadas()).containsExactly(cursoMateria1, cursoMateria2);
    }

    @Test
    @DisplayName("Debería crear curso completo correctamente")
    void deberiaCrearCursoCompletoCorrectamente() {
        Integer id = 1;
        String nombreCurso = "2° Secundaria";
        List<CursoMateria> materiasAsociadas = new ArrayList<>();
        
        curso.setId(id);
        curso.setNombreCurso(nombreCurso);
        curso.setAnioLectivo(anioLectivo);
        curso.setMateriasAsociadas(materiasAsociadas);
        
        assertThat(curso.getId()).isEqualTo(id);
        assertThat(curso.getNombreCurso()).isEqualTo(nombreCurso);
        assertThat(curso.getAnioLectivo()).isEqualTo(anioLectivo);
        assertThat(curso.getMateriasAsociadas()).isEqualTo(materiasAsociadas);
    }

    @Test
    @DisplayName("Debería manejar cursos comunes del sistema educativo")
    void deberiaManejarCursosComunesDelSistemaEducativo() {
        String[] cursosComunes = {
            "1° Primaria", "2° Primaria", "3° Primaria",
            "1° Secundaria", "2° Secundaria", "3° Secundaria",
            "1° Bachillerato", "2° Bachillerato", "3° Bachillerato"
        };
        
        for (String nombreCurso : cursosComunes) {
            curso.setNombreCurso(nombreCurso);
            assertThat(curso.getNombreCurso()).isEqualTo(nombreCurso);
            assertThat(curso.getNombreCurso().length()).isLessThanOrEqualTo(15);
        }
    }

    @Test
    @DisplayName("Debería permitir actualizar año lectivo")
    void deberiaPermitirActualizarAnioLectivo() {
        // Año lectivo inicial
        curso.setAnioLectivo(anioLectivo);
        assertThat(curso.getAnioLectivo().getId()).isEqualTo(1);
        
        // Nuevo año lectivo
        AnioLectivo nuevoAnioLectivo = new AnioLectivo();
        nuevoAnioLectivo.setId(2);
        nuevoAnioLectivo.setFechaInicio(LocalDate.of(2025, 1, 15));
        nuevoAnioLectivo.setFechaFinal(LocalDate.of(2025, 12, 15));
        nuevoAnioLectivo.setEstado("Activo");
        
        curso.setAnioLectivo(nuevoAnioLectivo);
        assertThat(curso.getAnioLectivo().getId()).isEqualTo(2);
        assertThat(curso.getAnioLectivo().getFechaInicio()).isEqualTo(LocalDate.of(2025, 1, 15));
    }

    @Test
    @DisplayName("Debería permitir actualizar lista de materias asociadas")
    void deberiaPermitirActualizarListaDeMateriasAsociadas() {
        // Lista inicial vacía
        List<CursoMateria> materiasIniciales = new ArrayList<>();
        curso.setMateriasAsociadas(materiasIniciales);
        assertThat(curso.getMateriasAsociadas()).isEmpty();
        
        // Agregar materias
        List<CursoMateria> materiasActualizadas = new ArrayList<>();
        CursoMateria materia1 = new CursoMateria();
        CursoMateria materia2 = new CursoMateria();
        CursoMateria materia3 = new CursoMateria();
        
        materiasActualizadas.add(materia1);
        materiasActualizadas.add(materia2);
        materiasActualizadas.add(materia3);
        
        curso.setMateriasAsociadas(materiasActualizadas);
        assertThat(curso.getMateriasAsociadas()).hasSize(3);
    }

    @Test
    @DisplayName("Debería manejar cursos con secciones")
    void deberiaManejarCursosConSecciones() {
        String[] cursosConSecciones = {
            "1°A", "1°B", "1°C",
            "2°A", "2°B", 
            "3°A", "3°B"
        };
        
        for (String nombreCurso : cursosConSecciones) {
            curso.setNombreCurso(nombreCurso);
            assertThat(curso.getNombreCurso()).isEqualTo(nombreCurso);
            assertThat(curso.getNombreCurso().length()).isLessThanOrEqualTo(15);
        }
    }
}