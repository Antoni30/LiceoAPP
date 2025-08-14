package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para entidad Usuario")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Debería crear un usuario con valores por defecto")
    void deberiaCrearUsuarioConValoresPorDefecto() {
        assertThat(usuario.getMfaHabilitado()).isFalse();
        assertThat(usuario.isEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID de usuario correctamente")
    void deberiaEstablecerYObtenerIdUsuario() {
        String idUsuario = "0504110438";
        usuario.setIdUsuario(idUsuario);
        
        assertThat(usuario.getIdUsuario()).isEqualTo(idUsuario);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombres correctamente")
    void deberiaEstablecerYObtenerNombres() {
        String nombres = "Juan Carlos";
        usuario.setNombres(nombres);
        
        assertThat(usuario.getNombres()).isEqualTo(nombres);
    }

    @Test
    @DisplayName("Debería establecer y obtener apellidos correctamente")
    void deberiaEstablecerYObtenerApellidos() {
        String apellidos = "García López";
        usuario.setApellidos(apellidos);
        
        assertThat(usuario.getApellidos()).isEqualTo(apellidos);
    }

    @Test
    @DisplayName("Debería establecer y obtener nickname correctamente")
    void deberiaEstablecerYObtenerNickname() {
        String nickname = "jgarcia";
        usuario.setNickname(nickname);
        
        assertThat(usuario.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("Debería establecer y obtener contraseña correctamente")
    void deberiaEstablecerYObtenerContrasena() {
        String contrasena = "password123";
        usuario.setContrasena(contrasena);
        
        assertThat(usuario.getContrasena()).isEqualTo(contrasena);
    }

    @Test
    @DisplayName("Debería establecer y obtener estado correctamente")
    void deberiaEstablecerYObtenerEstado() {
        String estado = "Activo";
        usuario.setEstado(estado);
        
        assertThat(usuario.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Debería establecer y obtener email correctamente")
    void deberiaEstablecerYObtenerEmail() {
        String email = "juan@ejemplo.com";
        usuario.setEmail(email);
        
        assertThat(usuario.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Debería manejar MFA habilitado correctamente")
    void deberiaManejarMfaHabilitado() {
        usuario.setMfaHabilitado(true);
        assertThat(usuario.getMfaHabilitado()).isTrue();
        
        usuario.setMfaHabilitado(false);
        assertThat(usuario.getMfaHabilitado()).isFalse();
    }

    @Test
    @DisplayName("Debería establecer y obtener MFA secret correctamente")
    void deberiaEstablecerYObtenerMfaSecret() {
        String mfaSecret = "JBSWY3DPEHPK3PXP";
        usuario.setMfaSecret(mfaSecret);
        
        assertThat(usuario.getMfaSecret()).isEqualTo(mfaSecret);
    }

    @Test
    @DisplayName("Debería establecer y obtener fecha de expiración MFA correctamente")
    void deberiaEstablecerYObtenerMfaCodeExpiration() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        usuario.setMfaCodeExpiration(expiration);
        
        assertThat(usuario.getMfaCodeExpiration()).isEqualTo(expiration);
    }

    @Test
    @DisplayName("Debería manejar email verificado correctamente")
    void deberiaManejarEmailVerificado() {
        usuario.setEmailVerificado(true);
        assertThat(usuario.isEmailVerificado()).isTrue();
        
        usuario.setEmailVerificado(false);
        assertThat(usuario.isEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería establecer y obtener token de verificación correctamente")
    void deberiaEstablecerYObtenerTokenVerificacion() {
        String token = "abc123-def456-ghi789";
        usuario.setTokenVerificacion(token);
        
        assertThat(usuario.getTokenVerificacion()).isEqualTo(token);
    }

    @Test
    @DisplayName("Debería manejar lista de roles correctamente")
    void deberiaManejarListaDeRoles() {
        List<Rol> roles = new ArrayList<>();
        Rol rol1 = new Rol();
        rol1.setId(1);
        rol1.setNombre("Estudiante");
        roles.add(rol1);
        
        usuario.setRoles(roles);
        
        assertThat(usuario.getRoles()).hasSize(1);
        assertThat(usuario.getRoles().get(0).getNombre()).isEqualTo("Estudiante");
    }

    @Test
    @DisplayName("Debería manejar lista de cursos correctamente")
    void deberiaManejarListaDeCursos() {
        List<UsuarioCurso> cursos = new ArrayList<>();
        usuario.setCursos(cursos);
        
        assertThat(usuario.getCursos()).isEmpty();
    }

    @Test
    @DisplayName("Debería crear usuario completo correctamente")
    void deberiaCrearUsuarioCompleto() {
        String idUsuario = "0504110438";
        String nombres = "Juan Carlos";
        String apellidos = "García López";
        String nickname = "jgarcia";
        String contrasena = "password123";
        String estado = "Activo";
        String email = "juan@ejemplo.com";
        String token = "token123";
        
        usuario.setIdUsuario(idUsuario);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setNickname(nickname);
        usuario.setContrasena(contrasena);
        usuario.setEstado(estado);
        usuario.setEmail(email);
        usuario.setTokenVerificacion(token);
        usuario.setMfaHabilitado(true);
        usuario.setEmailVerificado(true);
        
        assertThat(usuario.getIdUsuario()).isEqualTo(idUsuario);
        assertThat(usuario.getNombres()).isEqualTo(nombres);
        assertThat(usuario.getApellidos()).isEqualTo(apellidos);
        assertThat(usuario.getNickname()).isEqualTo(nickname);
        assertThat(usuario.getContrasena()).isEqualTo(contrasena);
        assertThat(usuario.getEstado()).isEqualTo(estado);
        assertThat(usuario.getEmail()).isEqualTo(email);
        assertThat(usuario.getTokenVerificacion()).isEqualTo(token);
        assertThat(usuario.getMfaHabilitado()).isTrue();
        assertThat(usuario.isEmailVerificado()).isTrue();
    }
}