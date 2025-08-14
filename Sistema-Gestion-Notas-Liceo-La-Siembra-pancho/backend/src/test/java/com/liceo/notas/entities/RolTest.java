package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad Rol")
class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        rol.setId(id);
        
        assertThat(rol.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre de rol correctamente")
    void deberiaEstablecerYObtenerNombreRol() {
        String nombre = "Administrador";
        rol.setNombre(nombre);
        
        assertThat(rol.getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Debería manejar lista de usuarios con rol correctamente")
    void deberiaManejarListaDeUsuariosConRol() {
        List<UsuarioRol> usuarioRoles = new ArrayList<>();
        rol.setUsuarioRoles(usuarioRoles);
        
        assertThat(rol.getUsuarioRoles()).isEmpty();
    }

    @Test
    @DisplayName("Debería agregar usuarios con rol correctamente")
    void deberiaAgregarUsuariosConRolCorrectamente() {
        List<UsuarioRol> usuarioRoles = new ArrayList<>();
        
        UsuarioRol usuarioRol1 = new UsuarioRol();
        UsuarioRol usuarioRol2 = new UsuarioRol();
        
        usuarioRoles.add(usuarioRol1);
        usuarioRoles.add(usuarioRol2);
        
        rol.setUsuarioRoles(usuarioRoles);
        
        assertThat(rol.getUsuarioRoles()).hasSize(2);
        assertThat(rol.getUsuarioRoles()).containsExactly(usuarioRol1, usuarioRol2);
    }

    @Test
    @DisplayName("Debería crear rol completo correctamente")
    void deberiaCrearRolCompletoCorrectamente() {
        Integer id = 1;
        String nombre = "Docente";
        List<UsuarioRol> usuarioRoles = new ArrayList<>();
        
        rol.setId(id);
        rol.setNombre(nombre);
        rol.setUsuarioRoles(usuarioRoles);
        
        assertThat(rol.getId()).isEqualTo(id);
        assertThat(rol.getNombre()).isEqualTo(nombre);
        assertThat(rol.getUsuarioRoles()).isEqualTo(usuarioRoles);
    }

    @Test
    @DisplayName("Debería manejar roles del sistema educativo")
    void deberiaManejarRolesDelSistemaEducativo() {
        String[] rolesComunes = {
            "Administrador", "Docente", "Estudiante", "Secretario", "Director"
        };
        
        for (String nombreRol : rolesComunes) {
            rol.setNombre(nombreRol);
            assertThat(rol.getNombre()).isEqualTo(nombreRol);
            assertThat(rol.getNombre().length()).isLessThanOrEqualTo(20);
        }
    }

    @Test
    @DisplayName("Debería manejar nombres de rol con longitud máxima")
    void deberiaManejarNombresDeRolConLongitudMaxima() {
        String nombreLargo = "AdministradorGeneral"; // 20 caracteres exactos
        rol.setNombre(nombreLargo);
        
        assertThat(rol.getNombre()).isEqualTo(nombreLargo);
        assertThat(rol.getNombre().length()).isEqualTo(20);
    }

    @Test
    @DisplayName("Debería permitir actualizar lista de usuarios con rol")
    void deberiaPermitirActualizarListaDeUsuariosConRol() {
        // Lista inicial vacía
        List<UsuarioRol> usuariosIniciales = new ArrayList<>();
        rol.setUsuarioRoles(usuariosIniciales);
        assertThat(rol.getUsuarioRoles()).isEmpty();
        
        // Agregar usuarios
        List<UsuarioRol> usuariosActualizados = new ArrayList<>();
        UsuarioRol usuarioRol1 = new UsuarioRol();
        UsuarioRol usuarioRol2 = new UsuarioRol();
        UsuarioRol usuarioRol3 = new UsuarioRol();
        
        usuariosActualizados.add(usuarioRol1);
        usuariosActualizados.add(usuarioRol2);
        usuariosActualizados.add(usuarioRol3);
        
        rol.setUsuarioRoles(usuariosActualizados);
        assertThat(rol.getUsuarioRoles()).hasSize(3);
    }

    @Test
    @DisplayName("Debería crear rol de Estudiante")
    void deberiaCrearRolDeEstudiante() {
        rol.setId(1);
        rol.setNombre("Estudiante");
        
        assertThat(rol.getId()).isEqualTo(1);
        assertThat(rol.getNombre()).isEqualTo("Estudiante");
    }

    @Test
    @DisplayName("Debería crear rol de Docente")
    void deberiaCrearRolDeDocente() {
        rol.setId(2);
        rol.setNombre("Docente");
        
        assertThat(rol.getId()).isEqualTo(2);
        assertThat(rol.getNombre()).isEqualTo("Docente");
    }

    @Test
    @DisplayName("Debería crear rol de Administrador")
    void deberiaCrearRolDeAdministrador() {
        rol.setId(3);
        rol.setNombre("Administrador");
        
        assertThat(rol.getId()).isEqualTo(3);
        assertThat(rol.getNombre()).isEqualTo("Administrador");
    }
}