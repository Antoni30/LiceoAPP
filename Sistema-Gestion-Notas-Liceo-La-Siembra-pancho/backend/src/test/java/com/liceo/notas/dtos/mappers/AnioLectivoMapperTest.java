package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.AnioLectivoDTO;
import com.liceo.notas.entities.AnioLectivo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests para AnioLectivoMapper")
class AnioLectivoMapperTest {

    @Test
    @DisplayName("Debería convertir entidad a DTO correctamente")
    void deberiaConvertirEntidadADtoCorrectamente() {
        // Crear entidad
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(1);
        entidad.setFechaInicio(LocalDate.of(2024, 2, 1));
        entidad.setFechaFinal(LocalDate.of(2024, 11, 30));
        entidad.setEstado("EN_CURSO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getFechaInicio()).isEqualTo(entidad.getFechaInicio());
        assertThat(dto.getFechaFinal()).isEqualTo(entidad.getFechaFinal());
        assertThat(dto.getEstado()).isEqualTo(entidad.getEstado());
    }

    @Test
    @DisplayName("Debería convertir DTO a entidad correctamente")
    void deberiaConvertirDtoAEntidadCorrectamente() {
        // Crear DTO
        AnioLectivoDTO dto = new AnioLectivoDTO();
        dto.setId(1);
        dto.setFechaInicio(LocalDate.of(2024, 2, 1));
        dto.setFechaFinal(LocalDate.of(2024, 11, 30));
        dto.setEstado("PLANEADO");
        
        // Convertir a entidad
        AnioLectivo entidad = AnioLectivoMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isEqualTo(dto.getId());
        assertThat(entidad.getFechaInicio()).isEqualTo(dto.getFechaInicio());
        assertThat(entidad.getFechaFinal()).isEqualTo(dto.getFechaFinal());
        assertThat(entidad.getEstado()).isEqualTo(dto.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando entidad es null")
    void deberiaLanzarExcepcionCuandoEntidadEsNull() {
        assertThatThrownBy(() -> AnioLectivoMapper.toDTO(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("La entidad AnioLectivo no puede ser nula");
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando DTO es null")
    void deberiaLanzarExcepcionCuandoDtoEsNull() {
        assertThatThrownBy(() -> AnioLectivoMapper.toEntity(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El DTO AnioLectivoDTO no puede ser nulo");
    }

    @Test
    @DisplayName("Debería manejar entidad con campos nulos")
    void deberiaManejarEntidadConCamposNulos() {
        // Crear entidad con algunos campos nulos
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(null);
        entidad.setFechaInicio(LocalDate.of(2024, 2, 1));
        entidad.setFechaFinal(null);
        entidad.setEstado("PLANEADO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getFechaInicio()).isEqualTo(entidad.getFechaInicio());
        assertThat(dto.getFechaFinal()).isNull();
        assertThat(dto.getEstado()).isEqualTo(entidad.getEstado());
    }

    @Test
    @DisplayName("Debería manejar DTO con campos nulos")
    void deberiaManejarDtoConCamposNulos() {
        // Crear DTO con algunos campos nulos
        AnioLectivoDTO dto = new AnioLectivoDTO();
        dto.setId(null);
        dto.setFechaInicio(null);
        dto.setFechaFinal(LocalDate.of(2024, 11, 30));
        dto.setEstado(null);
        
        // Convertir a entidad
        AnioLectivo entidad = AnioLectivoMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isNull();
        assertThat(entidad.getFechaInicio()).isNull();
        assertThat(entidad.getFechaFinal()).isEqualTo(dto.getFechaFinal());
        assertThat(entidad.getEstado()).isNull();
    }

    @Test
    @DisplayName("Debería manejar conversión bidireccional correctamente")
    void deberiaManejarConversionBidireccionalCorrectamente() {
        // Crear entidad original
        AnioLectivo entidadOriginal = new AnioLectivo();
        entidadOriginal.setId(1);
        entidadOriginal.setFechaInicio(LocalDate.of(2024, 9, 2));
        entidadOriginal.setFechaFinal(LocalDate.of(2025, 7, 4));
        entidadOriginal.setEstado("EN_CURSO");
        
        // Convertir a DTO y de vuelta a entidad
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidadOriginal);
        AnioLectivo entidadConvertida = AnioLectivoMapper.toEntity(dto);
        
        // Verificar que los datos se mantienen
        assertThat(entidadConvertida.getId()).isEqualTo(entidadOriginal.getId());
        assertThat(entidadConvertida.getFechaInicio()).isEqualTo(entidadOriginal.getFechaInicio());
        assertThat(entidadConvertida.getFechaFinal()).isEqualTo(entidadOriginal.getFechaFinal());
        assertThat(entidadConvertida.getEstado()).isEqualTo(entidadOriginal.getEstado());
    }

    @Test
    @DisplayName("Debería manejar período académico régimen sierra")
    void deberiaManejarPeriodoAcademicoRegimenSierra() {
        // Crear entidad con fechas del régimen sierra
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(1);
        entidad.setFechaInicio(LocalDate.of(2024, 9, 2));
        entidad.setFechaFinal(LocalDate.of(2025, 7, 4));
        entidad.setEstado("EN_CURSO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificaciones específicas del régimen sierra
        assertThat(dto.getFechaInicio().getMonthValue()).isEqualTo(9); // Septiembre
        assertThat(dto.getFechaFinal().getMonthValue()).isEqualTo(7);  // Julio
        assertThat(dto.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(dto.getFechaFinal().getYear()).isEqualTo(2025);
    }

    @Test
    @DisplayName("Debería manejar período académico régimen costa")
    void deberiaManejarPeriodoAcademicoRegimenCosta() {
        // Crear entidad con fechas del régimen costa
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(2);
        entidad.setFechaInicio(LocalDate.of(2024, 5, 6));
        entidad.setFechaFinal(LocalDate.of(2025, 2, 7));
        entidad.setEstado("EN_CURSO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificaciones específicas del régimen costa
        assertThat(dto.getFechaInicio().getMonthValue()).isEqualTo(5); // Mayo
        assertThat(dto.getFechaFinal().getMonthValue()).isEqualTo(2);  // Febrero
        assertThat(dto.getFechaInicio().getYear()).isEqualTo(2024);
        assertThat(dto.getFechaFinal().getYear()).isEqualTo(2025);
    }

    @Test
    @DisplayName("Debería manejar diferentes estados del año lectivo")
    void deberiaManejarDiferentesEstadosDelAnioLectivo() {
        String[] estados = {"PLANEADO", "EN_CURSO", "FINALIZADO", "CANCELADO"};
        
        for (String estado : estados) {
            // Crear entidad con estado específico
            AnioLectivo entidad = new AnioLectivo();
            entidad.setId(1);
            entidad.setFechaInicio(LocalDate.of(2024, 2, 1));
            entidad.setFechaFinal(LocalDate.of(2024, 11, 30));
            entidad.setEstado(estado);
            
            // Convertir a DTO
            AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
            
            // Verificar estado
            assertThat(dto.getEstado()).isEqualTo(estado);
        }
    }

    @Test
    @DisplayName("Debería manejar conversión para nuevo año lectivo sin ID")
    void deberiaManejarConversionParaNuevoAnioLectivoSinId() {
        // Crear DTO para nuevo año lectivo (sin ID)
        AnioLectivoDTO dto = new AnioLectivoDTO();
        dto.setId(null);
        dto.setFechaInicio(LocalDate.of(2025, 2, 1));
        dto.setFechaFinal(LocalDate.of(2025, 11, 30));
        dto.setEstado("PLANEADO");
        
        // Convertir a entidad
        AnioLectivo entidad = AnioLectivoMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad.getId()).isNull(); // ID nulo para nuevos registros
        assertThat(entidad.getFechaInicio()).isEqualTo(dto.getFechaInicio());
        assertThat(entidad.getFechaFinal()).isEqualTo(dto.getFechaFinal());
        assertThat(entidad.getEstado()).isEqualTo(dto.getEstado());
    }

    @Test
    @DisplayName("Debería manejar conversión para actualización con ID")
    void deberiaManejarConversionParaActualizacionConId() {
        // Crear DTO para actualización (con ID)
        AnioLectivoDTO dto = new AnioLectivoDTO();
        dto.setId(1);
        dto.setFechaInicio(LocalDate.of(2024, 2, 5));
        dto.setFechaFinal(LocalDate.of(2024, 12, 15));
        dto.setEstado("FINALIZADO");
        
        // Convertir a entidad
        AnioLectivo entidad = AnioLectivoMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad.getId()).isEqualTo(dto.getId()); // ID presente para actualización
        assertThat(entidad.getFechaInicio()).isEqualTo(dto.getFechaInicio());
        assertThat(entidad.getFechaFinal()).isEqualTo(dto.getFechaFinal());
        assertThat(entidad.getEstado()).isEqualTo(dto.getEstado());
    }

    @Test
    @DisplayName("Debería preservar tipos de datos LocalDate correctamente")
    void deberiaPreservarTiposDeDatosLocalDateCorrectamente() {
        // Crear entidad con fechas específicas
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(1);
        entidad.setFechaInicio(LocalDate.of(2024, 1, 15));
        entidad.setFechaFinal(LocalDate.of(2024, 12, 20));
        entidad.setEstado("EN_CURSO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificar tipos y valores exactos
        assertThat(dto.getFechaInicio()).isInstanceOf(LocalDate.class);
        assertThat(dto.getFechaFinal()).isInstanceOf(LocalDate.class);
        assertThat(dto.getFechaInicio()).isEqualTo("2024-01-15");
        assertThat(dto.getFechaFinal()).isEqualTo("2024-12-20");
    }

    @Test
    @DisplayName("Debería manejar múltiples conversiones de diferentes años lectivos")
    void deberiaManejarMultiplesConversionesDeDiferentesAniosLectivos() {
        // Datos de múltiples años lectivos
        Object[][] datosAniosLectivos = {
            {1, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 11, 30), "FINALIZADO"},
            {2, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 11, 30), "EN_CURSO"},
            {3, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 11, 30), "PLANEADO"}
        };
        
        for (Object[] datos : datosAniosLectivos) {
            // Crear entidad
            AnioLectivo entidad = new AnioLectivo();
            entidad.setId((Integer) datos[0]);
            entidad.setFechaInicio((LocalDate) datos[1]);
            entidad.setFechaFinal((LocalDate) datos[2]);
            entidad.setEstado((String) datos[3]);
            
            // Convertir a DTO
            AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(datos[0]);
            assertThat(dto.getFechaInicio()).isEqualTo(datos[1]);
            assertThat(dto.getFechaFinal()).isEqualTo(datos[2]);
            assertThat(dto.getEstado()).isEqualTo(datos[3]);
        }
    }

    @Test
    @DisplayName("Debería validar integridad de mapeo entre entidad y DTO")
    void deberiaValidarIntegridadDeMapeoEntreEntidadYDto() {
        // Crear entidad completa
        AnioLectivo entidad = new AnioLectivo();
        entidad.setId(1);
        entidad.setFechaInicio(LocalDate.of(2024, 9, 2));
        entidad.setFechaFinal(LocalDate.of(2025, 7, 4));
        entidad.setEstado("EN_CURSO");
        
        // Convertir a DTO
        AnioLectivoDTO dto = AnioLectivoMapper.toDTO(entidad);
        
        // Verificar que todos los campos están mapeados
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getFechaInicio()).isNotNull();
        assertThat(dto.getFechaFinal()).isNotNull();
        assertThat(dto.getEstado()).isNotNull();
        
        // Verificar integridad de datos
        assertThat(dto.getFechaInicio()).isBefore(dto.getFechaFinal());
        assertThat(dto.getId()).isPositive();
        assertThat(dto.getEstado()).isNotEmpty();
    }
}