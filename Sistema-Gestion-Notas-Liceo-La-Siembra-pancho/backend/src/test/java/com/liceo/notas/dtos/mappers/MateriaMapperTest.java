package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.MateriaDTO;
import com.liceo.notas.entities.Materia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests para MateriaMapper")
class MateriaMapperTest {

    @Test
    @DisplayName("Debería convertir entidad a DTO correctamente")
    void deberiaConvertirEntidadADtoCorrectamente() {
        // Crear entidad
        Materia entidad = new Materia();
        entidad.setId(1);
        entidad.setNombreMateria("Matemáticas");
        
        // Convertir a DTO
        MateriaDTO dto = MateriaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getNombreMateria()).isEqualTo(entidad.getNombreMateria());
    }

    @Test
    @DisplayName("Debería convertir DTO a entidad correctamente")
    void deberiaConvertirDtoAEntidadCorrectamente() {
        // Crear DTO
        MateriaDTO dto = new MateriaDTO();
        dto.setId(1);
        dto.setNombreMateria("Física");
        
        // Convertir a entidad
        Materia entidad = MateriaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isEqualTo(dto.getId());
        assertThat(entidad.getNombreMateria()).isEqualTo(dto.getNombreMateria());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando entidad es null")
    void deberiaLanzarExcepcionCuandoEntidadEsNull() {
        assertThatThrownBy(() -> MateriaMapper.toDTO(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("La entidad Materia no puede ser nula");
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando DTO es null")
    void deberiaLanzarExcepcionCuandoDtoEsNull() {
        assertThatThrownBy(() -> MateriaMapper.toEntity(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El DTO MateriaDTO no puede ser nulo");
    }

    @Test
    @DisplayName("Debería manejar entidad con campos nulos")
    void deberiaManejarEntidadConCamposNulos() {
        // Crear entidad con algunos campos nulos
        Materia entidad = new Materia();
        entidad.setId(null);
        entidad.setNombreMateria("Química");
        
        // Convertir a DTO
        MateriaDTO dto = MateriaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getNombreMateria()).isEqualTo(entidad.getNombreMateria());
    }

    @Test
    @DisplayName("Debería manejar DTO con campos nulos")
    void deberiaManejarDtoConCamposNulos() {
        // Crear DTO con algunos campos nulos
        MateriaDTO dto = new MateriaDTO();
        dto.setId(1);
        dto.setNombreMateria(null);
        
        // Convertir a entidad
        Materia entidad = MateriaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isEqualTo(dto.getId());
        assertThat(entidad.getNombreMateria()).isNull();
    }

    @Test
    @DisplayName("Debería manejar conversión bidireccional correctamente")
    void deberiaManejarConversionBidireccionalCorrectamente() {
        // Crear entidad original
        Materia entidadOriginal = new Materia();
        entidadOriginal.setId(1);
        entidadOriginal.setNombreMateria("Biología");
        
        // Convertir a DTO y de vuelta a entidad
        MateriaDTO dto = MateriaMapper.toDTO(entidadOriginal);
        Materia entidadConvertida = MateriaMapper.toEntity(dto);
        
        // Verificar que los datos se mantienen
        assertThat(entidadConvertida.getId()).isEqualTo(entidadOriginal.getId());
        assertThat(entidadConvertida.getNombreMateria()).isEqualTo(entidadOriginal.getNombreMateria());
    }

    @Test
    @DisplayName("Debería manejar materias del currículo ecuatoriano de EGB")
    void deberiaManejarMateriasDelCurriculoEcuatorianoDeEGB() {
        String[] materiasEGB = {
            "Lengua y Literatura",
            "Matemática",
            "Ciencias Naturales",
            "Ciencias Sociales",
            "Inglés",
            "Educación Física",
            "Educación Cultural y Artística"
        };
        
        for (int i = 0; i < materiasEGB.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasEGB[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasEGB[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar materias del bachillerato general unificado")
    void deberiaManejarMateriasDelBachilleratoGeneralUnificado() {
        String[] materiasBGU = {
            "Matemática",
            "Lengua y Literatura", 
            "Inglés",
            "Historia",
            "Educación para la Ciudadanía",
            "Filosofía",
            "Física",
            "Química",
            "Biología"
        };
        
        for (int i = 0; i < materiasBGU.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasBGU[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasBGU[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar materias técnicas especializadas")
    void deberiaManejarMateriasTecnicasEspecializadas() {
        String[] materiasTecnicas = {
            "Informática Aplicada",
            "Programación",
            "Redes y Comunicaciones",
            "Base de Datos",
            "Contabilidad General",
            "Administración",
            "Marketing",
            "Mecánica Automotriz"
        };
        
        for (int i = 0; i < materiasTecnicas.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasTecnicas[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasTecnicas[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar materias con nombres largos y descriptivos")
    void deberiaManejarMateriasConNombresLargosYDescriptivos() {
        String[] materiasDescriptivas = {
            "Educación Cultural y Artística",
            "Educación para la Ciudadanía",
            "Emprendimiento y Gestión",
            "Investigación de Ciencia y Tecnología",
            "Desarrollo del Pensamiento Filosófico"
        };
        
        for (int i = 0; i < materiasDescriptivas.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasDescriptivas[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasDescriptivas[i]);
            assertThat(dto.getNombreMateria().length()).isGreaterThan(15);
        }
    }

    @Test
    @DisplayName("Debería manejar materias con caracteres especiales y acentos")
    void deberiaManejarMateriasConCaracteresEspecialesYAcentos() {
        String[] materiasConAcentos = {
            "Matemática",
            "Educación Física",
            "Filosofía",
            "Ciencias Sociales",
            "Informática",
            "Educación Artística"
        };
        
        for (int i = 0; i < materiasConAcentos.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasConAcentos[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasConAcentos[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar conversión para nueva materia sin ID")
    void deberiaManejarConversionParaNuevaMateriasinId() {
        // Crear DTO para nueva materia (sin ID)
        MateriaDTO dto = new MateriaDTO();
        dto.setId(null);
        dto.setNombreMateria("Robótica");
        
        // Convertir a entidad
        Materia entidad = MateriaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad.getId()).isNull(); // ID nulo para nuevos registros
        assertThat(entidad.getNombreMateria()).isEqualTo(dto.getNombreMateria());
    }

    @Test
    @DisplayName("Debería manejar conversión para actualización con ID")
    void deberiaManejarConversionParaActualizacionConId() {
        // Crear DTO para actualización (con ID)
        MateriaDTO dto = new MateriaDTO();
        dto.setId(1);
        dto.setNombreMateria("Matemática Avanzada");
        
        // Convertir a entidad
        Materia entidad = MateriaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad.getId()).isEqualTo(dto.getId()); // ID presente para actualización
        assertThat(entidad.getNombreMateria()).isEqualTo(dto.getNombreMateria());
    }

    @Test
    @DisplayName("Debería manejar materias optativas y electivas")
    void deberiaManejarMateriasOptativasYElectivas() {
        String[] materiasOptativas = {
            "Música",
            "Teatro", 
            "Danza",
            "Artes Plásticas",
            "Deportes",
            "Ajedrez",
            "Oratoria",
            "Periodismo"
        };
        
        for (int i = 0; i < materiasOptativas.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 10); // IDs a partir de 10 para optativas
            entidad.setNombreMateria(materiasOptativas[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 10);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasOptativas[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes niveles de la misma materia")
    void deberiaManejarDiferentesNivelesDeLaMismaMateria() {
        // Matemática por niveles
        String[] nivelesMatematica = {
            "Matemática Básica",
            "Matemática Intermedia",
            "Matemática Avanzada",
            "Cálculo Diferencial",
            "Cálculo Integral"
        };
        
        for (int i = 0; i < nivelesMatematica.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(nivelesMatematica[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(nivelesMatematica[i]);
            assertThat(dto.getNombreMateria()).contains("Matemática", "Cálculo");
        }
    }

    @Test
    @DisplayName("Debería preservar nombres únicos de materias")
    void deberiaPreservarNombresUnicosDeMaterias() {
        // Verificar que cada materia mantiene su nombre único
        String[] materiasUnicas = {
            "Matemática 1° BGU",
            "Matemática 2° BGU",
            "Matemática 3° BGU",
            "Física 1° BGU", 
            "Física 2° BGU",
            "Química 2° BGU",
            "Química 3° BGU"
        };
        
        for (int i = 0; i < materiasUnicas.length; i++) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId(i + 1);
            entidad.setNombreMateria(materiasUnicas[i]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(i + 1);
            assertThat(dto.getNombreMateria()).isEqualTo(materiasUnicas[i]);
            assertThat(dto.getNombreMateria()).contains("BGU");
        }
    }

    @Test
    @DisplayName("Debería manejar múltiples conversiones sin interferencia")
    void deberiaManejarMultiplesConversionesSinInterferencia() {
        // Datos de múltiples materias
        Object[][] datosMaterias = {
            {1, "Matemática"},
            {2, "Lengua y Literatura"},
            {3, "Ciencias Naturales"},
            {4, "Ciencias Sociales"},
            {5, "Inglés"}
        };
        
        for (Object[] datos : datosMaterias) {
            // Crear entidad
            Materia entidad = new Materia();
            entidad.setId((Integer) datos[0]);
            entidad.setNombreMateria((String) datos[1]);
            
            // Convertir a DTO
            MateriaDTO dto = MateriaMapper.toDTO(entidad);
            
            // Verificaciones
            assertThat(dto.getId()).isEqualTo(datos[0]);
            assertThat(dto.getNombreMateria()).isEqualTo(datos[1]);
        }
    }

    @Test
    @DisplayName("Debería validar integridad de mapeo entre entidad y DTO")
    void deberiaValidarIntegridadDeMapeoEntreEntidadYDto() {
        // Crear entidad completa
        Materia entidad = new Materia();
        entidad.setId(1);
        entidad.setNombreMateria("Educación Física");
        
        // Convertir a DTO
        MateriaDTO dto = MateriaMapper.toDTO(entidad);
        
        // Verificar que todos los campos están mapeados
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getNombreMateria()).isNotNull();
        
        // Verificar integridad de datos
        assertThat(dto.getId()).isPositive();
        assertThat(dto.getNombreMateria()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería validar que el mapeo no altera datos originales")
    void deberiaValidarQueElMapeoNoAlteraDatosOriginales() {
        // Crear entidad original
        Materia entidadOriginal = new Materia();
        entidadOriginal.setId(5);
        entidadOriginal.setNombreMateria("Historia Universal");
        
        // Convertir a DTO
        MateriaDTO dto = MateriaMapper.toDTO(entidadOriginal);
        
        // Verificar que la entidad original no cambió
        assertThat(entidadOriginal.getId()).isEqualTo(5);
        assertThat(entidadOriginal.getNombreMateria()).isEqualTo("Historia Universal");
        
        // Verificar que el DTO tiene los datos correctos
        assertThat(dto.getId()).isEqualTo(5);
        assertThat(dto.getNombreMateria()).isEqualTo("Historia Universal");
    }

    @Test
    @DisplayName("Debería manejar materias por áreas del conocimiento")
    void deberiaManejarMateriasPorAreasDelConocimiento() {
        // Área de Comunicación
        Materia comunicacion = new Materia();
        comunicacion.setId(1);
        comunicacion.setNombreMateria("Lengua y Literatura");
        
        MateriaDTO dtoComunicacion = MateriaMapper.toDTO(comunicacion);
        assertThat(dtoComunicacion.getNombreMateria()).contains("Lengua");
        
        // Área de Matemáticas
        Materia matematicas = new Materia();
        matematicas.setId(2);
        matematicas.setNombreMateria("Matemática");
        
        MateriaDTO dtoMatematicas = MateriaMapper.toDTO(matematicas);
        assertThat(dtoMatematicas.getNombreMateria()).isEqualTo("Matemática");
        
        // Área de Ciencias Experimentales
        Materia ciencias = new Materia();
        ciencias.setId(3);
        ciencias.setNombreMateria("Ciencias Naturales");
        
        MateriaDTO dtoCiencias = MateriaMapper.toDTO(ciencias);
        assertThat(dtoCiencias.getNombreMateria()).contains("Ciencias");
    }
}