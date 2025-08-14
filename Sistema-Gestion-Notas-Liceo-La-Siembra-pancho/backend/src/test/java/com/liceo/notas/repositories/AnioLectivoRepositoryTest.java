package com.liceo.notas.repositories;

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
@DisplayName("Tests para AnioLectivoRepository")
class AnioLectivoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

    private AnioLectivo anio2023;
    private AnioLectivo anio2024;
    private AnioLectivo anio2025;

    @BeforeEach
    void setUp() {
        // Año lectivo 2023 (finalizado)
        anio2023 = new AnioLectivo();
        anio2023.setFechaInicio(LocalDate.of(2023, 2, 1));
        anio2023.setFechaFinal(LocalDate.of(2023, 11, 30));
        anio2023.setEstado("Finalizado");
        entityManager.persistAndFlush(anio2023);

        // Año lectivo 2024 (activo)
        anio2024 = new AnioLectivo();
        anio2024.setFechaInicio(LocalDate.of(2024, 2, 1));
        anio2024.setFechaFinal(LocalDate.of(2024, 11, 30));
        anio2024.setEstado("Activo");
        entityManager.persistAndFlush(anio2024);

        // Año lectivo 2025 (planeado)
        anio2025 = new AnioLectivo();
        anio2025.setFechaInicio(LocalDate.of(2025, 2, 1));
        anio2025.setFechaFinal(LocalDate.of(2025, 11, 30));
        anio2025.setEstado("Planeado");
        entityManager.persistAndFlush(anio2025);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar años lectivos por estado")
    void deberiaEncontrarAniosLectivosPorEstado() {
        List<AnioLectivo> aniosActivos = anioLectivoRepository.findByEstado("Activo");
        List<AnioLectivo> aniosFinalizados = anioLectivoRepository.findByEstado("Finalizado");
        List<AnioLectivo> aniosPlaneados = anioLectivoRepository.findByEstado("Planeado");

        assertThat(aniosActivos).hasSize(1);
        assertThat(aniosActivos.get(0).getId()).isEqualTo(anio2024.getId());

        assertThat(aniosFinalizados).hasSize(1);
        assertThat(aniosFinalizados.get(0).getId()).isEqualTo(anio2023.getId());

        assertThat(aniosPlaneados).hasSize(1);
        assertThat(aniosPlaneados.get(0).getId()).isEqualTo(anio2025.getId());
    }

    @Test
    @DisplayName("Debería retornar lista vacía para estado inexistente")
    void deberiaRetornarListaVaciaParaEstadoInexistente() {
        List<AnioLectivo> aniosInexistentes = anioLectivoRepository.findByEstado("Cancelado");
        
        assertThat(aniosInexistentes).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar años lectivos conflictivos con rangos superpuestos")
    void deberiaEncontrarAniosLectivosConflictivosConRangosSuperpuestos() {
        // Buscar conflictos con fechas que se superponen con 2024
        LocalDate fechaInicioConflicto = LocalDate.of(2024, 6, 1); // Medio del año 2024
        LocalDate fechaFinalConflicto = LocalDate.of(2024, 12, 31); // Se superpone con el final

        List<AnioLectivo> aniosConflictivos = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicioConflicto, fechaFinalConflicto);

        assertThat(aniosConflictivos).hasSize(1);
        assertThat(aniosConflictivos.get(0).getId()).isEqualTo(anio2024.getId());
    }

    @Test
    @DisplayName("Debería encontrar múltiples años lectivos conflictivos")
    void deberiaEncontrarMultiplesAniosLectivosConflictivos() {
        // Buscar conflictos con un rango que abarca múltiples años
        LocalDate fechaInicioAmplio = LocalDate.of(2023, 6, 1);
        LocalDate fechaFinalAmplio = LocalDate.of(2024, 6, 1);

        List<AnioLectivo> aniosConflictivos = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicioAmplio, fechaFinalAmplio);

        assertThat(aniosConflictivos).hasSize(2);
        assertThat(aniosConflictivos).extracting("id")
            .containsExactlyInAnyOrder(anio2023.getId(), anio2024.getId());
    }

    @Test
    @DisplayName("Debería no encontrar conflictos con rangos no superpuestos")
    void deberiaNoEncontrarConflictosConRangosNoSuperpuestos() {
        // Fechas que no se superponen con ningún año existente
        LocalDate fechaInicioSinConflicto = LocalDate.of(2026, 2, 1);
        LocalDate fechaFinalSinConflicto = LocalDate.of(2026, 11, 30);

        List<AnioLectivo> aniosConflictivos = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicioSinConflicto, fechaFinalSinConflicto);

        assertThat(aniosConflictivos).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar diferentes regímenes académicos")
    void deberiaManejarDiferentesRegimenesAcademicos() {
        // Año lectivo régimen sierra
        AnioLectivo anioSierra = new AnioLectivo();
        anioSierra.setFechaInicio(LocalDate.of(2024, 9, 2));
        anioSierra.setFechaFinal(LocalDate.of(2025, 7, 4));
        anioSierra.setEstado("Activo");
        anioLectivoRepository.save(anioSierra);

        // Año lectivo régimen costa
        AnioLectivo anioCosta = new AnioLectivo();
        anioCosta.setFechaInicio(LocalDate.of(2024, 5, 6));
        anioCosta.setFechaFinal(LocalDate.of(2025, 2, 7));
        anioCosta.setEstado("Activo");
        anioLectivoRepository.save(anioCosta);

        // Verificar que ambos están activos
        List<AnioLectivo> aniosActivos = anioLectivoRepository.findByEstado("Activo");
        assertThat(aniosActivos).hasSize(3); // El original 2024 + sierra + costa

        // Verificar fechas específicas
        assertThat(anioSierra.getFechaInicio().getMonthValue()).isEqualTo(9); // Septiembre
        assertThat(anioCosta.getFechaInicio().getMonthValue()).isEqualTo(5);  // Mayo
    }

    @Test
    @DisplayName("Debería guardar nuevo año lectivo correctamente")
    void deberiaGuardarNuevoAnioLectivoCorrectamente() {
        // Crear nuevo año lectivo
        AnioLectivo nuevoAnio = new AnioLectivo();
        nuevoAnio.setFechaInicio(LocalDate.of(2026, 2, 1));
        nuevoAnio.setFechaFinal(LocalDate.of(2026, 11, 30));
        nuevoAnio.setEstado("Planeado");

        // Guardar
        AnioLectivo anioGuardado = anioLectivoRepository.save(nuevoAnio);

        // Verificar que se guardó
        assertThat(anioGuardado.getId()).isNotNull();
        assertThat(anioGuardado.getEstado()).isEqualTo("Planeado");

        // Verificar que se puede encontrar
        Optional<AnioLectivo> anioEncontrado = anioLectivoRepository.findById(anioGuardado.getId());
        assertThat(anioEncontrado).isPresent();
        assertThat(anioEncontrado.get().getFechaInicio()).isEqualTo(LocalDate.of(2026, 2, 1));
    }

    @Test
    @DisplayName("Debería actualizar estado de año lectivo correctamente")
    void deberiaActualizarEstadoDeAnioLectivoCorrectamente() {
        // Obtener año existente
        Optional<AnioLectivo> anioExistente = anioLectivoRepository.findById(anio2025.getId());
        assertThat(anioExistente).isPresent();

        AnioLectivo anio = anioExistente.get();
        String estadoOriginal = anio.getEstado();

        // Actualizar estado
        anio.setEstado("Activo");
        AnioLectivo anioActualizado = anioLectivoRepository.save(anio);

        // Verificar actualización
        assertThat(anioActualizado.getEstado()).isEqualTo("Activo");
        assertThat(anioActualizado.getEstado()).isNotEqualTo(estadoOriginal);

        // Verificar en base de datos
        List<AnioLectivo> aniosActivos = anioLectivoRepository.findByEstado("Activo");
        assertThat(aniosActivos).hasSize(2); // 2024 + 2025 actualizado
    }

    @Test
    @DisplayName("Debería eliminar año lectivo correctamente")
    void deberiaEliminarAnioLectivoCorrectamente() {
        Integer anioId = anio2025.getId();

        // Verificar que existe
        assertThat(anioLectivoRepository.findById(anioId)).isPresent();

        // Eliminar
        anioLectivoRepository.deleteById(anioId);

        // Verificar que se eliminó
        assertThat(anioLectivoRepository.findById(anioId)).isEmpty();

        // Verificar que ya no está en la lista por estado
        List<AnioLectivo> aniosPlaneados = anioLectivoRepository.findByEstado("Planeado");
        assertThat(aniosPlaneados).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar estados múltiples correctamente")
    void deberiaManejarEstadosMultiplesCorrectamente() {
        // Crear años con diferentes estados
        AnioLectivo anioSuspendido = new AnioLectivo();
        anioSuspendido.setFechaInicio(LocalDate.of(2022, 2, 1));
        anioSuspendido.setFechaFinal(LocalDate.of(2022, 11, 30));
        anioSuspendido.setEstado("Suspendido");
        anioLectivoRepository.save(anioSuspendido);

        AnioLectivo anioCancelado = new AnioLectivo();
        anioCancelado.setFechaInicio(LocalDate.of(2021, 2, 1));
        anioCancelado.setFechaFinal(LocalDate.of(2021, 11, 30));
        anioCancelado.setEstado("Cancelado");
        anioLectivoRepository.save(anioCancelado);

        // Verificar estados específicos
        List<AnioLectivo> aniosSuspendidos = anioLectivoRepository.findByEstado("Suspendido");
        List<AnioLectivo> aniosCancelados = anioLectivoRepository.findByEstado("Cancelado");

        assertThat(aniosSuspendidos).hasSize(1);
        assertThat(aniosCancelados).hasSize(1);

        assertThat(aniosSuspendidos.get(0).getFechaInicio().getYear()).isEqualTo(2022);
        assertThat(aniosCancelados.get(0).getFechaInicio().getYear()).isEqualTo(2021);
    }

    @Test
    @DisplayName("Debería encontrar conflictos con fechas exactas en los límites")
    void deberiaEncontrarConflictosConFechasExactasEnLosLimites() {
        // Caso 1: Fecha inicio exacta con fecha final de año existente
        LocalDate fechaInicioLimite = anio2024.getFechaFinal(); // 2024-11-30
        LocalDate fechaFinalLimite = LocalDate.of(2024, 12, 31);

        List<AnioLectivo> conflictos1 = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicioLimite, fechaFinalLimite);

        assertThat(conflictos1).hasSize(1);
        assertThat(conflictos1.get(0).getId()).isEqualTo(anio2024.getId());

        // Caso 2: Fecha final exacta con fecha inicio de año existente
        LocalDate fechaInicio2 = LocalDate.of(2024, 1, 1);
        LocalDate fechaFinal2 = anio2024.getFechaInicio(); // 2024-02-01

        List<AnioLectivo> conflictos2 = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicio2, fechaFinal2);

        assertThat(conflictos2).hasSize(1);
        assertThat(conflictos2.get(0).getId()).isEqualTo(anio2024.getId());
    }

    @Test
    @DisplayName("Debería manejar años lectivos que cruzan años calendario")
    void deberiaManejarAniosLectivosQueCruzanAniosCalendario() {
        // Año lectivo que cruza de 2024 a 2025
        AnioLectivo anioCruzado = new AnioLectivo();
        anioCruzado.setFechaInicio(LocalDate.of(2024, 9, 1));
        anioCruzado.setFechaFinal(LocalDate.of(2025, 6, 30));
        anioCruzado.setEstado("Activo");
        anioLectivoRepository.save(anioCruzado);

        // Verificar que se guardó correctamente
        Optional<AnioLectivo> anioEncontrado = anioLectivoRepository.findById(anioCruzado.getId());
        assertThat(anioEncontrado).isPresent();

        AnioLectivo anio = anioEncontrado.get();
        assertThat(anio.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(anio.getFechaFinal().getYear()).isEqualTo(2025);

        // Verificar que está en los años activos
        List<AnioLectivo> aniosActivos = anioLectivoRepository.findByEstado("Activo");
        assertThat(aniosActivos).hasSize(2); // 2024 + cruzado
    }

    @Test
    @DisplayName("Debería contar años lectivos correctamente")
    void deberiaContarAniosLectivosCorrectamente() {
        long totalAnios = anioLectivoRepository.count();
        assertThat(totalAnios).isEqualTo(3); // 3 años del setUp

        // Agregar un año más
        AnioLectivo nuevoAnio = new AnioLectivo();
        nuevoAnio.setFechaInicio(LocalDate.of(2027, 2, 1));
        nuevoAnio.setFechaFinal(LocalDate.of(2027, 11, 30));
        nuevoAnio.setEstado("Planeado");
        anioLectivoRepository.save(nuevoAnio);

        // Verificar nuevo conteo
        long nuevoTotal = anioLectivoRepository.count();
        assertThat(nuevoTotal).isEqualTo(4);
    }

    @Test
    @DisplayName("Debería encontrar todos los años lectivos")
    void deberiaEncontrarTodosLosAniosLectivos() {
        List<AnioLectivo> todosLosAnios = anioLectivoRepository.findAll();

        assertThat(todosLosAnios).hasSize(3);
        assertThat(todosLosAnios).extracting("estado")
            .containsExactlyInAnyOrder("Finalizado", "Activo", "Planeado");
    }

    @Test
    @DisplayName("Debería manejar consulta de conflictos con rango completo")
    void deberiaManejarConsultaDeConflictosConRangoCompleto() {
        // Rango que incluye todos los años existentes
        LocalDate fechaInicioAmplia = LocalDate.of(2023, 1, 1);
        LocalDate fechaFinalAmplia = LocalDate.of(2025, 12, 31);

        List<AnioLectivo> todosLosConflictos = anioLectivoRepository
            .findConflictingAnioLectivo(fechaInicioAmplia, fechaFinalAmplia);

        assertThat(todosLosConflictos).hasSize(3);
        assertThat(todosLosConflictos).extracting("id")
            .containsExactlyInAnyOrder(anio2023.getId(), anio2024.getId(), anio2025.getId());
    }

    @Test
    @DisplayName("Debería existir por ID correctamente")
    void deberiaExistirPorIdCorrectamente() {
        // Verificar que existen los años del setUp
        assertThat(anioLectivoRepository.existsById(anio2023.getId())).isTrue();
        assertThat(anioLectivoRepository.existsById(anio2024.getId())).isTrue();
        assertThat(anioLectivoRepository.existsById(anio2025.getId())).isTrue();

        // Verificar que no existe ID inexistente
        assertThat(anioLectivoRepository.existsById(999)).isFalse();
    }
}