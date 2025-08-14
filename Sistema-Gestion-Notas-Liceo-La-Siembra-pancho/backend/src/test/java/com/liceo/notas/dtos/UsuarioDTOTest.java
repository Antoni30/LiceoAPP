package com.liceo.notas.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para UsuarioDTO")
class UsuarioDTOTest {

    private UsuarioDTO usuarioDTO;
    private Validator validator;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería validar UsuarioDTO válido correctamente")
    void deberiaValidarUsuarioDTOValido() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan Carlos");
        usuarioDTO.setApellidos("García López");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería fallar validación con cédula vacía")
    void deberiaFallarValidacionConCedulaVacia() {
        usuarioDTO.setIdUsuario("");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La cédula es obligatoria");
    }

    @Test
    @DisplayName("Debería fallar validación con cédula de longitud incorrecta")
    void deberiaFallarValidacionConCedulaLongitudIncorrecta() {
        usuarioDTO.setIdUsuario("123456789"); // 9 dígitos
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La cédula debe tener exactamente 10 dígitos");
    }

    @Test
    @DisplayName("Debería fallar validación con cédula que contiene letras")
    void deberiaFallarValidacionConCedulaConLetras() {
        usuarioDTO.setIdUsuario("123456789a");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La cédula solo debe contener números");
    }

    @Test
    @DisplayName("Debería fallar validación con nombres vacíos")
    void deberiaFallarValidacionConNombresVacios() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Los nombres solo deben contener letras");
    }

    @Test
    @DisplayName("Debería fallar validación con nombres que contienen números")
    void deberiaFallarValidacionConNombresConNumeros() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan123");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Los nombres solo deben contener letras");
    }

    @Test
    @DisplayName("Debería validar nombres con acentos")
    void deberiaValidarNombresConAcentos() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("José María");
        usuarioDTO.setApellidos("García López");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("jose@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería fallar validación con nickname muy corto")
    void deberiaFallarValidacionConNicknameMuyCorto() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("abc"); // 3 caracteres
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El nickname debe tener entre 4 y 10 caracteres");
    }

    @Test
    @DisplayName("Debería fallar validación con nickname muy largo")
    void deberiaFallarValidacionConNicknameMuyLargo() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("abcdefghijklmnop"); // 16 caracteres
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El nickname debe tener entre 4 y 10 caracteres");
    }

    @Test
    @DisplayName("Debería validar nickname con caracteres permitidos")
    void deberiaValidarNicknameConCaracteresPermitidos() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("user_123");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debería fallar validación con contraseña muy corta")
    void deberiaFallarValidacionConContrasenaMuyCorta() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Pass1!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("La contraseña debe contener: 1 número, 1 mayúscula, 1 minúscula, 1 carácter especial (@#$%^&+=!) y sin espacios");
    }

    @Test
    @DisplayName("Debería fallar validación con contraseña sin mayúscula")
    void deberiaFallarValidacionConContrasenaSinMayuscula() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("La contraseña debe contener: 1 número, 1 mayúscula, 1 minúscula, 1 carácter especial");
    }

    @Test
    @DisplayName("Debería fallar validación con estado inválido")
    void deberiaFallarValidacionConEstadoInvalido() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("OTRO_ESTADO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("El estado debe ser ACTIVO, INACTIVO o PENDIENTE");
    }

    @Test
    @DisplayName("Debería fallar validación con email inválido")
    void deberiaFallarValidacionConEmailInvalido() {
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan");
        usuarioDTO.setApellidos("García");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("email_invalido");
        
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Debe ser un email válido");
    }

    @Test
    @DisplayName("Debería establecer y obtener roles correctamente")
    void deberiaEstablecerYObtenerRoles() {
        List<Integer> roles = Arrays.asList(1, 2, 3);
        usuarioDTO.setRoles(roles);
        
        assertThat(usuarioDTO.getRoles()).isEqualTo(roles);
        assertThat(usuarioDTO.getRoles()).hasSize(3);
    }

    @Test
    @DisplayName("Debería manejar MFA habilitado correctamente")
    void deberiaManejarMfaHabilitado() {
        usuarioDTO.setMfaHabilitado(true);
        assertThat(usuarioDTO.isMfaHabilitado()).isTrue();
        
        usuarioDTO.setMfaHabilitado(false);
        assertThat(usuarioDTO.isMfaHabilitado()).isFalse();
    }

    @Test
    @DisplayName("Debería establecer y obtener MFA secret correctamente")
    void deberiaEstablecerYObtenerMfaSecret() {
        String mfaSecret = "JBSWY3DPEHPK3PXP";
        usuarioDTO.setMfaSecret(mfaSecret);
        
        assertThat(usuarioDTO.getMfaSecret()).isEqualTo(mfaSecret);
    }

    @Test
    @DisplayName("Debería manejar email verificado correctamente")
    void deberiaManejarEmailVerificado() {
        usuarioDTO.setEmailVerificado(true);
        assertThat(usuarioDTO.isEmailVerificado()).isTrue();
        
        usuarioDTO.setEmailVerificado(false);
        assertThat(usuarioDTO.isEmailVerificado()).isFalse();
    }
}