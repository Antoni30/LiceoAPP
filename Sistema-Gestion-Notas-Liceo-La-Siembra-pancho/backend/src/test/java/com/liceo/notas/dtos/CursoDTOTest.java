package com.liceo.notas.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para CursoDTO")
class CursoDTOTest {

    private CursoDTO cursoDTO;

    @BeforeEach
    void setUp() {
        cursoDTO = new CursoDTO();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        cursoDTO.setId(id);
        
        assertThat(cursoDTO.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener ID de año lectivo correctamente")
    void deberiaEstablecerYObtenerIdAnioLectivo() {
        Integer idAnioLectivo = 1;
        cursoDTO.setIdAnioLectivo(idAnioLectivo);
        
        assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(idAnioLectivo);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre de curso correctamente")
    void deberiaEstablecerYObtenerNombreCurso() {
        String nombreCurso = "1° Bachillerato A";
        cursoDTO.setNombreCurso(nombreCurso);
        
        assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
    }

    @Test
    @DisplayName("Debería crear DTO completo correctamente")
    void deberiaCrearDtoCompletoCorrectamente() {
        Integer id = 1;
        Integer idAnioLectivo = 1;
        String nombreCurso = "2° Bachillerato B";
        
        cursoDTO.setId(id);
        cursoDTO.setIdAnioLectivo(idAnioLectivo);
        cursoDTO.setNombreCurso(nombreCurso);
        
        assertThat(cursoDTO.getId()).isEqualTo(id);
        assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(idAnioLectivo);
        assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
    }

    @Test
    @DisplayName("Debería manejar nombres de curso del sistema educativo ecuatoriano")
    void deberiaManejarNombresDeCursoDelSistemaEducativoEcuatoriano() {
        String[] cursosComunes = {
            "1° EGB", "2° EGB", "3° EGB", "4° EGB", "5° EGB", "6° EGB", "7° EGB",
            "8° EGB", "9° EGB", "10° EGB",
            "1° Bachillerato", "2° Bachillerato", "3° Bachillerato"
        };
        
        for (String nombreCurso : cursosComunes) {
            cursoDTO.setNombreCurso(nombreCurso);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
        }
    }

    @Test
    @DisplayName("Debería manejar cursos con secciones")
    void deberiaManejarCursosConSecciones() {
        String[] cursosConSecciones = {
            "1° A", "1° B", "1° C",
            "2° A", "2° B", 
            "3° A", "3° B", "3° C",
            "1° Bachillerato A", "1° Bachillerato B",
            "2° Bachillerato A", "2° Bachillerato B",
            "3° Bachillerato A"
        };
        
        for (String nombreCurso : cursosConSecciones) {
            cursoDTO.setNombreCurso(nombreCurso);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
            assertThat(cursoDTO.getNombreCurso()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes años lectivos")
    void deberiaManejarDiferentesAniosLectivos() {
        Integer[] aniosLectivosIds = {1, 2, 3, 4, 5, 10, 20, 50, 100};
        
        for (Integer idAnioLectivo : aniosLectivosIds) {
            cursoDTO.setIdAnioLectivo(idAnioLectivo);
            assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(idAnioLectivo);
            assertThat(cursoDTO.getIdAnioLectivo()).isPositive();
        }
    }

    @Test
    @DisplayName("Debería manejar cursos especializados de bachillerato")
    void deberiaManejarCursosEspecializadosDeBachillerato() {
        String[] bachilleratosEspecializados = {
            "1° Bachillerato Ciencias",
            "2° Bachillerato Técnico", 
            "3° Bachillerato Internacional",
            "1° Bachillerato Artístico",
            "2° Bachillerato Deportivo"
        };
        
        for (String nombreCurso : bachilleratosEspecializados) {
            cursoDTO.setNombreCurso(nombreCurso);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
            assertThat(cursoDTO.getNombreCurso()).contains("Bachillerato");
        }
    }

    @Test
    @DisplayName("Debería manejar múltiples cursos para el mismo año lectivo")
    void deberiaManejarMultiplesCursosParaElMismoAnioLectivo() {
        Integer anioLectivoComun = 1;
        String[] cursosDelMismoAnio = {
            "1° A", "1° B", "1° C",
            "2° A", "2° B",
            "3° A", "3° B"
        };
        
        for (String nombreCurso : cursosDelMismoAnio) {
            cursoDTO.setIdAnioLectivo(anioLectivoComun);
            cursoDTO.setNombreCurso(nombreCurso);
            
            assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(anioLectivoComun);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombreCurso);
        }
    }

    @Test
    @DisplayName("Debería manejar nombres de curso cortos y largos")
    void deberiaManejarNombresDeCursoCortosyLargos() {
        // Nombres cortos
        String[] nombresCortos = {"1°A", "2°B", "3°C"};
        for (String nombre : nombresCortos) {
            cursoDTO.setNombreCurso(nombre);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombre);
            assertThat(cursoDTO.getNombreCurso().length()).isLessThan(10);
        }
        
        // Nombres largos descriptivos
        String[] nombresLargos = {
            "1° Bachillerato Técnico en Informática",
            "2° Bachillerato Internacional Bilingüe",
            "3° Bachillerato Científico Especializado"
        };
        for (String nombre : nombresLargos) {
            cursoDTO.setNombreCurso(nombre);
            assertThat(cursoDTO.getNombreCurso()).isEqualTo(nombre);
            assertThat(cursoDTO.getNombreCurso().length()).isGreaterThan(20);
        }
    }

    @Test
    @DisplayName("Debería manejar valores nulos en campos opcionales")
    void deberiaManejarValoresNulosEnCamposOpcionales() {
        // ID puede ser nulo para nuevos registros
        cursoDTO.setId(null);
        assertThat(cursoDTO.getId()).isNull();
        
        // ID del año lectivo puede ser nulo temporalmente
        cursoDTO.setIdAnioLectivo(null);
        assertThat(cursoDTO.getIdAnioLectivo()).isNull();
        
        // Nombre puede ser nulo durante construcción
        cursoDTO.setNombreCurso(null);
        assertThat(cursoDTO.getNombreCurso()).isNull();
    }

    @Test
    @DisplayName("Debería implementar equals y hashCode por Lombok correctamente")
    void deberiaImplementarEqualsYHashCodePorLombokCorrectamente() {
        CursoDTO dto1 = new CursoDTO();
        dto1.setId(1);
        dto1.setIdAnioLectivo(1);
        dto1.setNombreCurso("1° Bachillerato A");
        
        CursoDTO dto2 = new CursoDTO();
        dto2.setId(1);
        dto2.setIdAnioLectivo(1);
        dto2.setNombreCurso("1° Bachillerato A");
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar toString por Lombok correctamente")
    void deberiaImplementarToStringPorLombokCorrectamente() {
        cursoDTO.setId(1);
        cursoDTO.setIdAnioLectivo(1);
        cursoDTO.setNombreCurso("2° Bachillerato B");
        
        String resultado = cursoDTO.toString();
        
        assertThat(resultado).contains("CursoDTO");
        assertThat(resultado).contains("id=1");
        assertThat(resultado).contains("idAnioLectivo=1");
        assertThat(resultado).contains("nombreCurso=2° Bachillerato B");
    }

    @Test
    @DisplayName("Debería ser utilizable como DTO de transferencia de datos")
    void deberiaSerUtilizableComoDtoDeTransferenciaDeDatos() {
        // Simular datos recibidos desde una API REST
        cursoDTO.setId(1);
        cursoDTO.setIdAnioLectivo(1);
        cursoDTO.setNombreCurso("1° Bachillerato A");
        
        // Verificar que todos los datos están disponibles
        assertThat(cursoDTO.getId()).isNotNull();
        assertThat(cursoDTO.getIdAnioLectivo()).isNotNull();
        assertThat(cursoDTO.getNombreCurso()).isNotNull();
        
        // Verificar coherencia de datos
        assertThat(cursoDTO.getId()).isPositive();
        assertThat(cursoDTO.getIdAnioLectivo()).isPositive();
        assertThat(cursoDTO.getNombreCurso()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería manejar escenarios de organización escolar")
    void deberiaManejarEscenariosDeOrganizacionEscolar() {
        // Educación General Básica
        cursoDTO.setId(1);
        cursoDTO.setIdAnioLectivo(1);
        cursoDTO.setNombreCurso("5° EGB");
        
        assertThat(cursoDTO.getNombreCurso()).contains("EGB");
        
        // Bachillerato General Unificado
        cursoDTO.setId(2);
        cursoDTO.setIdAnioLectivo(1);
        cursoDTO.setNombreCurso("1° BGU");
        
        assertThat(cursoDTO.getNombreCurso()).contains("BGU");
        
        // Bachillerato Técnico
        cursoDTO.setId(3);
        cursoDTO.setIdAnioLectivo(1);
        cursoDTO.setNombreCurso("2° BT Informática");
        
        assertThat(cursoDTO.getNombreCurso()).contains("BT");
    }

    @Test
    @DisplayName("Debería manejar diferentes configuraciones de paralelos")
    void deberiaManejarDiferentesConfiguracionesDeParalelos() {
        // Configuración con letras
        String[] paralelosConLetras = {"1° A", "1° B", "1° C", "1° D", "1° E"};
        for (int i = 0; i < paralelosConLetras.length; i++) {
            cursoDTO.setId(i + 1);
            cursoDTO.setIdAnioLectivo(1);
            cursoDTO.setNombreCurso(paralelosConLetras[i]);
            
            assertThat(cursoDTO.getNombreCurso()).matches("\\d°\\s[A-E]");
        }
        
        // Configuración con números
        String[] paralelosConNumeros = {"1° Paralelo 1", "1° Paralelo 2", "1° Paralelo 3"};
        for (int i = 0; i < paralelosConNumeros.length; i++) {
            cursoDTO.setNombreCurso(paralelosConNumeros[i]);
            assertThat(cursoDTO.getNombreCurso()).contains("Paralelo");
        }
    }

    @Test
    @DisplayName("Debería validar relación entre curso y año lectivo")
    void deberiaValidarRelacionEntreCursoYAnioLectivo() {
        // Múltiples cursos pueden pertenecer al mismo año lectivo
        Integer anioLectivoActual = 2024;
        
        cursoDTO.setId(1);
        cursoDTO.setIdAnioLectivo(anioLectivoActual);
        cursoDTO.setNombreCurso("1° Bachillerato A");
        
        assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(anioLectivoActual);
        
        // Cambiar a otro curso del mismo año
        cursoDTO.setId(2);
        cursoDTO.setNombreCurso("1° Bachillerato B");
        
        assertThat(cursoDTO.getIdAnioLectivo()).isEqualTo(anioLectivoActual);
        assertThat(cursoDTO.getId()).isEqualTo(2);
        assertThat(cursoDTO.getNombreCurso()).isEqualTo("1° Bachillerato B");
    }
}