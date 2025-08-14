package com.liceo.notas.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para NotaDTO")
class NotaDTOTest {

    private NotaDTO notaDTO;

    @BeforeEach
    void setUp() {
        notaDTO = new NotaDTO();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        notaDTO.setId(id);
        
        assertThat(notaDTO.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener ID de usuario correctamente")
    void deberiaEstablecerYObtenerIdUsuario() {
        String idUsuario = "0504110438";
        notaDTO.setIdUsuario(idUsuario);
        
        assertThat(notaDTO.getIdUsuario()).isEqualTo(idUsuario);
    }

    @Test
    @DisplayName("Debería establecer y obtener ID de materia correctamente")
    void deberiaEstablecerYObtenerIdMateria() {
        Integer idMateria = 1;
        notaDTO.setIdMateria(idMateria);
        
        assertThat(notaDTO.getIdMateria()).isEqualTo(idMateria);
    }

    @Test
    @DisplayName("Debería establecer y obtener nota correctamente")
    void deberiaEstablecerYObtenerNota() {
        Double nota = 18.5;
        notaDTO.setNota(nota);
        
        assertThat(notaDTO.getNota()).isEqualTo(nota);
    }

    @Test
    @DisplayName("Debería establecer y obtener parcial correctamente")
    void deberiaEstablecerYObtenerParcial() {
        Integer parcial = 1;
        notaDTO.setParcial(parcial);
        
        assertThat(notaDTO.getParcial()).isEqualTo(parcial);
    }

    @Test
    @DisplayName("Debería crear DTO completo correctamente")
    void deberiaCrearDtoCompletoCorrectamente() {
        Integer id = 1;
        String idUsuario = "0504110438";
        Integer idMateria = 1;
        Double nota = 17.5;
        Integer parcial = 2;
        
        notaDTO.setId(id);
        notaDTO.setIdUsuario(idUsuario);
        notaDTO.setIdMateria(idMateria);
        notaDTO.setNota(nota);
        notaDTO.setParcial(parcial);
        
        assertThat(notaDTO.getId()).isEqualTo(id);
        assertThat(notaDTO.getIdUsuario()).isEqualTo(idUsuario);
        assertThat(notaDTO.getIdMateria()).isEqualTo(idMateria);
        assertThat(notaDTO.getNota()).isEqualTo(nota);
        assertThat(notaDTO.getParcial()).isEqualTo(parcial);
    }

    @Test
    @DisplayName("Debería manejar notas válidas en el rango 0-20")
    void deberiaManejarNotasValidasEnElRango0A20() {
        Double[] notasValidas = {0.0, 5.5, 10.0, 15.75, 20.0};
        
        for (Double nota : notasValidas) {
            notaDTO.setNota(nota);
            assertThat(notaDTO.getNota()).isEqualTo(nota);
            assertThat(notaDTO.getNota()).isBetween(0.0, 20.0);
        }
    }

    @Test
    @DisplayName("Debería manejar parciales válidos del 1 al 3")
    void deberiaManejarParcialesValidosDel1Al3() {
        Integer[] parcialesValidos = {1, 2, 3};
        
        for (Integer parcial : parcialesValidos) {
            notaDTO.setParcial(parcial);
            assertThat(notaDTO.getParcial()).isEqualTo(parcial);
            assertThat(notaDTO.getParcial()).isBetween(1, 3);
        }
    }

    @Test
    @DisplayName("Debería manejar cédulas ecuatorianas válidas como ID de usuario")
    void deberiaManejarCedulasEcuatorianasValidasComoIdDeUsuario() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (String cedula : cedulasValidas) {
            notaDTO.setIdUsuario(cedula);
            assertThat(notaDTO.getIdUsuario()).isEqualTo(cedula);
            assertThat(notaDTO.getIdUsuario().length()).isEqualTo(10);
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes materias del sistema educativo")
    void deberiaManejarDiferentesMateriasDelSistemaEducativo() {
        Integer[] materiasIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        for (Integer materiaId : materiasIds) {
            notaDTO.setIdMateria(materiaId);
            assertThat(notaDTO.getIdMateria()).isEqualTo(materiaId);
            assertThat(notaDTO.getIdMateria()).isPositive();
        }
    }

    @Test
    @DisplayName("Debería manejar escenarios típicos de calificaciones")
    void deberiaManejarEscenariosTipicosDeCalificaciones() {
        // Nota reprobatoria
        notaDTO.setNota(6.0);
        assertThat(notaDTO.getNota()).isLessThan(7.0); // Nota de reprobación
        
        // Nota aprobatoria mínima
        notaDTO.setNota(7.0);
        assertThat(notaDTO.getNota()).isGreaterThanOrEqualTo(7.0);
        
        // Nota excelente
        notaDTO.setNota(19.0);
        assertThat(notaDTO.getNota()).isGreaterThan(18.0);
        
        // Nota perfecta
        notaDTO.setNota(20.0);
        assertThat(notaDTO.getNota()).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Debería manejar notas con decimales precisos")
    void deberiaManejarNotasConDecimalesPrecisos() {
        Double[] notasConDecimales = {7.25, 8.5, 9.75, 12.33, 15.67, 18.99};
        
        for (Double nota : notasConDecimales) {
            notaDTO.setNota(nota);
            assertThat(notaDTO.getNota()).isEqualTo(nota);
            assertThat(notaDTO.getNota()).isBetween(0.0, 20.0);
        }
    }

    @Test
    @DisplayName("Debería manejar secuencia completa de parciales para un estudiante")
    void deberiaManejarSecuenciaCompletaDeParciales() {
        String idUsuario = "0504110438";
        Integer idMateria = 1;
        
        // Primer parcial
        notaDTO.setIdUsuario(idUsuario);
        notaDTO.setIdMateria(idMateria);
        notaDTO.setParcial(1);
        notaDTO.setNota(15.0);
        
        assertThat(notaDTO.getParcial()).isEqualTo(1);
        assertThat(notaDTO.getNota()).isEqualTo(15.0);
        
        // Segundo parcial
        notaDTO.setParcial(2);
        notaDTO.setNota(17.5);
        
        assertThat(notaDTO.getParcial()).isEqualTo(2);
        assertThat(notaDTO.getNota()).isEqualTo(17.5);
        
        // Tercer parcial
        notaDTO.setParcial(3);
        notaDTO.setNota(19.0);
        
        assertThat(notaDTO.getParcial()).isEqualTo(3);
        assertThat(notaDTO.getNota()).isEqualTo(19.0);
    }

    @Test
    @DisplayName("Debería manejar múltiples estudiantes y materias")
    void deberiaManejarMultiplesEstudiantesYMaterias() {
        String[] estudiantes = {"0504110438", "1751983014", "1722070560"};
        Integer[] materias = {1, 2, 3, 4, 5};
        
        for (String estudiante : estudiantes) {
            for (Integer materia : materias) {
                notaDTO.setIdUsuario(estudiante);
                notaDTO.setIdMateria(materia);
                notaDTO.setNota(15.0);
                notaDTO.setParcial(1);
                
                assertThat(notaDTO.getIdUsuario()).isEqualTo(estudiante);
                assertThat(notaDTO.getIdMateria()).isEqualTo(materia);
            }
        }
    }

    @Test
    @DisplayName("Debería manejar valores nulos en campos opcionales")
    void deberiaManejarValoresNulosEnCamposOpcionales() {
        // ID puede ser nulo para nuevos registros
        notaDTO.setId(null);
        assertThat(notaDTO.getId()).isNull();
        
        // Campos requeridos pueden ser nulos temporalmente durante construcción
        notaDTO.setIdUsuario(null);
        notaDTO.setIdMateria(null);
        notaDTO.setNota(null);
        notaDTO.setParcial(null);
        
        assertThat(notaDTO.getIdUsuario()).isNull();
        assertThat(notaDTO.getIdMateria()).isNull();
        assertThat(notaDTO.getNota()).isNull();
        assertThat(notaDTO.getParcial()).isNull();
    }

    @Test
    @DisplayName("Debería implementar equals y hashCode por Lombok correctamente")
    void deberiaImplementarEqualsYHashCodePorLombokCorrectamente() {
        NotaDTO dto1 = new NotaDTO();
        dto1.setId(1);
        dto1.setIdUsuario("0504110438");
        dto1.setIdMateria(1);
        dto1.setNota(18.5);
        dto1.setParcial(2);
        
        NotaDTO dto2 = new NotaDTO();
        dto2.setId(1);
        dto2.setIdUsuario("0504110438");
        dto2.setIdMateria(1);
        dto2.setNota(18.5);
        dto2.setParcial(2);
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar toString por Lombok correctamente")
    void deberiaImplementarToStringPorLombokCorrectamente() {
        notaDTO.setId(1);
        notaDTO.setIdUsuario("0504110438");
        notaDTO.setIdMateria(1);
        notaDTO.setNota(18.5);
        notaDTO.setParcial(2);
        
        String resultado = notaDTO.toString();
        
        assertThat(resultado).contains("NotaDTO");
        assertThat(resultado).contains("id=1");
        assertThat(resultado).contains("idUsuario=0504110438");
        assertThat(resultado).contains("idMateria=1");
        assertThat(resultado).contains("nota=18.5");
        assertThat(resultado).contains("parcial=2");
    }

    @Test
    @DisplayName("Debería ser utilizable como DTO de transferencia de datos")
    void deberiaSerUtilizableComoDtoDeTransferenciaDeDatos() {
        // Simular datos recibidos desde una API REST
        notaDTO.setId(1);
        notaDTO.setIdUsuario("0504110438");
        notaDTO.setIdMateria(1);
        notaDTO.setNota(16.75);
        notaDTO.setParcial(1);
        
        // Verificar que todos los datos están disponibles
        assertThat(notaDTO.getId()).isNotNull();
        assertThat(notaDTO.getIdUsuario()).isNotNull();
        assertThat(notaDTO.getIdMateria()).isNotNull();
        assertThat(notaDTO.getNota()).isNotNull();
        assertThat(notaDTO.getParcial()).isNotNull();
        
        // Verificar coherencia de datos
        assertThat(notaDTO.getNota()).isBetween(0.0, 20.0);
        assertThat(notaDTO.getParcial()).isBetween(1, 3);
        assertThat(notaDTO.getIdUsuario().length()).isEqualTo(10);
    }

    @Test
    @DisplayName("Debería manejar casos extremos de calificaciones")
    void deberiaManejarCasosExtremosDeCalificaciones() {
        // Nota mínima posible
        notaDTO.setNota(0.0);
        assertThat(notaDTO.getNota()).isEqualTo(0.0);
        
        // Nota máxima posible
        notaDTO.setNota(20.0);
        assertThat(notaDTO.getNota()).isEqualTo(20.0);
        
        // Primer parcial
        notaDTO.setParcial(1);
        assertThat(notaDTO.getParcial()).isEqualTo(1);
        
        // Último parcial
        notaDTO.setParcial(3);
        assertThat(notaDTO.getParcial()).isEqualTo(3);
    }
}