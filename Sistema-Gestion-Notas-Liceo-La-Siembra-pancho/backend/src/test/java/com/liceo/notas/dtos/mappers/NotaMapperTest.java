package com.liceo.notas.dtos.mappers;
/*
import com.liceo.notas.dtos.NotaDTO;
import com.liceo.notas.entities.Nota;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.Materia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para NotaMapper")
class NotaMapperTest {

    @Test
    @DisplayName("Debería convertir entidad a DTO correctamente")
    void deberiaConvertirEntidadADtoCorrectamente() {
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("0504110438");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        
        // Crear materia
        Materia materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Matemáticas");
        
        // Crear entidad nota
        Nota entidad = new Nota();
        entidad.setId(1);
        entidad.setUsuario(usuario);
        entidad.setMateria(materia);
        entidad.setNota(18.5);
        entidad.setParcial(2);
        
        // Convertir a DTO
        NotaDTO dto = NotaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getIdUsuario()).isEqualTo(usuario.getCedula());
        assertThat(dto.getIdMateria()).isEqualTo(materia.getId());
        assertThat(dto.getNota()).isEqualTo(entidad.getNota());
        assertThat(dto.getParcial()).isEqualTo(entidad.getParcial());
    }

    @Test
    @DisplayName("Debería convertir DTO a entidad correctamente")
    void deberiaConvertirDtoAEntidadCorrectamente() {
        // Crear DTO
        NotaDTO dto = new NotaDTO();
        dto.setId(1);
        dto.setIdUsuario("0504110438");
        dto.setIdMateria(1);
        dto.setNota(17.5);
        dto.setParcial(1);
        
        // Convertir a entidad
        Nota entidad = NotaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isEqualTo(dto.getId());
        assertThat(entidad.getNota()).isEqualTo(dto.getNota());
        assertThat(entidad.getParcial()).isEqualTo(dto.getParcial());
        // Nota: Usuario y Materia no se mapean en toEntity según el código
        assertThat(entidad.getUsuario()).isNull();
        assertThat(entidad.getMateria()).isNull();
    }

    @Test
    @DisplayName("Debería retornar null cuando entidad es null")
    void deberiaRetornarNullCuandoEntidadEsNull() {
        NotaDTO dto = NotaMapper.toDTO(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Debería retornar null cuando DTO es null")
    void deberiaRetornarNullCuandoDtoEsNull() {
        Nota entidad = NotaMapper.toEntity(null);
        assertThat(entidad).isNull();
    }

    @Test
    @DisplayName("Debería manejar entidad con usuario null")
    void deberiaManejarEntidadConUsuarioNull() {
        // Crear materia
        Materia materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Física");
        
        // Crear entidad sin usuario
        Nota entidad = new Nota();
        entidad.setId(1);
        entidad.setUsuario(null);
        entidad.setMateria(materia);
        entidad.setNota(15.0);
        entidad.setParcial(1);
        
        // Convertir a DTO
        NotaDTO dto = NotaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getIdUsuario()).isNull();
        assertThat(dto.getIdMateria()).isEqualTo(materia.getId());
        assertThat(dto.getNota()).isEqualTo(entidad.getNota());
        assertThat(dto.getParcial()).isEqualTo(entidad.getParcial());
    }

    @Test
    @DisplayName("Debería manejar entidad con materia null")
    void deberiaManejarEntidadConMateriaNull() {
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("1751983014");
        usuario.setNombre("María");
        usuario.setApellido("González");
        
        // Crear entidad sin materia
        Nota entidad = new Nota();
        entidad.setId(1);
        entidad.setUsuario(usuario);
        entidad.setMateria(null);
        entidad.setNota(19.0);
        entidad.setParcial(3);
        
        // Convertir a DTO
        NotaDTO dto = NotaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getIdUsuario()).isEqualTo(usuario.getCedula());
        assertThat(dto.getIdMateria()).isNull();
        assertThat(dto.getNota()).isEqualTo(entidad.getNota());
        assertThat(dto.getParcial()).isEqualTo(entidad.getParcial());
    }

    @Test
    @DisplayName("Debería manejar entidad con todos los campos nulos excepto IDs")
    void deberiaManejarEntidadConTodosLosCamposNulosExceptoIds() {
        // Crear entidad con campos básicos
        Nota entidad = new Nota();
        entidad.setId(1);
        entidad.setUsuario(null);
        entidad.setMateria(null);
        entidad.setNota(null);
        entidad.setParcial(null);
        
        // Convertir a DTO
        NotaDTO dto = NotaMapper.toDTO(entidad);
        
        // Verificaciones
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entidad.getId());
        assertThat(dto.getIdUsuario()).isNull();
        assertThat(dto.getIdMateria()).isNull();
        assertThat(dto.getNota()).isNull();
        assertThat(dto.getParcial()).isNull();
    }

    @Test
    @DisplayName("Debería manejar DTO con campos nulos")
    void deberiaManejarDtoConCamposNulos() {
        // Crear DTO con campos nulos
        NotaDTO dto = new NotaDTO();
        dto.setId(null);
        dto.setIdUsuario(null);
        dto.setIdMateria(null);
        dto.setNota(null);
        dto.setParcial(null);
        
        // Convertir a entidad
        Nota entidad = NotaMapper.toEntity(dto);
        
        // Verificaciones
        assertThat(entidad).isNotNull();
        assertThat(entidad.getId()).isNull();
        assertThat(entidad.getNota()).isNull();
        assertThat(entidad.getParcial()).isNull();
        assertThat(entidad.getUsuario()).isNull();
        assertThat(entidad.getMateria()).isNull();
    }

    @Test
    @DisplayName("Debería manejar diferentes valores de nota válidos")
    void deberiaManejarDiferentesValoresDeNotaValidos() {
        Double[] notasValidas = {0.0, 7.0, 10.5, 15.75, 18.25, 20.0};
        
        for (Double nota : notasValidas) {
            // Crear entidad
            Nota entidad = new Nota();
            entidad.setId(1);
            entidad.setNota(nota);
            entidad.setParcial(1);
            
            // Convertir a DTO
            NotaDTO dto = NotaMapper.toDTO(entidad);
            
            // Verificar nota
            assertThat(dto.getNota()).isEqualTo(nota);
            assertThat(dto.getNota()).isBetween(0.0, 20.0);
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes parciales válidos")
    void deberiaManejarDiferentesParcialesValidos() {
        Integer[] parcialesValidos = {1, 2, 3};
        
        for (Integer parcial : parcialesValidos) {
            // Crear entidad
            Nota entidad = new Nota();
            entidad.setId(1);
            entidad.setNota(15.0);
            entidad.setParcial(parcial);
            
            // Convertir a DTO
            NotaDTO dto = NotaMapper.toDTO(entidad);
            
            // Verificar parcial
            assertThat(dto.getParcial()).isEqualTo(parcial);
            assertThat(dto.getParcial()).isBetween(1, 3);
        }
    }

    @Test
    @DisplayName("Debería manejar cédulas ecuatorianas válidas")
    void deberiaManejarCedulasEcuatorianasValidas() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (String cedula : cedulasValidas) {
            // Crear usuario con cédula válida
            Usuario usuario = new Usuario();
            usuario.setCedula(cedula);
            
            // Crear entidad
            Nota entidad = new Nota();
            entidad.setId(1);
            entidad.setUsuario(usuario);
            entidad.setNota(16.0);
            entidad.setParcial(2);
            
            // Convertir a DTO
            NotaDTO dto = NotaMapper.toDTO(entidad);
            
            // Verificar cédula
            assertThat(dto.getIdUsuario()).isEqualTo(cedula);
            assertThat(dto.getIdUsuario().length()).isEqualTo(10);
        }
    }

    @Test
    @DisplayName("Debería manejar múltiples materias")
    void deberiaManejarMultiplesMaterias() {
        String[] nombresMaterias = {
            "Matemáticas", "Física", "Química", "Biología", "Historia", "Lenguaje"
        };
        
        for (int i = 0; i < nombresMaterias.length; i++) {
            // Crear materia
            Materia materia = new Materia();
            materia.setId(i + 1);
            materia.setNombreMateria(nombresMaterias[i]);
            
            // Crear entidad
            Nota entidad = new Nota();
            entidad.setId(1);
            entidad.setMateria(materia);
            entidad.setNota(15.0);
            entidad.setParcial(1);
            
            // Convertir a DTO
            NotaDTO dto = NotaMapper.toDTO(entidad);
            
            // Verificar materia
            assertThat(dto.getIdMateria()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("Debería manejar conversión bidireccional correctamente")
    void deberiaManejarConversionBidireccionalCorrectamente() {
        // Crear DTO original
        NotaDTO dtoOriginal = new NotaDTO();
        dtoOriginal.setId(1);
        dtoOriginal.setIdUsuario("0504110438");
        dtoOriginal.setIdMateria(1);
        dtoOriginal.setNota(17.5);
        dtoOriginal.setParcial(2);
        
        // Convertir a entidad y de vuelta a DTO
        Nota entidad = NotaMapper.toEntity(dtoOriginal);
        
        // Crear entidad completa para prueba completa
        Usuario usuario = new Usuario();
        usuario.setCedula(dtoOriginal.getIdUsuario());
        
        Materia materia = new Materia();
        materia.setId(dtoOriginal.getIdMateria());
        
        entidad.setUsuario(usuario);
        entidad.setMateria(materia);
        
        NotaDTO dtoConvertido = NotaMapper.toDTO(entidad);
        
        // Verificar que los datos básicos se mantienen
        assertThat(dtoConvertido.getId()).isEqualTo(dtoOriginal.getId());
        assertThat(dtoConvertido.getIdUsuario()).isEqualTo(dtoOriginal.getIdUsuario());
        assertThat(dtoConvertido.getIdMateria()).isEqualTo(dtoOriginal.getIdMateria());
        assertThat(dtoConvertido.getNota()).isEqualTo(dtoOriginal.getNota());
        assertThat(dtoConvertido.getParcial()).isEqualTo(dtoOriginal.getParcial());
    }

    @Test
    @DisplayName("Debería manejar escenarios de calificaciones típicas")
    void deberiaManejarEscenariosDeCalificacionesTipicas() {
        // Nota reprobatoria
        Nota notaReprobatoria = new Nota();
        notaReprobatoria.setId(1);
        notaReprobatoria.setNota(6.0);
        notaReprobatoria.setParcial(1);
        
        NotaDTO dtoReprobatoria = NotaMapper.toDTO(notaReprobatoria);
        assertThat(dtoReprobatoria.getNota()).isLessThan(7.0);
        
        // Nota aprobatoria
        Nota notaAprobatoria = new Nota();
        notaAprobatoria.setId(2);
        notaAprobatoria.setNota(14.0);
        notaAprobatoria.setParcial(2);
        
        NotaDTO dtoAprobatoria = NotaMapper.toDTO(notaAprobatoria);
        assertThat(dtoAprobatoria.getNota()).isGreaterThanOrEqualTo(7.0);
        
        // Nota excelente
        Nota notaExcelente = new Nota();
        notaExcelente.setId(3);
        notaExcelente.setNota(19.5);
        notaExcelente.setParcial(3);
        
        NotaDTO dtoExcelente = NotaMapper.toDTO(notaExcelente);
        assertThat(dtoExcelente.getNota()).isGreaterThan(18.0);
    }

    @Test
    @DisplayName("Debería preservar precisión de decimales en notas")
    void deberiaPreservarPrecisionDeDecimalesEnNotas() {
        Double[] notasConDecimales = {7.25, 8.75, 12.33, 15.67, 18.99};
        
        for (Double nota : notasConDecimales) {
            // Crear entidad
            Nota entidad = new Nota();
            entidad.setId(1);
            entidad.setNota(nota);
            entidad.setParcial(1);
            
            // Convertir a DTO
            NotaDTO dto = NotaMapper.toDTO(entidad);
            
            // Verificar precisión
            assertThat(dto.getNota()).isEqualTo(nota);
        }
    }

    @Test
    @DisplayName("Debería validar que el mapeo no altera datos originales")
    void deberiaValidarQueElMapeoNoAlteraDatosOriginales() {
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("1722070560");
        usuario.setNombre("Carlos");
        
        // Crear materia
        Materia materia = new Materia();
        materia.setId(2);
        materia.setNombreMateria("Química");
        
        // Crear entidad original
        Nota entidadOriginal = new Nota();
        entidadOriginal.setId(5);
        entidadOriginal.setUsuario(usuario);
        entidadOriginal.setMateria(materia);
        entidadOriginal.setNota(16.75);
        entidadOriginal.setParcial(2);
        
        // Convertir a DTO
        NotaDTO dto = NotaMapper.toDTO(entidadOriginal);
        
        // Verificar que la entidad original no cambió
        assertThat(entidadOriginal.getId()).isEqualTo(5);
        assertThat(entidadOriginal.getUsuario().getCedula()).isEqualTo("1722070560");
        assertThat(entidadOriginal.getMateria().getId()).isEqualTo(2);
        assertThat(entidadOriginal.getNota()).isEqualTo(16.75);
        assertThat(entidadOriginal.getParcial()).isEqualTo(2);
        
        // Verificar que el DTO tiene los datos correctos
        assertThat(dto.getId()).isEqualTo(5);
        assertThat(dto.getIdUsuario()).isEqualTo("1722070560");
        assertThat(dto.getIdMateria()).isEqualTo(2);
        assertThat(dto.getNota()).isEqualTo(16.75);
        assertThat(dto.getParcial()).isEqualTo(2);
    }
}*/