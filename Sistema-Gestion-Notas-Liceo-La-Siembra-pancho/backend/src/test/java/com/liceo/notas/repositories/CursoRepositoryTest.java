package com.liceo.notas.repositories;

import com.liceo.notas.entities.Curso;
import com.liceo.notas.entities.AnioLectivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests para CursoRepository")
class CursoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CursoRepository cursoRepository;

    private AnioLectivo anio2024Activo;
    private AnioLectivo anio2023Finalizado;
    private AnioLectivo anio2025Planeado;
    private Curso curso1A;
    private Curso curso1B;
    private Curso curso2A;
    private Curso cursoFinalizado;

    @BeforeEach
    void setUp() {
        // Crear años lectivos
        anio2024Activo = new AnioLectivo();
        anio2024Activo.setFechaInicio(LocalDate.of(2024, 2, 1));
        anio2024Activo.setFechaFinal(LocalDate.of(2024, 11, 30));
        anio2024Activo.setEstado("Activo");
        entityManager.persistAndFlush(anio2024Activo);

        anio2023Finalizado = new AnioLectivo();
        anio2023Finalizado.setFechaInicio(LocalDate.of(2023, 2, 1));
        anio2023Finalizado.setFechaFinal(LocalDate.of(2023, 11, 30));
        anio2023Finalizado.setEstado("Finalizado");
        entityManager.persistAndFlush(anio2023Finalizado);

        anio2025Planeado = new AnioLectivo();
        anio2025Planeado.setFechaInicio(LocalDate.of(2025, 2, 1));
        anio2025Planeado.setFechaFinal(LocalDate.of(2025, 11, 30));
        anio2025Planeado.setEstado("Planeado");
        entityManager.persistAndFlush(anio2025Planeado);

        // Crear cursos para año activo
        curso1A = new Curso();
        curso1A.setNombreCurso("1° Bachillerato A");
        curso1A.setAnioLectivo(anio2024Activo);
        entityManager.persistAndFlush(curso1A);

        curso1B = new Curso();
        curso1B.setNombreCurso("1° Bachillerato B");
        curso1B.setAnioLectivo(anio2024Activo);
        entityManager.persistAndFlush(curso1B);

        curso2A = new Curso();
        curso2A.setNombreCurso("2° Bachillerato A");
        curso2A.setAnioLectivo(anio2024Activo);
        entityManager.persistAndFlush(curso2A);

        // Crear curso para año finalizado
        cursoFinalizado = new Curso();
        cursoFinalizado.setNombreCurso("3° Bachillerato A");
        cursoFinalizado.setAnioLectivo(anio2023Finalizado);
        entityManager.persistAndFlush(cursoFinalizado);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar cursos por ID de año lectivo")
    void deberiaEncontrarCursosPorIdDeAnioLectivo() {
        List<Curso> cursosActivos = cursoRepository.findByAnioLectivoId(anio2024Activo.getId());
        
        assertThat(cursosActivos).hasSize(3);
        assertThat(cursosActivos).extracting("nombreCurso")
            .containsExactlyInAnyOrder("1° Bachillerato A", "1° Bachillerato B", "2° Bachillerato A");
    }

    @Test
    @DisplayName("Debería encontrar cursos por año activo")
    void deberiaEncontrarCursosPorAnioActivo() {
        List<Curso> cursosActivos = cursoRepository.findCursosByAnioActivo();
        
        assertThat(cursosActivos).hasSize(3);
        assertThat(cursosActivos).extracting("nombreCurso")
            .containsExactlyInAnyOrder("1° Bachillerato A", "1° Bachillerato B", "2° Bachillerato A");
    }

    @Test
    @DisplayName("Debería verificar existencia de curso por nombre y año lectivo (ignorando case)")
    void deberiaVerificarExistenciaDeCursoPorNombreYAnioLectivoIgnorandoCase() {
        boolean existe1 = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "1° bachillerato a", anio2024Activo.getId());
        boolean existe2 = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "1° BACHILLERATO A", anio2024Activo.getId());
        boolean noExiste = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "3° Bachillerato A", anio2024Activo.getId());
        
        assertThat(existe1).isTrue();
        assertThat(existe2).isTrue();
        assertThat(noExiste).isFalse();
    }

    @Test
    @DisplayName("Debería verificar existencia excluyendo ID específico")
    void deberiaVerificarExistenciaExcluyendoIdEspecifico() {
        boolean existeExcluyendo = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_IdAndIdNot(
            "1° Bachillerato A", anio2024Activo.getId(), curso1A.getId());
        boolean noExisteExcluyendo = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_IdAndIdNot(
            "1° Bachillerato A", anio2024Activo.getId(), 999);
        
        assertThat(existeExcluyendo).isFalse(); // No existe otro con el mismo nombre
        assertThat(noExisteExcluyendo).isTrue(); // Existe pero no excluimos el correcto
    }

    @Test
    @DisplayName("Debería retornar lista vacía para año lectivo sin cursos")
    void deberiaRetornarListaVaciaParaAnioLectivoSinCursos() {
        List<Curso> cursosPlaneados = cursoRepository.findByAnioLectivoId(anio2025Planeado.getId());
        
        assertThat(cursosPlaneados).isEmpty();
    }

    @Test
    @DisplayName("Debería guardar nuevo curso correctamente")
    void deberiaGuardarNuevoCursoCorrectamente() {
        // Crear nuevo curso
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombreCurso("3° Bachillerato B");
        nuevoCurso.setAnioLectivo(anio2024Activo);
        
        // Guardar
        Curso cursoGuardado = cursoRepository.save(nuevoCurso);
        
        // Verificar que se guardó
        assertThat(cursoGuardado.getId()).isNotNull();
        assertThat(cursoGuardado.getNombreCurso()).isEqualTo("3° Bachillerato B");
        
        // Verificar que aparece en las consultas
        List<Curso> cursosActivos = cursoRepository.findByAnioLectivoId(anio2024Activo.getId());
        assertThat(cursosActivos).hasSize(4); // 3 originales + 1 nuevo
    }

    @Test
    @DisplayName("Debería manejar cursos con diferentes nombres")
    void deberiaManejarCursosConDiferentesNombres() {
        String[] nombresCursos = {
            "1° EGB", "2° EGB", "3° EGB", "4° EGB", "5° EGB",
            "6° EGB", "7° EGB", "8° EGB", "9° EGB", "10° EGB",
            "1° BGU", "2° BGU", "3° BGU"
        };
        
        // Crear cursos con diferentes nombres
        for (String nombreCurso : nombresCursos) {
            Curso curso = new Curso();
            curso.setNombreCurso(nombreCurso);
            curso.setAnioLectivo(anio2024Activo);
            cursoRepository.save(curso);
        }
        
        // Verificar que se guardaron todos
        List<Curso> todosLosCursos = cursoRepository.findByAnioLectivoId(anio2024Activo.getId());
        assertThat(todosLosCursos).hasSize(3 + nombresCursos.length); // originales + nuevos
    }

    @Test
    @DisplayName("Debería manejar cursos con secciones paralelas")
    void deberiaManejarCursosConSeccionesParalelas() {
        String[] seccionesParalelas = {
            "1° A", "1° B", "1° C", "1° D",
            "2° A", "2° B", "2° C",
            "3° A", "3° B"
        };
        
        // Crear cursos con secciones paralelas
        for (String seccion : seccionesParalelas) {
            Curso curso = new Curso();
            curso.setNombreCurso(seccion);
            curso.setAnioLectivo(anio2025Planeado); // Usar año sin cursos
            cursoRepository.save(curso);
        }
        
        // Verificar que se crearon correctamente
        List<Curso> cursosPlaneados = cursoRepository.findByAnioLectivoId(anio2025Planeado.getId());
        assertThat(cursosPlaneados).hasSize(seccionesParalelas.length);
        
        // Verificar que no están en año activo
        List<Curso> cursosActivosQuery = cursoRepository.findCursosByAnioActivo();
        assertThat(cursosActivosQuery).hasSize(3); // Solo los originales del año activo
    }

    @Test
    @DisplayName("Debería actualizar nombre de curso correctamente")
    void deberiaActualizarNombreDeCursoCorrectamente() {
        // Obtener curso existente
        Optional<Curso> cursoExistente = cursoRepository.findById(curso1A.getId());
        assertThat(cursoExistente).isPresent();
        
        Curso curso = cursoExistente.get();
        String nombreOriginal = curso.getNombreCurso();
        
        // Actualizar nombre
        curso.setNombreCurso("1° Bachillerato Internacional A");
        Curso cursoActualizado = cursoRepository.save(curso);
        
        // Verificar actualización
        assertThat(cursoActualizado.getNombreCurso()).isEqualTo("1° Bachillerato Internacional A");
        assertThat(cursoActualizado.getNombreCurso()).isNotEqualTo(nombreOriginal);
        
        // Verificar en consulta
        List<Curso> cursosActivos = cursoRepository.findCursosByAnioActivo();
        assertThat(cursosActivos).extracting("nombreCurso")
            .contains("1° Bachillerato Internacional A");
    }

    @Test
    @DisplayName("Debería eliminar curso correctamente")
    void deberiaEliminarCursoCorrectamente() {
        Integer cursoId = curso1B.getId();
        
        // Verificar que existe
        assertThat(cursoRepository.findById(cursoId)).isPresent();
        
        // Eliminar
        cursoRepository.deleteById(cursoId);
        
        // Verificar que se eliminó
        assertThat(cursoRepository.findById(cursoId)).isEmpty();
        
        // Verificar que no aparece en consultas
        List<Curso> cursosActivos = cursoRepository.findCursosByAnioActivo();
        assertThat(cursosActivos).hasSize(2); // 3 - 1 eliminado
    }

    @Test
    @DisplayName("Debería manejar cursos especializados de bachillerato")
    void deberiaManejarCursosEspecializadosDeBachillerato() {
        String[] cursosEspecializados = {
            "1° Bachillerato Ciencias",
            "2° Bachillerato Técnico",
            "3° Bachillerato Internacional",
            "1° Bachillerato Artístico",
            "2° Bachillerato Deportivo"
        };
        
        // Crear cursos especializados
        for (String nombreCurso : cursosEspecializados) {
            Curso curso = new Curso();
            curso.setNombreCurso(nombreCurso);
            curso.setAnioLectivo(anio2024Activo);
            cursoRepository.save(curso);
        }
        
        // Verificar que se crearon
        List<Curso> cursosActivos = cursoRepository.findByAnioLectivoId(anio2024Activo.getId());
        assertThat(cursosActivos).hasSize(3 + cursosEspecializados.length);
        
        // Verificar nombres específicos
        assertThat(cursosActivos).extracting("nombreCurso")
            .contains("1° Bachillerato Ciencias", "2° Bachillerato Técnico");
    }

    @Test
    @DisplayName("Debería manejar múltiples años lectivos con cursos")
    void deberiaManejarMultiplesAniosLectivosConCursos() {
        // Agregar cursos al año planeado
        Curso cursoPlaneado1 = new Curso();
        cursoPlaneado1.setNombreCurso("1° A");
        cursoPlaneado1.setAnioLectivo(anio2025Planeado);
        cursoRepository.save(cursoPlaneado1);

        Curso cursoPlaneado2 = new Curso();
        cursoPlaneado2.setNombreCurso("2° A");
        cursoPlaneado2.setAnioLectivo(anio2025Planeado);
        cursoRepository.save(cursoPlaneado2);
        
        // Verificar cursos por año específico
        List<Curso> cursos2024 = cursoRepository.findByAnioLectivoId(anio2024Activo.getId());
        List<Curso> cursos2023 = cursoRepository.findByAnioLectivoId(anio2023Finalizado.getId());
        List<Curso> cursos2025 = cursoRepository.findByAnioLectivoId(anio2025Planeado.getId());
        
        assertThat(cursos2024).hasSize(3);
        assertThat(cursos2023).hasSize(1);
        assertThat(cursos2025).hasSize(2);
        
        // Verificar que solo los activos aparecen en la consulta especial
        List<Curso> soloActivos = cursoRepository.findCursosByAnioActivo();
        assertThat(soloActivos).hasSize(3);
    }

    @Test
    @DisplayName("Debería cambiar año lectivo de curso correctamente")
    void deberiaCambiarAnioLectivoDeCursoCorrectamente() {
        // Mover curso del año finalizado al año activo
        Optional<Curso> cursoExistente = cursoRepository.findById(cursoFinalizado.getId());
        assertThat(cursoExistente).isPresent();
        
        Curso curso = cursoExistente.get();
        Integer anioOriginal = curso.getAnioLectivo().getId();
        
        // Cambiar año lectivo
        curso.setAnioLectivo(anio2024Activo);
        Curso cursoActualizado = cursoRepository.save(curso);
        
        // Verificar cambio
        assertThat(cursoActualizado.getAnioLectivo().getId()).isEqualTo(anio2024Activo.getId());
        assertThat(cursoActualizado.getAnioLectivo().getId()).isNotEqualTo(anioOriginal);
        
        // Verificar en consultas
        List<Curso> cursosActivos = cursoRepository.findCursosByAnioActivo();
        assertThat(cursosActivos).hasSize(4); // 3 + 1 movido
        
        List<Curso> cursosFinalizados = cursoRepository.findByAnioLectivoId(anio2023Finalizado.getId());
        assertThat(cursosFinalizados).isEmpty(); // Ya no hay cursos en año finalizado
    }

    @Test
    @DisplayName("Debería contar cursos correctamente")
    void deberiaContarCursosCorrectamente() {
        long totalCursos = cursoRepository.count();
        assertThat(totalCursos).isEqualTo(4); // 4 cursos del setUp
        
        // Agregar un curso más
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombreCurso("4° Bachillerato A");
        nuevoCurso.setAnioLectivo(anio2024Activo);
        cursoRepository.save(nuevoCurso);
        
        // Verificar nuevo conteo
        long nuevoTotal = cursoRepository.count();
        assertThat(nuevoTotal).isEqualTo(5);
    }

    @Test
    @DisplayName("Debería encontrar todos los cursos")
    void deberiaEncontrarTodosLosCursos() {
        List<Curso> todosLosCursos = cursoRepository.findAll();
        
        assertThat(todosLosCursos).hasSize(4);
        assertThat(todosLosCursos).extracting("nombreCurso")
            .containsExactlyInAnyOrder(
                "1° Bachillerato A", "1° Bachillerato B", 
                "2° Bachillerato A", "3° Bachillerato A"
            );
    }

    @Test
    @DisplayName("Debería existir por ID correctamente")
    void deberiaExistirPorIdCorrectamente() {
        // Verificar que existen los cursos del setUp
        assertThat(cursoRepository.existsById(curso1A.getId())).isTrue();
        assertThat(cursoRepository.existsById(curso1B.getId())).isTrue();
        assertThat(cursoRepository.existsById(curso2A.getId())).isTrue();
        assertThat(cursoRepository.existsById(cursoFinalizado.getId())).isTrue();
        
        // Verificar que no existe ID inexistente
        assertThat(cursoRepository.existsById(999)).isFalse();
    }

    @Test
    @DisplayName("Debería manejar validación de nombres únicos por año")
    void deberiaManejarValidacionDeNombresUnicosPorAnio() {
        // Verificar que no existe duplicado en el mismo año
        boolean duplicadoMismoAnio = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "1° Bachillerato A", anio2024Activo.getId());
        assertThat(duplicadoMismoAnio).isTrue();
        
        // Verificar que puede existir el mismo nombre en diferente año
        boolean mismoNombreDiferenteAnio = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "1° Bachillerato A", anio2025Planeado.getId());
        assertThat(mismoNombreDiferenteAnio).isFalse();
        
        // Crear curso con mismo nombre en año diferente
        Curso cursoMismoNombre = new Curso();
        cursoMismoNombre.setNombreCurso("1° Bachillerato A");
        cursoMismoNombre.setAnioLectivo(anio2025Planeado);
        cursoRepository.save(cursoMismoNombre);
        
        // Verificar que ahora existe en ambos años
        boolean existeEnNuevoAnio = cursoRepository.existsByNombreCursoIgnoreCaseAndAnioLectivo_Id(
            "1° Bachillerato A", anio2025Planeado.getId());
        assertThat(existeEnNuevoAnio).isTrue();
    }
}