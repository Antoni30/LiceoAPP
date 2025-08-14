package com.liceo.notas.services;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.exceptions.EmailInvalidoException;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.services.ServiceImpl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para UsuarioServiceImpl")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configurar baseUrl para el servicio
        ReflectionTestUtils.setField(usuarioService, "baseUrl", "http://localhost:8080");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan Carlos");
        usuarioDTO.setApellidos("García López");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        usuarioDTO.setMfaHabilitado(false);
        usuarioDTO.setEmailVerificado(false);

        usuario = new Usuario();
        usuario.setIdUsuario("0504110438");
        usuario.setNombres("Juan Carlos");
        usuario.setApellidos("García López");
        usuario.setNickname("jgarcia");
        usuario.setContrasena("hashedPassword");
        usuario.setEstado("ACTIVO");
        usuario.setEmail("juan@ejemplo.com");
        usuario.setMfaHabilitado(false);
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion("token123");
    }

    @Test
    @DisplayName("Debería crear usuario exitosamente")
    void deberiaCrearUsuarioExitosamente() {
        // Given
        when(usuarioRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByIdUsuario(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        doNothing().when(emailService).sendHtmlEmail(anyString(), anyString(), anyString());

        // When
        UsuarioDTO resultado = usuarioService.crearUsuario(usuarioDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.getNombres()).isEqualTo("Juan Carlos");
        assertThat(resultado.getApellidos()).isEqualTo("García López");
        
        verify(usuarioRepository).findByNickname("jgarcia");
        verify(usuarioRepository).findByIdUsuario("0504110438");
        verify(passwordEncoder).encode("Password123!");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(emailService).sendHtmlEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando nickname ya existe")
    void deberiaLanzarExcepcionCuandoNicknameYaExiste() {
        // Given
        when(usuarioRepository.findByNickname("jgarcia")).thenReturn(Optional.of(usuario));

        // When & Then
        assertThatThrownBy(() -> usuarioService.crearUsuario(usuarioDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("El nickname ya está en uso");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando cédula ya existe")
    void deberiaLanzarExcepcionCuandoCedulaYaExiste() {
        // Given
        when(usuarioRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByIdUsuario("0504110438")).thenReturn(Optional.of(usuario));

        // When & Then
        assertThatThrownBy(() -> usuarioService.crearUsuario(usuarioDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("La cédula ya está en uso");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando cédula es inválida")
    void deberiaLanzarExcepcionCuandoCedulaEsInvalida() {
        // Given
        usuarioDTO.setIdUsuario("1111111111"); // Cédula inválida para Ecuador
        when(usuarioRepository.findByNickname(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.crearUsuario(usuarioDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("La cédula ingresada no es válida para Ecuador");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando email es inválido")
    void deberiaLanzarExcepcionCuandoEmailEsInvalido() {
        // Given
        usuarioDTO.setEmail("email_invalido");
        when(usuarioRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByIdUsuario(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.crearUsuario(usuarioDTO))
            .isInstanceOf(EmailInvalidoException.class)
            .hasMessage("El formato del email es inválido. Debe ser ejemplo@dominio.com");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería verificar email exitosamente")
    void deberiaVerificarEmailExitosamente() {
        // Given
        when(usuarioRepository.findByTokenVerificacion("token123")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        boolean resultado = usuarioService.verificarEmail("token123");

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería fallar verificación cuando token no existe")
    void deberiaFallarVerificacionCuandoTokenNoExiste() {
        // Given
        when(usuarioRepository.findByTokenVerificacion("token_inexistente")).thenReturn(Optional.empty());

        // When
        boolean resultado = usuarioService.verificarEmail("token_inexistente");

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería obtener usuario por ID exitosamente")
    void deberiaObtenerUsuarioPorIdExitosamente() {
        // Given
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));

        // When
        Optional<UsuarioDTO> resultado = usuarioService.obtenerPorId("0504110438");

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.get().getNombres()).isEqualTo("Juan Carlos");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando usuario no existe")
    void deberiaRetornarVacioCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById("9999999999")).thenReturn(Optional.empty());

        // When
        Optional<UsuarioDTO> resultado = usuarioService.obtenerPorId("9999999999");

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería listar todos los usuarios")
    void deberiaListarTodosLosUsuarios() {
        // Given
        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario("1751983014");
        usuario2.setNombres("María");
        usuario2.setApellidos("González");
        usuario2.setNickname("mgonzalez");
        usuario2.setContrasena("hashedPassword2");
        usuario2.setEstado("ACTIVO");
        usuario2.setEmail("maria@ejemplo.com");

        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado)
            .extracting(UsuarioDTO::getNickname)
            .containsExactlyInAnyOrder("jgarcia", "mgonzalez");
    }

    @Test
    @DisplayName("Debería actualizar usuario exitosamente")
    void deberiaActualizarUsuarioExitosamente() {
        // Given
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        doNothing().when(emailService).sendHtmlEmail(anyString(), anyString(), anyString());

        UsuarioDTO actualizacionDTO = new UsuarioDTO();
        actualizacionDTO.setNombres("Juan Carlos Actualizado");
        actualizacionDTO.setApellidos("García López");
        actualizacionDTO.setNickname("jgarcia_nuevo");
        actualizacionDTO.setContrasena("NewPassword123!");
        actualizacionDTO.setEstado("ACTIVO");
        actualizacionDTO.setEmail("nuevo@ejemplo.com");
        actualizacionDTO.setMfaHabilitado(true);

        // When
        Optional<UsuarioDTO> resultado = usuarioService.actualizarUsuario("0504110438", actualizacionDTO);

        // Then
        assertThat(resultado).isPresent();
        verify(usuarioRepository).save(any(Usuario.class));
        verify(passwordEncoder).encode("NewPassword123!");
        verify(emailService).sendHtmlEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debería eliminar usuario exitosamente")
    void deberiaEliminarUsuarioExitosamente() {
        // Given
        when(usuarioRepository.existsById("0504110438")).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById("0504110438");

        // When
        boolean resultado = usuarioService.eliminarUsuario("0504110438");

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioRepository).deleteById("0504110438");
    }

    @Test
    @DisplayName("Debería fallar eliminación cuando usuario no existe")
    void deberiaFallarEliminacionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.existsById("9999999999")).thenReturn(false);

        // When
        boolean resultado = usuarioService.eliminarUsuario("9999999999");

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Debería listar usuarios por estado")
    void deberiaListarUsuariosPorEstado() {
        // Given
        List<Usuario> usuariosActivos = Arrays.asList(usuario);
        when(usuarioRepository.findByEstado("ACTIVO")).thenReturn(usuariosActivos);

        // When
        List<UsuarioDTO> resultado = usuarioService.listarPorEstado("ACTIVO");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    @DisplayName("Debería enviar email de verificación")
    void deberiaEnviarEmailDeVerificacion() {
        // Given
        doNothing().when(emailService).sendHtmlEmail(anyString(), anyString(), anyString());

        // When
        usuarioService.enviarEmailVerificacion(usuario);

        // Then
        verify(emailService).sendHtmlEmail(
            eq("juan@ejemplo.com"),
            contains("Verifica tu email"),
            contains("Verificar Email")
        );
    }
}