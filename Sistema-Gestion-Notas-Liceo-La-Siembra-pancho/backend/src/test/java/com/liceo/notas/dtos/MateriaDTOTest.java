package com.liceo.notas.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para MateriaDTO")
class MateriaDTOTest {

    private MateriaDTO materiaDTO;

    @BeforeEach
    void setUp() {
        materiaDTO = new MateriaDTO();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        materiaDTO.setId(id);
        
        assertThat(materiaDTO.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre de materia correctamente")
    void deberiaEstablecerYObtenerNombreMateria() {
        String nombreMateria = "Matemáticas";
        materiaDTO.setNombreMateria(nombreMateria);
        
        assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
    }

    @Test
    @DisplayName("Debería crear DTO completo correctamente")
    void deberiaCrearDtoCompletoCorrectamente() {
        Integer id = 1;
        String nombreMateria = "Lengua y Literatura";
        
        materiaDTO.setId(id);
        materiaDTO.setNombreMateria(nombreMateria);
        
        assertThat(materiaDTO.getId()).isEqualTo(id);
        assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
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
        
        for (String nombreMateria : materiasEGB) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
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
            "Biología",
            "Educación Física",
            "Educación Cultural y Artística",
            "Emprendimiento y Gestión"
        };
        
        for (String nombreMateria : materiasBGU) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
        }
    }

    @Test
    @DisplayName("Debería manejar materias especializadas del bachillerato técnico")
    void deberiaManejarMateriasEspecializadasDelBachilleratoTecnico() {
        String[] materiasTecnicas = {
            "Informática Aplicada",
            "Programación",
            "Redes y Comunicaciones",
            "Base de Datos",
            "Desarrollo Web",
            "Contabilidad General",
            "Administración",
            "Marketing",
            "Turismo y Hotelería",
            "Mecánica Automotriz",
            "Electricidad",
            "Electrónica"
        };
        
        for (String nombreMateria : materiasTecnicas) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
        }
    }

    @Test
    @DisplayName("Debería manejar materias con nombres largos y descriptivos")
    void deberiaManejarMateriasConNombresLargosYDescriptivos() {
        String[] materiasDescriptivas = {
            "Educación Cultural y Artística",
            "Educación para la Ciudadanía",
            "Emprendimiento y Gestión",
            "Módulos de Formación Técnica",
            "Investigación de Ciencia y Tecnología",
            "Desarrollo del Pensamiento Filosófico",
            "Historia y Ciencias Sociales"
        };
        
        for (String nombreMateria : materiasDescriptivas) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
            assertThat(materiaDTO.getNombreMateria().length()).isGreaterThan(15);
        }
    }

    @Test
    @DisplayName("Debería manejar materias con nombres cortos")
    void deberiaManejarMateriasConNombresCortos() {
        String[] materiasCortas = {
            "Matemática",
            "Física",
            "Química",
            "Biología",
            "Historia",
            "Filosofía",
            "Inglés",
            "Arte"
        };
        
        for (String nombreMateria : materiasCortas) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
            assertThat(materiaDTO.getNombreMateria().length()).isLessThan(15);
        }
    }

    @Test
    @DisplayName("Debería manejar materias con acentos y caracteres especiales")
    void deberiaManejarMateriasConAcentosYCaracteresEspeciales() {
        String[] materiasConAcentos = {
            "Matemática",
            "Educación Física",
            "Educación Cultural y Artística", 
            "Ciencias Sociales",
            "Lengua y Literatura",
            "Informática",
            "Filosofía"
        };
        
        for (String nombreMateria : materiasConAcentos) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes niveles de materias")
    void deberiaManejarDiferentesNivelesDeMaterias() {
        // Materias básicas
        materiaDTO.setId(1);
        materiaDTO.setNombreMateria("Matemática Básica");
        assertThat(materiaDTO.getNombreMateria()).contains("Básica");
        
        // Materias intermedias
        materiaDTO.setId(2);
        materiaDTO.setNombreMateria("Matemática Intermedia");
        assertThat(materiaDTO.getNombreMateria()).contains("Intermedia");
        
        // Materias avanzadas
        materiaDTO.setId(3);
        materiaDTO.setNombreMateria("Matemática Avanzada");
        assertThat(materiaDTO.getNombreMateria()).contains("Avanzada");
        
        // Materias especializadas
        materiaDTO.setId(4);
        materiaDTO.setNombreMateria("Cálculo Diferencial");
        assertThat(materiaDTO.getNombreMateria()).contains("Cálculo");
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
            "Robótica",
            "Ajedrez",
            "Oratoria",
            "Periodismo",
            "Fotografía"
        };
        
        for (String nombreMateria : materiasOptativas) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
        }
    }

    @Test
    @DisplayName("Debería manejar valores nulos en campos opcionales")
    void deberiaManejarValoresNulosEnCamposOpcionales() {
        // ID puede ser nulo para nuevos registros
        materiaDTO.setId(null);
        assertThat(materiaDTO.getId()).isNull();
        
        // Nombre puede ser nulo durante construcción
        materiaDTO.setNombreMateria(null);
        assertThat(materiaDTO.getNombreMateria()).isNull();
    }

    @Test
    @DisplayName("Debería implementar equals y hashCode por Lombok correctamente")
    void deberiaImplementarEqualsYHashCodePorLombokCorrectamente() {
        MateriaDTO dto1 = new MateriaDTO();
        dto1.setId(1);
        dto1.setNombreMateria("Matemática");
        
        MateriaDTO dto2 = new MateriaDTO();
        dto2.setId(1);
        dto2.setNombreMateria("Matemática");
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar toString por Lombok correctamente")
    void deberiaImplementarToStringPorLombokCorrectamente() {
        materiaDTO.setId(1);
        materiaDTO.setNombreMateria("Lengua y Literatura");
        
        String resultado = materiaDTO.toString();
        
        assertThat(resultado).contains("MateriaDTO");
        assertThat(resultado).contains("id=1");
        assertThat(resultado).contains("nombreMateria=Lengua y Literatura");
    }

    @Test
    @DisplayName("Debería ser utilizable como DTO de transferencia de datos")
    void deberiaSerUtilizableComoDtoDeTransferenciaDeDatos() {
        // Simular datos recibidos desde una API REST
        materiaDTO.setId(1);
        materiaDTO.setNombreMateria("Ciencias Naturales");
        
        // Verificar que todos los datos están disponibles
        assertThat(materiaDTO.getId()).isNotNull();
        assertThat(materiaDTO.getNombreMateria()).isNotNull();
        
        // Verificar coherencia de datos
        assertThat(materiaDTO.getId()).isPositive();
        assertThat(materiaDTO.getNombreMateria()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería manejar materias por áreas del conocimiento")
    void deberiaManejarMateriasPorAreasDelConocimiento() {
        // Área de Comunicación
        String[] areaComunicacion = {"Lengua y Literatura", "Inglés", "Francés"};
        for (String materia : areaComunicacion) {
            materiaDTO.setNombreMateria(materia);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(materia);
        }
        
        // Área de Matemáticas
        String[] areaMatematicas = {"Matemática", "Estadística", "Cálculo"};
        for (String materia : areaMatematicas) {
            materiaDTO.setNombreMateria(materia);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(materia);
        }
        
        // Área de Ciencias Experimentales
        String[] areaCiencias = {"Física", "Química", "Biología", "Ciencias Naturales"};
        for (String materia : areaCiencias) {
            materiaDTO.setNombreMateria(materia);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(materia);
        }
        
        // Área de Ciencias Sociales
        String[] areaSociales = {"Historia", "Geografía", "Educación para la Ciudadanía"};
        for (String materia : areaSociales) {
            materiaDTO.setNombreMateria(materia);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(materia);
        }
    }

    @Test
    @DisplayName("Debería manejar identificadores únicos de materias")
    void deberiaManejarIdentificadoresUnicosDeMaterias() {
        // Diferentes IDs para diferentes materias
        Integer[] idsUnicos = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 50, 100};
        
        for (Integer id : idsUnicos) {
            materiaDTO.setId(id);
            assertThat(materiaDTO.getId()).isEqualTo(id);
            assertThat(materiaDTO.getId()).isPositive();
        }
    }

    @Test
    @DisplayName("Debería manejar nombres de materias únicas en el sistema")
    void deberiaManejarNombresDeMaterialesUnicasEnElSistema() {
        // Verificar que cada materia tiene un nombre único y descriptivo
        String[] materiasUnicas = {
            "Matemática 1° BGU",
            "Matemática 2° BGU", 
            "Matemática 3° BGU",
            "Física 1° BGU",
            "Física 2° BGU",
            "Química 2° BGU",
            "Química 3° BGU"
        };
        
        for (String nombreMateria : materiasUnicas) {
            materiaDTO.setNombreMateria(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).isEqualTo(nombreMateria);
            assertThat(materiaDTO.getNombreMateria()).contains("BGU");
        }
    }

    @Test
    @DisplayName("Debería validar coherencia de datos de materia")
    void deberiaValidarCoherenciaDeDatosDeMateria() {
        materiaDTO.setId(1);
        materiaDTO.setNombreMateria("Educación Física");
        
        // Verificar que el ID y nombre son coherentes
        assertThat(materiaDTO.getId()).isNotNull();
        assertThat(materiaDTO.getNombreMateria()).isNotNull();
        assertThat(materiaDTO.getId()).isPositive();
        assertThat(materiaDTO.getNombreMateria()).isNotEmpty();
        
        // Una materia debe tener tanto ID como nombre para ser válida
        assertThat(materiaDTO.getId()).isGreaterThan(0);
        assertThat(materiaDTO.getNombreMateria().trim()).isNotEmpty();
    }
}