package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad AnioLectivo")
class AnioLectivoTest {

    private AnioLectivo anioLectivo;

    @BeforeEach
    void setUp() {
        anioLectivo = new AnioLectivo();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        anioLectivo.setId(id);
        
        assertThat(anioLectivo.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener fecha de inicio correctamente")
    void deberiaEstablecerYObtenerFechaInicio() {
        LocalDate fechaInicio = LocalDate.of(2024, 2, 1);
        anioLectivo.setFechaInicio(fechaInicio);
        
        assertThat(anioLectivo.getFechaInicio()).isEqualTo(fechaInicio);
    }

    @Test
    @DisplayName("Debería establecer y obtener fecha final correctamente")
    void deberiaEstablecerYObtenerFechaFinal() {
        LocalDate fechaFinal = LocalDate.of(2024, 11, 30);
        anioLectivo.setFechaFinal(fechaFinal);
        
        assertThat(anioLectivo.getFechaFinal()).isEqualTo(fechaFinal);
    }

    @Test
    @DisplayName("Debería establecer y obtener estado correctamente")
    void deberiaEstablecerYObtenerEstado() {
        String estado = "Activo";
        anioLectivo.setEstado(estado);
        
        assertThat(anioLectivo.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Debería manejar lista de cursos correctamente")
    void deberiaManejarListaDeCursos() {
        List<Curso> cursos = new ArrayList<>();
        anioLectivo.setCursos(cursos);
        
        assertThat(anioLectivo.getCursos()).isEmpty();
    }

    @Test
    @DisplayName("Debería agregar cursos correctamente")
    void deberiaAgregarCursosCorrectamente() {
        List<Curso> cursos = new ArrayList<>();
        
        Curso curso1 = new Curso();
        curso1.setId(1);
        curso1.setNombreCurso("1° Bachillerato");
        
        Curso curso2 = new Curso();
        curso2.setId(2);
        curso2.setNombreCurso("2° Bachillerato");
        
        cursos.add(curso1);
        cursos.add(curso2);
        
        anioLectivo.setCursos(cursos);
        
        assertThat(anioLectivo.getCursos()).hasSize(2);
        assertThat(anioLectivo.getCursos()).containsExactly(curso1, curso2);
    }

    @Test
    @DisplayName("Debería crear año lectivo completo correctamente")
    void deberiaCrearAnioLectivoCompletoCorrectamente() {
        Integer id = 1;
        LocalDate fechaInicio = LocalDate.of(2024, 2, 5);
        LocalDate fechaFinal = LocalDate.of(2024, 12, 20);
        String estado = "Activo";
        List<Curso> cursos = new ArrayList<>();
        
        anioLectivo.setId(id);
        anioLectivo.setFechaInicio(fechaInicio);
        anioLectivo.setFechaFinal(fechaFinal);
        anioLectivo.setEstado(estado);
        anioLectivo.setCursos(cursos);
        
        assertThat(anioLectivo.getId()).isEqualTo(id);
        assertThat(anioLectivo.getFechaInicio()).isEqualTo(fechaInicio);
        assertThat(anioLectivo.getFechaFinal()).isEqualTo(fechaFinal);
        assertThat(anioLectivo.getEstado()).isEqualTo(estado);
        assertThat(anioLectivo.getCursos()).isEqualTo(cursos);
    }

    @Test
    @DisplayName("Debería manejar estados válidos del año lectivo")
    void deberiaManejarEstadosValidosDelAnioLectivo() {
        String[] estadosValidos = {"Activo", "Inactivo", "Finalizado", "Planificado"};
        
        for (String estado : estadosValidos) {
            anioLectivo.setEstado(estado);
            assertThat(anioLectivo.getEstado()).isEqualTo(estado);
        }
    }

    @Test
    @DisplayName("Debería validar período académico típico")
    void deberiaValidarPeriodoAcademicoTipico() {
        // Período académico típico ecuatoriano
        LocalDate inicioSierra = LocalDate.of(2024, 9, 2);
        LocalDate finSierra = LocalDate.of(2025, 7, 4);
        
        anioLectivo.setFechaInicio(inicioSierra);
        anioLectivo.setFechaFinal(finSierra);
        
        assertThat(anioLectivo.getFechaInicio()).isBefore(anioLectivo.getFechaFinal());
        assertThat(anioLectivo.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(anioLectivo.getFechaFinal().getYear()).isEqualTo(2025);
    }

    @Test
    @DisplayName("Debería manejar año lectivo de régimen costa")
    void deberiaManejarAnioLectivoDeRegimenCosta() {
        // Período académico régimen costa
        LocalDate inicioCosta = LocalDate.of(2024, 5, 6);
        LocalDate finCosta = LocalDate.of(2025, 2, 7);
        
        anioLectivo.setFechaInicio(inicioCosta);
        anioLectivo.setFechaFinal(finCosta);
        anioLectivo.setEstado("Activo");
        
        assertThat(anioLectivo.getFechaInicio()).isEqualTo(inicioCosta);
        assertThat(anioLectivo.getFechaFinal()).isEqualTo(finCosta);
        assertThat(anioLectivo.getFechaInicio()).isBefore(anioLectivo.getFechaFinal());
    }

    @Test
    @DisplayName("Debería permitir actualizar fechas del año lectivo")
    void deberiaPermitirActualizarFechasDelAnioLectivo() {
        // Fechas iniciales
        LocalDate fechaInicioOriginal = LocalDate.of(2024, 2, 1);
        LocalDate fechaFinalOriginal = LocalDate.of(2024, 11, 30);
        
        anioLectivo.setFechaInicio(fechaInicioOriginal);
        anioLectivo.setFechaFinal(fechaFinalOriginal);
        
        assertThat(anioLectivo.getFechaInicio()).isEqualTo(fechaInicioOriginal);
        assertThat(anioLectivo.getFechaFinal()).isEqualTo(fechaFinalOriginal);
        
        // Fechas actualizadas
        LocalDate nuevaFechaInicio = LocalDate.of(2024, 2, 5);
        LocalDate nuevaFechaFinal = LocalDate.of(2024, 12, 15);
        
        anioLectivo.setFechaInicio(nuevaFechaInicio);
        anioLectivo.setFechaFinal(nuevaFechaFinal);
        
        assertThat(anioLectivo.getFechaInicio()).isEqualTo(nuevaFechaInicio);
        assertThat(anioLectivo.getFechaFinal()).isEqualTo(nuevaFechaFinal);
    }

    @Test
    @DisplayName("Debería permitir cambiar estado del año lectivo")
    void deberiaPermitirCambiarEstadoDelAnioLectivo() {
        // Estado inicial
        anioLectivo.setEstado("Planificado");
        assertThat(anioLectivo.getEstado()).isEqualTo("Planificado");
        
        // Cambio a activo
        anioLectivo.setEstado("Activo");
        assertThat(anioLectivo.getEstado()).isEqualTo("Activo");
        
        // Cambio a finalizado
        anioLectivo.setEstado("Finalizado");
        assertThat(anioLectivo.getEstado()).isEqualTo("Finalizado");
    }

    @Test
    @DisplayName("Debería permitir actualizar lista de cursos")
    void deberiaPermitirActualizarListaDeCursos() {
        // Lista inicial vacía
        List<Curso> cursosIniciales = new ArrayList<>();
        anioLectivo.setCursos(cursosIniciales);
        assertThat(anioLectivo.getCursos()).isEmpty();
        
        // Agregar cursos
        List<Curso> cursosActualizados = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Curso curso = new Curso();
            curso.setId(i);
            curso.setNombreCurso(i + "° Bachillerato");
            cursosActualizados.add(curso);
        }
        
        anioLectivo.setCursos(cursosActualizados);
        assertThat(anioLectivo.getCursos()).hasSize(5);
    }

    @Test
    @DisplayName("Debería manejar fechas con diferentes años")
    void deberiaManejarFechasConDiferentesAnios() {
        // Año lectivo que cruza años calendario
        LocalDate fechaInicio = LocalDate.of(2024, 9, 1);
        LocalDate fechaFinal = LocalDate.of(2025, 6, 30);
        
        anioLectivo.setFechaInicio(fechaInicio);
        anioLectivo.setFechaFinal(fechaFinal);
        
        assertThat(anioLectivo.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(anioLectivo.getFechaFinal().getYear()).isEqualTo(2025);
        assertThat(anioLectivo.getFechaInicio()).isBefore(anioLectivo.getFechaFinal());
    }
}