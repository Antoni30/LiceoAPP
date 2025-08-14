package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
/*
@DisplayName("Tests para entidad UsuarioCurso")
class UsuarioCursoTest {

    private UsuarioCurso usuarioCurso;
    private Usuario usuario;
    private Curso curso;
    private AnioLectivo anioLectivo;

    @BeforeEach
    void setUp() {
        usuarioCurso = new UsuarioCurso();
        
        // Configurar año lectivo
        anioLectivo = new AnioLectivo();
        anioLectivo.setId(1);
        anioLectivo.setFechaInicio(LocalDate.of(2024, 2, 1));
        anioLectivo.setFechaFinal(LocalDate.of(2024, 11, 30));
        anioLectivo.setEstado("Activo");
        
        // Configurar usuario con cédula válida
        usuario = new Usuario();
        usuario.setCedula("0504110438");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@test.com");
        usuario.setTelefono("0987654321");
        usuario.setPassword("password123");
        
        // Configurar curso
        curso = new Curso();
        curso.setId(1);
        curso.setNombreCurso("1° Bachillerato");
        curso.setAnioLectivo(anioLectivo);
    }

    @Test
    @DisplayName("Debería establecer y obtener usuario correctamente")
    void deberiaEstablecerYObtenerUsuario() {
        usuarioCurso.setUsuario(usuario);
        
        assertThat(usuarioCurso.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioCurso.getUsuario().getCedula()).isEqualTo("0504110438");
        assertThat(usuarioCurso.getUsuario().getNombre()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("Debería establecer y obtener curso correctamente")
    void deberiaEstablecerYObtenerCurso() {
        usuarioCurso.setCurso(curso);
        
        assertThat(usuarioCurso.getCurso()).isEqualTo(curso);
        assertThat(usuarioCurso.getCurso().getId()).isEqualTo(1);
        assertThat(usuarioCurso.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
    }

    @Test
    @DisplayName("Debería crear relación usuario-curso completa correctamente")
    void deberiaCrearRelacionUsuarioCursoCompletaCorrectamente() {
        usuarioCurso.setUsuario(usuario);
        usuarioCurso.setCurso(curso);
        
        assertThat(usuarioCurso.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioCurso.getCurso()).isEqualTo(curso);
        assertThat(usuarioCurso.getUsuario().getNombre()).isEqualTo("Juan");
        assertThat(usuarioCurso.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
    }

    @Test
    @DisplayName("Debería manejar múltiples estudiantes en un curso")
    void deberiaManejarMultiplesEstudiantesEnUnCurso() {
        // Primer estudiante
        Usuario estudiante1 = new Usuario();
        estudiante1.setCedula("1751983014");
        estudiante1.setNombre("María");
        estudiante1.setApellido("González");
        estudiante1.setEmail("maria.gonzalez@test.com");
        
        UsuarioCurso usuarioCurso1 = new UsuarioCurso();
        usuarioCurso1.setUsuario(estudiante1);
        usuarioCurso1.setCurso(curso);
        
        // Segundo estudiante
        Usuario estudiante2 = new Usuario();
        estudiante2.setCedula("1722070560");
        estudiante2.setNombre("Carlos");
        estudiante2.setApellido("Rodríguez");
        estudiante2.setEmail("carlos.rodriguez@test.com");
        
        UsuarioCurso usuarioCurso2 = new UsuarioCurso();
        usuarioCurso2.setUsuario(estudiante2);
        usuarioCurso2.setCurso(curso);
        
        // Verificaciones
        assertThat(usuarioCurso1.getCurso()).isEqualTo(curso);
        assertThat(usuarioCurso1.getUsuario().getNombre()).isEqualTo("María");
        
        assertThat(usuarioCurso2.getCurso()).isEqualTo(curso);
        assertThat(usuarioCurso2.getUsuario().getNombre()).isEqualTo("Carlos");
        
        // Ambas relaciones apuntan al mismo curso pero diferentes usuarios
        assertThat(usuarioCurso1.getCurso().getId()).isEqualTo(usuarioCurso2.getCurso().getId());
        assertThat(usuarioCurso1.getUsuario().getCedula()).isNotEqualTo(usuarioCurso2.getUsuario().getCedula());
    }

    @Test
    @DisplayName("Debería manejar un estudiante inscrito en múltiples cursos")
    void deberiaManejarUnEstudianteInscritoEnMultiplesCursos() {
        // Curso 2° Bachillerato
        Curso segundoBachillerato = new Curso();
        segundoBachillerato.setId(2);
        segundoBachillerato.setNombreCurso("2° Bachillerato");
        segundoBachillerato.setAnioLectivo(anioLectivo);
        
        // Usuario en 1° Bachillerato
        UsuarioCurso usuarioCurso1 = new UsuarioCurso();
        usuarioCurso1.setUsuario(usuario);
        usuarioCurso1.setCurso(curso);
        
        // Usuario en 2° Bachillerato (caso de estudiante repitente o con materias pendientes)
        UsuarioCurso usuarioCurso2 = new UsuarioCurso();
        usuarioCurso2.setUsuario(usuario);
        usuarioCurso2.setCurso(segundoBachillerato);
        
        // Verificaciones
        assertThat(usuarioCurso1.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioCurso2.getUsuario()).isEqualTo(usuario);
        
        assertThat(usuarioCurso1.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
        assertThat(usuarioCurso2.getCurso().getNombreCurso()).isEqualTo("2° Bachillerato");
        
        // Ambas relaciones apuntan al mismo usuario pero diferentes cursos
        assertThat(usuarioCurso1.getUsuario().getCedula()).isEqualTo(usuarioCurso2.getUsuario().getCedula());
        assertThat(usuarioCurso1.getCurso().getId()).isNotEqualTo(usuarioCurso2.getCurso().getId());
    }

    @Test
    @DisplayName("Debería manejar diferentes tipos de usuarios en cursos")
    void deberiaManejarDiferentesTiposDeUsuariosEnCursos() {
        // Estudiante
        Usuario estudiante = new Usuario();
        estudiante.setCedula("1756916233");
        estudiante.setNombre("Ana");
        estudiante.setApellido("López");
        estudiante.setEmail("ana.lopez@estudiante.com");
        
        UsuarioCurso estudianteEnCurso = new UsuarioCurso();
        estudianteEnCurso.setUsuario(estudiante);
        estudianteEnCurso.setCurso(curso);
        
        // Docente
        Usuario docente = new Usuario();
        docente.setCedula("1754392635");
        docente.setNombre("Dr. Roberto");
        docente.setApellido("Martínez");
        docente.setEmail("roberto.martinez@docente.com");
        
        UsuarioCurso docenteEnCurso = new UsuarioCurso();
        docenteEnCurso.setUsuario(docente);
        docenteEnCurso.setCurso(curso);
        
        // Verificaciones
        assertThat(estudianteEnCurso.getUsuario().getNombre()).isEqualTo("Ana");
        assertThat(estudianteEnCurso.getCurso()).isEqualTo(curso);
        
        assertThat(docenteEnCurso.getUsuario().getNombre()).isEqualTo("Dr. Roberto");
        assertThat(docenteEnCurso.getCurso()).isEqualTo(curso);
        
        // Ambos están en el mismo curso pero son usuarios diferentes
        assertThat(estudianteEnCurso.getCurso().getId()).isEqualTo(docenteEnCurso.getCurso().getId());
        assertThat(estudianteEnCurso.getUsuario().getCedula()).isNotEqualTo(docenteEnCurso.getUsuario().getCedula());
    }

    @Test
    @DisplayName("Debería permitir cambiar usuario en la relación")
    void deberiaPermitirCambiarUsuarioEnLaRelacion() {
        // Configurar relación inicial
        usuarioCurso.setUsuario(usuario);
        usuarioCurso.setCurso(curso);
        
        assertThat(usuarioCurso.getUsuario().getNombre()).isEqualTo("Juan");
        
        // Cambiar a otro usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCedula("1751983014");
        nuevoUsuario.setNombre("Pedro");
        nuevoUsuario.setApellido("Silva");
        nuevoUsuario.setEmail("pedro.silva@test.com");
        
        usuarioCurso.setUsuario(nuevoUsuario);
        
        assertThat(usuarioCurso.getUsuario()).isEqualTo(nuevoUsuario);
        assertThat(usuarioCurso.getUsuario().getNombre()).isEqualTo("Pedro");
        assertThat(usuarioCurso.getCurso()).isEqualTo(curso); // El curso se mantiene
    }

    @Test
    @DisplayName("Debería permitir cambiar curso en la relación")
    void deberiaPermitirCambiarCursoEnLaRelacion() {
        // Configurar relación inicial
        usuarioCurso.setUsuario(usuario);
        usuarioCurso.setCurso(curso);
        
        assertThat(usuarioCurso.getCurso().getNombreCurso()).isEqualTo("1° Bachillerato");
        
        // Cambiar a otro curso
        Curso nuevoCurso = new Curso();
        nuevoCurso.setId(2);
        nuevoCurso.setNombreCurso("3° Bachillerato");
        nuevoCurso.setAnioLectivo(anioLectivo);
        
        usuarioCurso.setCurso(nuevoCurso);
        
        assertThat(usuarioCurso.getCurso()).isEqualTo(nuevoCurso);
        assertThat(usuarioCurso.getCurso().getNombreCurso()).isEqualTo("3° Bachillerato");
        assertThat(usuarioCurso.getUsuario()).isEqualTo(usuario); // El usuario se mantiene
    }

    @Test
    @DisplayName("Debería validar integridad de la relación usuario-curso")
    void deberiaValidarIntegridadDeLaRelacionUsuarioCurso() {
        usuarioCurso.setUsuario(usuario);
        usuarioCurso.setCurso(curso);
        
        // Verificar que tanto usuario como curso están presentes
        assertThat(usuarioCurso.getUsuario()).isNotNull();
        assertThat(usuarioCurso.getCurso()).isNotNull();
        
        // Verificar que tienen identificadores válidos
        assertThat(usuarioCurso.getUsuario().getCedula()).isNotNull().isNotEmpty();
        assertThat(usuarioCurso.getCurso().getId()).isNotNull();
        
        // Verificar que tienen nombres válidos
        assertThat(usuarioCurso.getUsuario().getNombre()).isNotEmpty();
        assertThat(usuarioCurso.getCurso().getNombreCurso()).isNotEmpty();
        
        // Verificar que el curso tiene un año lectivo asociado
        assertThat(usuarioCurso.getCurso().getAnioLectivo()).isNotNull();
        assertThat(usuarioCurso.getCurso().getAnioLectivo().getEstado()).isEqualTo("Activo");
        
        // Verificar que el usuario tiene información básica completa
        assertThat(usuarioCurso.getUsuario().getApellido()).isNotEmpty();
        assertThat(usuarioCurso.getUsuario().getEmail()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería manejar estudiantes con cédulas válidas ecuatorianas")
    void deberiaManejarEstudiantesConCedulasValidasEcuatorianas() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (int i = 0; i < cedulasValidas.length; i++) {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCedula(cedulasValidas[i]);
            nuevoUsuario.setNombre("Estudiante" + (i + 1));
            nuevoUsuario.setApellido("Apellido" + (i + 1));
            nuevoUsuario.setEmail("estudiante" + (i + 1) + "@test.com");
            
            UsuarioCurso nuevaRelacion = new UsuarioCurso();
            nuevaRelacion.setUsuario(nuevoUsuario);
            nuevaRelacion.setCurso(curso);
            
            assertThat(nuevaRelacion.getUsuario().getCedula()).isEqualTo(cedulasValidas[i]);
            assertThat(nuevaRelacion.getCurso()).isEqualTo(curso);
        }
    }
}*/