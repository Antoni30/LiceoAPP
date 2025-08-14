package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad CursoMateria")
class CursoMateriaTest {

    private CursoMateria cursoMateria;
    private Curso curso;
    private Materia materia;
    private AnioLectivo anioLectivo;

    @BeforeEach
    void setUp() {
        cursoMateria = new CursoMateria();
        
        // Configurar año lectivo
        anioLectivo = new AnioLectivo();
        anioLectivo.setId(1);
        anioLectivo.setFechaInicio(LocalDate.of(2024, 2, 1));
        anioLectivo.setFechaFinal(LocalDate.of(2024, 11, 30));
        anioLectivo.setEstado("Activo");
        
        // Configurar curso
        curso = new Curso();
        curso.setId(1);
        curso.setNombreCurso("1° Bachillerato");
        curso.setAnioLectivo(anioLectivo);
        
        // Configurar materia
        materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Matemáticas");
    }

    @Test
    @DisplayName("Debería establecer y obtener curso correctamente")
    void deberiaEstablecerYObtenerCurso() {
        cursoMateria.setCurso(curso);
        
        assertThat(cursoMateria.getCurso()).isEqualTo(curso);
        assertThat(cursoMateria.getCurso().getId()).isEqualTo(1);
        assertThat(cursoMateria.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
    }

    @Test
    @DisplayName("Debería establecer y obtener materia correctamente")
    void deberiaEstablecerYObtenerMateria() {
        cursoMateria.setMateria(materia);
        
        assertThat(cursoMateria.getMateria()).isEqualTo(materia);
        assertThat(cursoMateria.getMateria().getId()).isEqualTo(1);
        assertThat(cursoMateria.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
    }

    @Test
    @DisplayName("Debería crear relación curso-materia completa correctamente")
    void deberiaCrearRelacionCursoMateriaCompletaCorrectamente() {
        cursoMateria.setCurso(curso);
        cursoMateria.setMateria(materia);
        
        assertThat(cursoMateria.getCurso()).isEqualTo(curso);
        assertThat(cursoMateria.getMateria()).isEqualTo(materia);
        assertThat(cursoMateria.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
        assertThat(cursoMateria.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
    }

    @Test
    @DisplayName("Debería manejar múltiples materias para un curso")
    void deberiaManejarMultiplesMateriasParaUnCurso() {
        // Matemáticas
        Materia matematicas = new Materia();
        matematicas.setId(1);
        matematicas.setNombreMateria("Matemáticas");
        
        CursoMateria cursoMatematicas = new CursoMateria();
        cursoMatematicas.setCurso(curso);
        cursoMatematicas.setMateria(matematicas);
        
        // Lenguaje
        Materia lenguaje = new Materia();
        lenguaje.setId(2);
        lenguaje.setNombreMateria("Lenguaje");
        
        CursoMateria cursoLenguaje = new CursoMateria();
        cursoLenguaje.setCurso(curso);
        cursoLenguaje.setMateria(lenguaje);
        
        // Verificaciones
        assertThat(cursoMatematicas.getCurso()).isEqualTo(curso);
        assertThat(cursoMatematicas.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
        
        assertThat(cursoLenguaje.getCurso()).isEqualTo(curso);
        assertThat(cursoLenguaje.getMateria().getNombreMateria()).isEqualTo("Lenguaje");
        
        // Ambas relaciones apuntan al mismo curso pero diferentes materias
        assertThat(cursoMatematicas.getCurso().getId()).isEqualTo(cursoLenguaje.getCurso().getId());
        assertThat(cursoMatematicas.getMateria().getId()).isNotEqualTo(cursoLenguaje.getMateria().getId());
    }

    @Test
    @DisplayName("Debería manejar una materia asignada a múltiples cursos")
    void deberiaManejarUnaMateriaAsignadaAMultiplesCursos() {
        // Curso 2° Bachillerato
        Curso segundoBachillerato = new Curso();
        segundoBachillerato.setId(2);
        segundoBachillerato.setNombreCurso("2° Bachillerato");
        segundoBachillerato.setAnioLectivo(anioLectivo);
        
        // Matemáticas para 1° Bachillerato
        CursoMateria primeroBachMatematicas = new CursoMateria();
        primeroBachMatematicas.setCurso(curso);
        primeroBachMatematicas.setMateria(materia);
        
        // Matemáticas para 2° Bachillerato
        CursoMateria segundoBachMatematicas = new CursoMateria();
        segundoBachMatematicas.setCurso(segundoBachillerato);
        segundoBachMatematicas.setMateria(materia);
        
        // Verificaciones
        assertThat(primeroBachMatematicas.getMateria()).isEqualTo(materia);
        assertThat(segundoBachMatematicas.getMateria()).isEqualTo(materia);
        
        assertThat(primeroBachMatematicas.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
        assertThat(segundoBachMatematicas.getCurso().getNombreCurso()).isEqualTo("2° Bachillerato");
        
        // Ambas relaciones apuntan a la misma materia pero diferentes cursos
        assertThat(primeroBachMatematicas.getMateria().getId()).isEqualTo(segundoBachMatematicas.getMateria().getId());
        assertThat(primeroBachMatematicas.getCurso().getId()).isNotEqualTo(segundoBachMatematicas.getCurso().getId());
    }

    @Test
    @DisplayName("Debería manejar materias típicas del bachillerato")
    void deberiaManejarMateriasTipicasDelBachillerato() {
        String[] materiasComunes = {
            "Matemáticas", "Lenguaje", "Ciencias Naturales", "Estudios Sociales",
            "Inglés", "Educación Física", "Educación Cultural y Artística"
        };
        
        for (int i = 0; i < materiasComunes.length; i++) {
            Materia nuevaMateria = new Materia();
            nuevaMateria.setId(i + 1);
            nuevaMateria.setNombreMateria(materiasComunes[i]);
            
            CursoMateria nuevoCursoMateria = new CursoMateria();
            nuevoCursoMateria.setCurso(curso);
            nuevoCursoMateria.setMateria(nuevaMateria);
            
            assertThat(nuevoCursoMateria.getCurso()).isEqualTo(curso);
            assertThat(nuevoCursoMateria.getMateria().getNombreMateria()).isEqualTo(materiasComunes[i]);
        }
    }

    @Test
    @DisplayName("Debería permitir cambiar curso en la relación")
    void deberiaPermitirCambiarCursoEnLaRelacion() {
        // Configurar relación inicial
        cursoMateria.setCurso(curso);
        cursoMateria.setMateria(materia);
        
        assertThat(cursoMateria.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
        
        // Cambiar a otro curso
        Curso nuevoCurso = new Curso();
        nuevoCurso.setId(2);
        nuevoCurso.setNombreCurso("3° Bachillerato");
        nuevoCurso.setAnioLectivo(anioLectivo);
        
        cursoMateria.setCurso(nuevoCurso);
        
        assertThat(cursoMateria.getCurso()).isEqualTo(nuevoCurso);
        assertThat(cursoMateria.getCurso().getNombreCurso()).isEqualTo("3° Bachillerato");
        assertThat(cursoMateria.getMateria()).isEqualTo(materia); // La materia se mantiene
    }

    @Test
    @DisplayName("Debería permitir cambiar materia en la relación")
    void deberiaPermitirCambiarMateriaEnLaRelacion() {
        // Configurar relación inicial
        cursoMateria.setCurso(curso);
        cursoMateria.setMateria(materia);
        
        assertThat(cursoMateria.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
        
        // Cambiar a otra materia
        Materia nuevaMateria = new Materia();
        nuevaMateria.setId(2);
        nuevaMateria.setNombreMateria("Física");
        
        cursoMateria.setMateria(nuevaMateria);
        
        assertThat(cursoMateria.getMateria()).isEqualTo(nuevaMateria);
        assertThat(cursoMateria.getMateria().getNombreMateria()).isEqualTo("Física");
        assertThat(cursoMateria.getCurso()).isEqualTo(curso); // El curso se mantiene
    }

    @Test
    @DisplayName("Debería validar integridad de la relación curso-materia")
    void deberiaValidarIntegridadDeLaRelacionCursoMateria() {
        cursoMateria.setCurso(curso);
        cursoMateria.setMateria(materia);
        
        // Verificar que tanto curso como materia están presentes
        assertThat(cursoMateria.getCurso()).isNotNull();
        assertThat(cursoMateria.getMateria()).isNotNull();
        
        // Verificar que tienen IDs válidos
        assertThat(cursoMateria.getCurso().getId()).isNotNull();
        assertThat(cursoMateria.getMateria().getId()).isNotNull();
        
        // Verificar que tienen nombres válidos
        assertThat(cursoMateria.getCurso().getNombreCurso()).isNotEmpty();
        assertThat(cursoMateria.getMateria().getNombreMateria()).isNotEmpty();
        
        // Verificar que el curso tiene un año lectivo asociado
        assertThat(cursoMateria.getCurso().getAnioLectivo()).isNotNull();
        assertThat(cursoMateria.getCurso().getAnioLectivo().getEstado()).isEqualTo("Activo");
    }
}