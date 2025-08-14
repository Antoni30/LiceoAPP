package com.liceo.notas.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para AnioLectivoDTO")
class AnioLectivoDTOTest {

    private AnioLectivoDTO anioLectivoDTO;

    @BeforeEach
    void setUp() {
        anioLectivoDTO = new AnioLectivoDTO();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        anioLectivoDTO.setId(id);
        
        assertThat(anioLectivoDTO.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener fecha de inicio correctamente")
    void deberiaEstablecerYObtenerFechaInicio() {
        LocalDate fechaInicio = LocalDate.of(2024, 2, 1);
        anioLectivoDTO.setFechaInicio(fechaInicio);
        
        assertThat(anioLectivoDTO.getFechaInicio()).isEqualTo(fechaInicio);
    }

    @Test
    @DisplayName("Debería establecer y obtener fecha final correctamente")
    void deberiaEstablecerYObtenerFechaFinal() {
        LocalDate fechaFinal = LocalDate.of(2024, 11, 30);
        anioLectivoDTO.setFechaFinal(fechaFinal);
        
        assertThat(anioLectivoDTO.getFechaFinal()).isEqualTo(fechaFinal);
    }

    @Test
    @DisplayName("Debería establecer y obtener estado correctamente")
    void deberiaEstablecerYObtenerEstado() {
        String estado = "PLANEADO";
        anioLectivoDTO.setEstado(estado);
        
        assertThat(anioLectivoDTO.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Debería crear DTO completo correctamente")
    void deberiaCrearDtoCompletoCorrectamente() {
        Integer id = 1;
        LocalDate fechaInicio = LocalDate.of(2024, 2, 5);
        LocalDate fechaFinal = LocalDate.of(2024, 12, 20);
        String estado = "EN_CURSO";
        
        anioLectivoDTO.setId(id);
        anioLectivoDTO.setFechaInicio(fechaInicio);
        anioLectivoDTO.setFechaFinal(fechaFinal);
        anioLectivoDTO.setEstado(estado);
        
        assertThat(anioLectivoDTO.getId()).isEqualTo(id);
        assertThat(anioLectivoDTO.getFechaInicio()).isEqualTo(fechaInicio);
        assertThat(anioLectivoDTO.getFechaFinal()).isEqualTo(fechaFinal);
        assertThat(anioLectivoDTO.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Debería manejar estados válidos del año lectivo")
    void deberiaManejarEstadosValidosDelAnioLectivo() {
        String[] estadosValidos = {"PLANEADO", "EN_CURSO", "FINALIZADO", "CANCELADO"};
        
        for (String estado : estadosValidos) {
            anioLectivoDTO.setEstado(estado);
            assertThat(anioLectivoDTO.getEstado()).isEqualTo(estado);
        }
    }

    @Test
    @DisplayName("Debería validar período académico típico ecuatoriano")
    void deberiaValidarPeriodoAcademicoTipicoEcuatoriano() {
        // Período académico régimen sierra
        LocalDate inicioSierra = LocalDate.of(2024, 9, 2);
        LocalDate finSierra = LocalDate.of(2025, 7, 4);
        
        anioLectivoDTO.setFechaInicio(inicioSierra);
        anioLectivoDTO.setFechaFinal(finSierra);
        
        assertThat(anioLectivoDTO.getFechaInicio()).isBefore(anioLectivoDTO.getFechaFinal());
        assertThat(anioLectivoDTO.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(anioLectivoDTO.getFechaFinal().getYear()).isEqualTo(2025);
    }

    @Test
    @DisplayName("Debería manejar año lectivo de régimen costa")
    void deberiaManejarAnioLectivoDeRegimenCosta() {
        // Período académico régimen costa
        LocalDate inicioCosta = LocalDate.of(2024, 5, 6);
        LocalDate finCosta = LocalDate.of(2025, 2, 7);
        
        anioLectivoDTO.setFechaInicio(inicioCosta);
        anioLectivoDTO.setFechaFinal(finCosta);
        anioLectivoDTO.setEstado("EN_CURSO");
        
        assertThat(anioLectivoDTO.getFechaInicio()).isEqualTo(inicioCosta);
        assertThat(anioLectivoDTO.getFechaFinal()).isEqualTo(finCosta);
        assertThat(anioLectivoDTO.getFechaInicio()).isBefore(anioLectivoDTO.getFechaFinal());
    }

    @Test
    @DisplayName("Debería permitir actualizar fechas del año lectivo")
    void deberiaPermitirActualizarFechasDelAnioLectivo() {
        // Fechas iniciales
        LocalDate fechaInicioOriginal = LocalDate.of(2024, 2, 1);
        LocalDate fechaFinalOriginal = LocalDate.of(2024, 11, 30);
        
        anioLectivoDTO.setFechaInicio(fechaInicioOriginal);
        anioLectivoDTO.setFechaFinal(fechaFinalOriginal);
        
        assertThat(anioLectivoDTO.getFechaInicio()).isEqualTo(fechaInicioOriginal);
        assertThat(anioLectivoDTO.getFechaFinal()).isEqualTo(fechaFinalOriginal);
        
        // Fechas actualizadas
        LocalDate nuevaFechaInicio = LocalDate.of(2024, 2, 5);
        LocalDate nuevaFechaFinal = LocalDate.of(2024, 12, 15);
        
        anioLectivoDTO.setFechaInicio(nuevaFechaInicio);
        anioLectivoDTO.setFechaFinal(nuevaFechaFinal);
        
        assertThat(anioLectivoDTO.getFechaInicio()).isEqualTo(nuevaFechaInicio);
        assertThat(anioLectivoDTO.getFechaFinal()).isEqualTo(nuevaFechaFinal);
    }

    @Test
    @DisplayName("Debería permitir cambiar estado del año lectivo")
    void deberiaPermitirCambiarEstadoDelAnioLectivo() {
        // Estado inicial
        anioLectivoDTO.setEstado("PLANEADO");
        assertThat(anioLectivoDTO.getEstado()).isEqualTo("PLANEADO");
        
        // Cambio a en curso
        anioLectivoDTO.setEstado("EN_CURSO");
        assertThat(anioLectivoDTO.getEstado()).isEqualTo("EN_CURSO");
        
        // Cambio a finalizado
        anioLectivoDTO.setEstado("FINALIZADO");
        assertThat(anioLectivoDTO.getEstado()).isEqualTo("FINALIZADO");
    }

    @Test
    @DisplayName("Debería manejar fechas con diferentes años")
    void deberiaManejarFechasConDiferentesAnios() {
        // Año lectivo que cruza años calendario
        LocalDate fechaInicio = LocalDate.of(2024, 9, 1);
        LocalDate fechaFinal = LocalDate.of(2025, 6, 30);
        
        anioLectivoDTO.setFechaInicio(fechaInicio);
        anioLectivoDTO.setFechaFinal(fechaFinal);
        
        assertThat(anioLectivoDTO.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(anioLectivoDTO.getFechaFinal().getYear()).isEqualTo(2025);
        assertThat(anioLectivoDTO.getFechaInicio()).isBefore(anioLectivoDTO.getFechaFinal());
    }

    @Test
    @DisplayName("Debería manejar valores nulos en campos opcionales")
    void deberiaManejarValoresNulosEnCamposOpcionales() {
        // ID puede ser nulo para nuevos registros
        anioLectivoDTO.setId(null);
        assertThat(anioLectivoDTO.getId()).isNull();
        
        // Estado puede ser nulo temporalmente
        anioLectivoDTO.setEstado(null);
        assertThat(anioLectivoDTO.getEstado()).isNull();
        
        // Fechas pueden ser nulas durante construcción
        anioLectivoDTO.setFechaInicio(null);
        anioLectivoDTO.setFechaFinal(null);
        assertThat(anioLectivoDTO.getFechaInicio()).isNull();
        assertThat(anioLectivoDTO.getFechaFinal()).isNull();
    }

    @Test
    @DisplayName("Debería implementar equals y hashCode por Lombok correctamente")
    void deberiaImplementarEqualsYHashCodePorLombokCorrectamente() {
        AnioLectivoDTO dto1 = new AnioLectivoDTO();
        dto1.setId(1);
        dto1.setFechaInicio(LocalDate.of(2024, 2, 1));
        dto1.setFechaFinal(LocalDate.of(2024, 11, 30));
        dto1.setEstado("EN_CURSO");
        
        AnioLectivoDTO dto2 = new AnioLectivoDTO();
        dto2.setId(1);
        dto2.setFechaInicio(LocalDate.of(2024, 2, 1));
        dto2.setFechaFinal(LocalDate.of(2024, 11, 30));
        dto2.setEstado("EN_CURSO");
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar toString por Lombok correctamente")
    void deberiaImplementarToStringPorLombokCorrectamente() {
        anioLectivoDTO.setId(1);
        anioLectivoDTO.setFechaInicio(LocalDate.of(2024, 2, 1));
        anioLectivoDTO.setFechaFinal(LocalDate.of(2024, 11, 30));
        anioLectivoDTO.setEstado("EN_CURSO");
        
        String resultado = anioLectivoDTO.toString();
        
        assertThat(resultado).contains("AnioLectivoDTO");
        assertThat(resultado).contains("id=1");
        assertThat(resultado).contains("fechaInicio=2024-02-01");
        assertThat(resultado).contains("fechaFinal=2024-11-30");
        assertThat(resultado).contains("estado=EN_CURSO");
    }

    @Test
    @DisplayName("Debería ser utilizable como DTO de transferencia de datos")
    void deberiaSerUtilizableComoDtoDeTransferenciaDeDatos() {
        // Simular datos recibidos desde una API REST
        anioLectivoDTO.setId(1);
        anioLectivoDTO.setFechaInicio(LocalDate.of(2024, 9, 2));
        anioLectivoDTO.setFechaFinal(LocalDate.of(2025, 7, 4));
        anioLectivoDTO.setEstado("EN_CURSO");
        
        // Verificar que todos los datos están disponibles
        assertThat(anioLectivoDTO.getId()).isNotNull();
        assertThat(anioLectivoDTO.getFechaInicio()).isNotNull();
        assertThat(anioLectivoDTO.getFechaFinal()).isNotNull();
        assertThat(anioLectivoDTO.getEstado()).isNotNull();
        
        // Verificar coherencia de datos
        assertThat(anioLectivoDTO.getFechaInicio()).isBefore(anioLectivoDTO.getFechaFinal());
    }
}