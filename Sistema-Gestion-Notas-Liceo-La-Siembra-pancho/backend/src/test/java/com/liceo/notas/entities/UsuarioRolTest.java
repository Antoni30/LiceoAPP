package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
/*
@DisplayName("Tests para entidad UsuarioRol")
class UsuarioRolTest {

    private UsuarioRol usuarioRol;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        usuarioRol = new UsuarioRol();
        
        // Configurar usuario con cédula válida
        usuario = new Usuario();
        usuario.setCedula("0504110438");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@test.com");
        usuario.setTelefono("0987654321");
        usuario.setPassword("password123");
        
        // Configurar rol
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("ESTUDIANTE");
    }

    @Test
    @DisplayName("Debería establecer y obtener usuario correctamente")
    void deberiaEstablecerYObtenerUsuario() {
        usuarioRol.setUsuario(usuario);
        
        assertThat(usuarioRol.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioRol.getUsuario().getCedula()).isEqualTo("0504110438");
        assertThat(usuarioRol.getUsuario().getNombre()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("Debería establecer y obtener rol correctamente")
    void deberiaEstablecerYObtenerRol() {
        usuarioRol.setRol(rol);
        
        assertThat(usuarioRol.getRol()).isEqualTo(rol);
        assertThat(usuarioRol.getRol().getId()).isEqualTo(1);
        assertThat(usuarioRol.getRol().getNombre()).isEqualTo("ESTUDIANTE");
    }

    @Test
    @DisplayName("Debería crear relación usuario-rol completa correctamente")
    void deberiaCrearRelacionUsuarioRolCompletaCorrectamente() {
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        
        assertThat(usuarioRol.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioRol.getRol()).isEqualTo(rol);
        assertThat(usuarioRol.getUsuario().getNombre()).isEqualTo("Juan");
        assertThat(usuarioRol.getRol().getNombre()).isEqualTo("ESTUDIANTE");
    }

    @Test
    @DisplayName("Debería manejar múltiples usuarios con el mismo rol")
    void deberiaManejarMultiplesUsuariosConElMismoRol() {
        // Primer usuario con rol estudiante
        Usuario estudiante1 = new Usuario();
        estudiante1.setCedula("1751983014");
        estudiante1.setNombre("María");
        estudiante1.setApellido("González");
        estudiante1.setEmail("maria.gonzalez@test.com");
        
        UsuarioRol usuarioRol1 = new UsuarioRol();
        usuarioRol1.setUsuario(estudiante1);
        usuarioRol1.setRol(rol);
        
        // Segundo usuario con rol estudiante
        Usuario estudiante2 = new Usuario();
        estudiante2.setCedula("1722070560");
        estudiante2.setNombre("Carlos");
        estudiante2.setApellido("Rodríguez");
        estudiante2.setEmail("carlos.rodriguez@test.com");
        
        UsuarioRol usuarioRol2 = new UsuarioRol();
        usuarioRol2.setUsuario(estudiante2);
        usuarioRol2.setRol(rol);
        
        // Verificaciones
        assertThat(usuarioRol1.getRol()).isEqualTo(rol);
        assertThat(usuarioRol1.getUsuario().getNombre()).isEqualTo("María");
        
        assertThat(usuarioRol2.getRol()).isEqualTo(rol);
        assertThat(usuarioRol2.getUsuario().getNombre()).isEqualTo("Carlos");
        
        // Ambas relaciones apuntan al mismo rol pero diferentes usuarios
        assertThat(usuarioRol1.getRol().getId()).isEqualTo(usuarioRol2.getRol().getId());
        assertThat(usuarioRol1.getUsuario().getCedula()).isNotEqualTo(usuarioRol2.getUsuario().getCedula());
    }

    @Test
    @DisplayName("Debería manejar un usuario con múltiples roles")
    void deberiaManejarUnUsuarioConMultiplesRoles() {
        // Rol docente
        Rol rolDocente = new Rol();
        rolDocente.setId(2);
        rolDocente.setNombre("DOCENTE");
        
        // Usuario con rol estudiante
        UsuarioRol usuarioRolEstudiante = new UsuarioRol();
        usuarioRolEstudiante.setUsuario(usuario);
        usuarioRolEstudiante.setRol(rol);
        
        // Usuario con rol docente (caso de un estudiante que también es auxiliar de cátedra)
        UsuarioRol usuarioRolDocente = new UsuarioRol();
        usuarioRolDocente.setUsuario(usuario);
        usuarioRolDocente.setRol(rolDocente);
        
        // Verificaciones
        assertThat(usuarioRolEstudiante.getUsuario()).isEqualTo(usuario);
        assertThat(usuarioRolDocente.getUsuario()).isEqualTo(usuario);
        
        assertThat(usuarioRolEstudiante.getRol().getNombre()).isEqualTo("ESTUDIANTE");
        assertThat(usuarioRolDocente.getRol().getNombre()).isEqualTo("DOCENTE");
        
        // Ambas relaciones apuntan al mismo usuario pero diferentes roles
        assertThat(usuarioRolEstudiante.getUsuario().getCedula()).isEqualTo(usuarioRolDocente.getUsuario().getCedula());
        assertThat(usuarioRolEstudiante.getRol().getId()).isNotEqualTo(usuarioRolDocente.getRol().getId());
    }

    @Test
    @DisplayName("Debería manejar diferentes tipos de roles del sistema")
    void deberiaManejarDiferentesTiposDeRolesDelSistema() {
        String[] rolesComunes = {
            "ESTUDIANTE", "DOCENTE", "ADMIN", "DIRECTOR", "SECRETARIO"
        };
        
        for (int i = 0; i < rolesComunes.length; i++) {
            Rol nuevoRol = new Rol();
            nuevoRol.setId(i + 1);
            nuevoRol.setNombre(rolesComunes[i]);
            
            UsuarioRol nuevaRelacion = new UsuarioRol();
            nuevaRelacion.setUsuario(usuario);
            nuevaRelacion.setRol(nuevoRol);
            
            assertThat(nuevaRelacion.getUsuario()).isEqualTo(usuario);
            assertThat(nuevaRelacion.getRol().getNombre()).isEqualTo(rolesComunes[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar roles jerárquicos")
    void deberiaManejarRolesJerarquicos() {
        // Estudiante regular
        Usuario estudiante = new Usuario();
        estudiante.setCedula("1756916233");
        estudiante.setNombre("Ana");
        estudiante.setApellido("López");
        estudiante.setEmail("ana.lopez@test.com");
        
        Rol rolEstudiante = new Rol();
        rolEstudiante.setId(1);
        rolEstudiante.setNombre("ESTUDIANTE");
        
        UsuarioRol estudianteRol = new UsuarioRol();
        estudianteRol.setUsuario(estudiante);
        estudianteRol.setRol(rolEstudiante);
        
        // Docente
        Usuario docente = new Usuario();
        docente.setCedula("1754392635");
        docente.setNombre("Dr. Roberto");
        docente.setApellido("Martínez");
        docente.setEmail("roberto.martinez@test.com");
        
        Rol rolDocente = new Rol();
        rolDocente.setId(2);
        rolDocente.setNombre("DOCENTE");
        
        UsuarioRol docenteRol = new UsuarioRol();
        docenteRol.setUsuario(docente);
        docenteRol.setRol(rolDocente);
        
        // Director
        Usuario director = new Usuario();
        director.setCedula("0504110438");
        director.setNombre("Lic. Patricia");
        director.setApellido("Herrera");
        director.setEmail("patricia.herrera@test.com");
        
        Rol rolDirector = new Rol();
        rolDirector.setId(3);
        rolDirector.setNombre("DIRECTOR");
        
        UsuarioRol directorRol = new UsuarioRol();
        directorRol.setUsuario(director);
        directorRol.setRol(rolDirector);
        
        // Verificaciones de jerarquía
        assertThat(estudianteRol.getRol().getNombre()).isEqualTo("ESTUDIANTE");
        assertThat(docenteRol.getRol().getNombre()).isEqualTo("DOCENTE");
        assertThat(directorRol.getRol().getNombre()).isEqualTo("DIRECTOR");
        
        // Verificar que cada usuario tiene su rol específico
        assertThat(estudianteRol.getUsuario().getNombre()).isEqualTo("Ana");
        assertThat(docenteRol.getUsuario().getNombre()).isEqualTo("Dr. Roberto");
        assertThat(directorRol.getUsuario().getNombre()).isEqualTo("Lic. Patricia");
    }

    @Test
    @DisplayName("Debería permitir cambiar usuario en la relación")
    void deberiaPermitirCambiarUsuarioEnLaRelacion() {
        // Configurar relación inicial
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        
        assertThat(usuarioRol.getUsuario().getNombre()).isEqualTo("Juan");
        
        // Cambiar a otro usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCedula("1751983014");
        nuevoUsuario.setNombre("Pedro");
        nuevoUsuario.setApellido("Silva");
        nuevoUsuario.setEmail("pedro.silva@test.com");
        
        usuarioRol.setUsuario(nuevoUsuario);
        
        assertThat(usuarioRol.getUsuario()).isEqualTo(nuevoUsuario);
        assertThat(usuarioRol.getUsuario().getNombre()).isEqualTo("Pedro");
        assertThat(usuarioRol.getRol()).isEqualTo(rol); // El rol se mantiene
    }

    @Test
    @DisplayName("Debería permitir cambiar rol en la relación")
    void deberiaPermitirCambiarRolEnLaRelacion() {
        // Configurar relación inicial
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        
        assertThat(usuarioRol.getRol().getNombre()).isEqualTo("ESTUDIANTE");
        
        // Cambiar a otro rol
        Rol nuevoRol = new Rol();
        nuevoRol.setId(2);
        nuevoRol.setNombre("DOCENTE");
        
        usuarioRol.setRol(nuevoRol);
        
        assertThat(usuarioRol.getRol()).isEqualTo(nuevoRol);
        assertThat(usuarioRol.getRol().getNombre()).isEqualTo("DOCENTE");
        assertThat(usuarioRol.getUsuario()).isEqualTo(usuario); // El usuario se mantiene
    }

    @Test
    @DisplayName("Debería validar integridad de la relación usuario-rol")
    void deberiaValidarIntegridadDeLaRelacionUsuarioRol() {
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        
        // Verificar que tanto usuario como rol están presentes
        assertThat(usuarioRol.getUsuario()).isNotNull();
        assertThat(usuarioRol.getRol()).isNotNull();
        
        // Verificar que tienen identificadores válidos
        assertThat(usuarioRol.getUsuario().getCedula()).isNotNull().isNotEmpty();
        assertThat(usuarioRol.getRol().getId()).isNotNull();
        
        // Verificar que tienen nombres válidos
        assertThat(usuarioRol.getUsuario().getNombre()).isNotEmpty();
        assertThat(usuarioRol.getRol().getNombre()).isNotEmpty();
        
        // Verificar que el usuario tiene información básica completa
        assertThat(usuarioRol.getUsuario().getApellido()).isNotEmpty();
        assertThat(usuarioRol.getUsuario().getEmail()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería manejar usuarios con diferentes roles administrativos")
    void deberiaManejarUsuariosConDiferentesRolesAdministrativos() {
        // Secretario
        Rol rolSecretario = new Rol();
        rolSecretario.setId(4);
        rolSecretario.setNombre("SECRETARIO");
        
        UsuarioRol usuarioSecretario = new UsuarioRol();
        usuarioSecretario.setUsuario(usuario);
        usuarioSecretario.setRol(rolSecretario);
        
        // Administrador del sistema
        Rol rolAdmin = new Rol();
        rolAdmin.setId(5);
        rolAdmin.setNombre("ADMIN");
        
        UsuarioRol usuarioAdmin = new UsuarioRol();
        usuarioAdmin.setUsuario(usuario);
        usuarioAdmin.setRol(rolAdmin);
        
        // Verificaciones
        assertThat(usuarioSecretario.getRol().getNombre()).isEqualTo("SECRETARIO");
        assertThat(usuarioAdmin.getRol().getNombre()).isEqualTo("ADMIN");
        
        // Ambos roles pueden ser asignados al mismo usuario
        assertThat(usuarioSecretario.getUsuario().getCedula()).isEqualTo(usuarioAdmin.getUsuario().getCedula());
        assertThat(usuarioSecretario.getRol().getId()).isNotEqualTo(usuarioAdmin.getRol().getId());
    }

    @Test
    @DisplayName("Debería manejar usuarios con cédulas válidas ecuatorianas")
    void deberiaManejarUsuariosConCedulasValidasEcuatorianas() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (int i = 0; i < cedulasValidas.length; i++) {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCedula(cedulasValidas[i]);
            nuevoUsuario.setNombre("Usuario" + (i + 1));
            nuevoUsuario.setApellido("Apellido" + (i + 1));
            nuevoUsuario.setEmail("usuario" + (i + 1) + "@test.com");
            
            UsuarioRol nuevaRelacion = new UsuarioRol();
            nuevaRelacion.setUsuario(nuevoUsuario);
            nuevaRelacion.setRol(rol);
            
            assertThat(nuevaRelacion.getUsuario().getCedula()).isEqualTo(cedulasValidas[i]);
            assertThat(nuevaRelacion.getRol()).isEqualTo(rol);
        }
    }
}*/