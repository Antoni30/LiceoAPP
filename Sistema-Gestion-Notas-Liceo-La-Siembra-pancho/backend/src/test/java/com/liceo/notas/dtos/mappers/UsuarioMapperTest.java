package com.liceo.notas.dtos.mappers;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests para UsuarioMapper")
class UsuarioMapperTest {

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario("0504110438");
        usuario.setNombres("Juan Carlos");
        usuario.setApellidos("García López");
        usuario.setNickname("jgarcia");
        usuario.setContrasena("hashedPassword");
        usuario.setEstado("ACTIVO");
        usuario.setEmail("juan@ejemplo.com");
        usuario.setMfaHabilitado(true);
        usuario.setMfaSecret("JBSWY3DPEHPK3PXP");
        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacion("token123");
        usuario.setMfaCodeExpiration(LocalDateTime.now().plusMinutes(5));

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan Carlos");
        usuarioDTO.setApellidos("García López");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        usuarioDTO.setMfaHabilitado(true);
        usuarioDTO.setMfaSecret("JBSWY3DPEHPK3PXP");
        usuarioDTO.setEmailVerificado(true);
        usuarioDTO.setRoles(Arrays.asList(1, 2));
    }

    @Test
    @DisplayName("Debería convertir entidad Usuario a DTO correctamente")
    void deberiaConvertirEntidadUsuarioADTOCorrectamente() {
        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

        assertThat(dto).isNotNull();
        assertThat(dto.getIdUsuario()).isEqualTo(usuario.getIdUsuario());
        assertThat(dto.getNombres()).isEqualTo(usuario.getNombres());
        assertThat(dto.getApellidos()).isEqualTo(usuario.getApellidos());
        assertThat(dto.getNickname()).isEqualTo(usuario.getNickname());
        assertThat(dto.getContrasena()).isEqualTo(usuario.getContrasena());
        assertThat(dto.getEstado()).isEqualTo(usuario.getEstado());
        assertThat(dto.getEmail()).isEqualTo(usuario.getEmail());
        assertThat(dto.isMfaHabilitado()).isEqualTo(usuario.getMfaHabilitado());
        assertThat(dto.isEmailVerificado()).isEqualTo(usuario.isEmailVerificado());
    }

    @Test
    @DisplayName("Debería convertir DTO Usuario a entidad correctamente")
    void deberiaConvertirDTOUsuarioAEntidadCorrectamente() {
        Usuario entidad = UsuarioMapper.toEntity(usuarioDTO);

        assertThat(entidad).isNotNull();
        assertThat(entidad.getIdUsuario()).isEqualTo(usuarioDTO.getIdUsuario());
        assertThat(entidad.getNombres()).isEqualTo(usuarioDTO.getNombres());
        assertThat(entidad.getApellidos()).isEqualTo(usuarioDTO.getApellidos());
        assertThat(entidad.getNickname()).isEqualTo(usuarioDTO.getNickname());
        assertThat(entidad.getContrasena()).isEqualTo(usuarioDTO.getContrasena());
        assertThat(entidad.getEstado()).isEqualTo(usuarioDTO.getEstado());
        assertThat(entidad.getEmail()).isEqualTo(usuarioDTO.getEmail());
        assertThat(entidad.getMfaHabilitado()).isEqualTo(usuarioDTO.isMfaHabilitado());
        assertThat(entidad.getMfaSecret()).isEqualTo(usuarioDTO.getMfaSecret());
        assertThat(entidad.isEmailVerificado()).isEqualTo(usuarioDTO.isEmailVerificado());
    }

    @Test
    @DisplayName("Debería lanzar excepción al convertir entidad nula a DTO")
    void deberiaLanzarExcepcionAlConvertirEntidadNulaADTO() {
        assertThatThrownBy(() -> UsuarioMapper.toDTO(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("La entidad Usuario no puede ser nula");
    }

    @Test
    @DisplayName("Debería lanzar excepción al convertir DTO nulo a entidad")
    void deberiaLanzarExcepcionAlConvertirDTONuloAEntidad() {
        assertThatThrownBy(() -> UsuarioMapper.toEntity(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El DTO UsuarioDTO no puede ser nulo");
    }

    @Test
    @DisplayName("Debería manejar campos opcionales nulos en entidad a DTO")
    void deberiaManejarCamposOpcionalesNulosEnEntidadADTO() {
        usuario.setMfaSecret(null);
        usuario.setTokenVerificacion(null);
        usuario.setMfaCodeExpiration(null);

        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

        assertThat(dto).isNotNull();
        assertThat(dto.getIdUsuario()).isEqualTo(usuario.getIdUsuario());
        assertThat(dto.getNombres()).isEqualTo(usuario.getNombres());
        assertThat(dto.getApellidos()).isEqualTo(usuario.getApellidos());
    }

    @Test
    @DisplayName("Debería manejar campos opcionales nulos en DTO a entidad")
    void deberiaManejarCamposOpcionalesNulosEnDTOAEntidad() {
        usuarioDTO.setMfaSecret(null);
        usuarioDTO.setRoles(null);

        Usuario entidad = UsuarioMapper.toEntity(usuarioDTO);

        assertThat(entidad).isNotNull();
        assertThat(entidad.getIdUsuario()).isEqualTo(usuarioDTO.getIdUsuario());
        assertThat(entidad.getNombres()).isEqualTo(usuarioDTO.getNombres());
        assertThat(entidad.getApellidos()).isEqualTo(usuarioDTO.getApellidos());
        assertThat(entidad.getMfaSecret()).isNull();
    }

    @Test
    @DisplayName("Debería preservar booleanos correctamente en entidad a DTO")
    void deberiaPreservarBooleanosCorrectamenteEnEntidadADTO() {
        usuario.setMfaHabilitado(false);
        usuario.setEmailVerificado(false);

        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

        assertThat(dto.isMfaHabilitado()).isFalse();
        assertThat(dto.isEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería preservar booleanos correctamente en DTO a entidad")
    void deberiaPreservarBooleanosCorrectamenteEnDTOAEntidad() {
        usuarioDTO.setMfaHabilitado(false);
        usuarioDTO.setEmailVerificado(false);

        Usuario entidad = UsuarioMapper.toEntity(usuarioDTO);

        assertThat(entidad.getMfaHabilitado()).isFalse();
        assertThat(entidad.isEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería realizar conversión bidireccional correctamente")
    void deberiaRealizarConversionBidireccionalCorrectamente() {
        // Entidad -> DTO -> Entidad
        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);
        Usuario entidadConvertida = UsuarioMapper.toEntity(dto);

        assertThat(entidadConvertida.getIdUsuario()).isEqualTo(usuario.getIdUsuario());
        assertThat(entidadConvertida.getNombres()).isEqualTo(usuario.getNombres());
        assertThat(entidadConvertida.getApellidos()).isEqualTo(usuario.getApellidos());
        assertThat(entidadConvertida.getNickname()).isEqualTo(usuario.getNickname());
        assertThat(entidadConvertida.getContrasena()).isEqualTo(usuario.getContrasena());
        assertThat(entidadConvertida.getEstado()).isEqualTo(usuario.getEstado());
        assertThat(entidadConvertida.getEmail()).isEqualTo(usuario.getEmail());
        assertThat(entidadConvertida.getMfaHabilitado()).isEqualTo(usuario.getMfaHabilitado());
        assertThat(entidadConvertida.isEmailVerificado()).isEqualTo(usuario.isEmailVerificado());
    }

    @Test
    @DisplayName("Debería manejar usuario con MFA deshabilitado")
    void deberiaManejarUsuarioConMfaDeshabilitado() {
        usuario.setMfaHabilitado(false);
        usuario.setMfaSecret(null);

        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

        assertThat(dto.isMfaHabilitado()).isFalse();
        assertThat(dto.getMfaSecret()).isNull();

        Usuario entidad = UsuarioMapper.toEntity(dto);
        assertThat(entidad.getMfaHabilitado()).isFalse();
        assertThat(entidad.getMfaSecret()).isNull();
    }

    @Test
    @DisplayName("Debería manejar usuario con email no verificado")
    void deberiaManejarUsuarioConEmailNoVerificado() {
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion("pending_token");

        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

        assertThat(dto.isEmailVerificado()).isFalse();

        Usuario entidad = UsuarioMapper.toEntity(dto);
        assertThat(entidad.isEmailVerificado()).isFalse();
    }

    @Test
    @DisplayName("Debería crear nuevas instancias en cada conversión")
    void deberiaCrearNuevasInstanciasEnCadaConversion() {
        UsuarioDTO dto1 = UsuarioMapper.toDTO(usuario);
        UsuarioDTO dto2 = UsuarioMapper.toDTO(usuario);

        assertThat(dto1).isNotSameAs(dto2);
        assertThat(dto1.getIdUsuario()).isEqualTo(dto2.getIdUsuario());

        Usuario entidad1 = UsuarioMapper.toEntity(usuarioDTO);
        Usuario entidad2 = UsuarioMapper.toEntity(usuarioDTO);

        assertThat(entidad1).isNotSameAs(entidad2);
        assertThat(entidad1.getIdUsuario()).isEqualTo(entidad2.getIdUsuario());
    }
}