package com.liceo.notas.repositories;

import com.liceo.notas.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests para UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setIdUsuario("0504110438");
        usuario1.setNombres("Juan Carlos");
        usuario1.setApellidos("García López");
        usuario1.setNickname("jgarcia");
        usuario1.setContrasena("hashedPassword1");
        usuario1.setEstado("ACTIVO");
        usuario1.setEmail("juan@ejemplo.com");
        usuario1.setMfaHabilitado(false);
        usuario1.setEmailVerificado(true);
        usuario1.setTokenVerificacion("token123");

        usuario2 = new Usuario();
        usuario2.setIdUsuario("1751983014");
        usuario2.setNombres("María Elena");
        usuario2.setApellidos("Rodríguez Pérez");
        usuario2.setNickname("mrodriguez");
        usuario2.setContrasena("hashedPassword2");
        usuario2.setEstado("INACTIVO");
        usuario2.setEmail("maria@ejemplo.com");
        usuario2.setMfaHabilitado(true);
        usuario2.setEmailVerificado(false);
        usuario2.setTokenVerificacion("token456");

        usuario3 = new Usuario();
        usuario3.setIdUsuario("1722070560");
        usuario3.setNombres("Pedro José");
        usuario3.setApellidos("Martínez Silva");
        usuario3.setNickname("pmartinez");
        usuario3.setContrasena("hashedPassword3");
        usuario3.setEstado("ACTIVO");
        usuario3.setEmail("pedro@ejemplo.com");
        usuario3.setMfaHabilitado(false);
        usuario3.setEmailVerificado(true);
        usuario3.setTokenVerificacion(null);

        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);
        entityManager.persistAndFlush(usuario3);
    }

    @Test
    @DisplayName("Debería encontrar usuario por nickname")
    void deberiaEncontrarUsuarioPorNickname() {
        Optional<Usuario> resultado = usuarioRepository.findByNickname("jgarcia");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombres()).isEqualTo("Juan Carlos");
        assertThat(resultado.get().getApellidos()).isEqualTo("García López");
        assertThat(resultado.get().getIdUsuario()).isEqualTo("0504110438");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando nickname no existe")
    void deberiaRetornarVacioCuandoNicknameNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByNickname("usuario_inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por ID")
    void deberiaEncontrarUsuarioPorId() {
        Optional<Usuario> resultado = usuarioRepository.findByIdUsuario("0504110438");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNickname()).isEqualTo("jgarcia");
        assertThat(resultado.get().getNombres()).isEqualTo("Juan Carlos");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando ID de usuario no existe")
    void deberiaRetornarVacioCuandoIdUsuarioNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByIdUsuario("9999999999");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuarios por estado ACTIVO")
    void deberiaEncontrarUsuariosPorEstadoActivo() {
        List<Usuario> usuariosActivos = usuarioRepository.findByEstado("ACTIVO");

        assertThat(usuariosActivos).hasSize(2);
        assertThat(usuariosActivos)
            .extracting(Usuario::getNickname)
            .containsExactlyInAnyOrder("jgarcia", "pmartinez");
    }

    @Test
    @DisplayName("Debería encontrar usuarios por estado INACTIVO")
    void deberiaEncontrarUsuariosPorEstadoInactivo() {
        List<Usuario> usuariosInactivos = usuarioRepository.findByEstado("INACTIVO");

        assertThat(usuariosInactivos).hasSize(1);
        assertThat(usuariosInactivos.get(0).getNickname()).isEqualTo("mrodriguez");
    }

    @Test
    @DisplayName("Debería retornar lista vacía para estado inexistente")
    void deberiaRetornarListaVaciaParaEstadoInexistente() {
        List<Usuario> usuarios = usuarioRepository.findByEstado("ESTADO_INEXISTENTE");

        assertThat(usuarios).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por email")
    void deberiaEncontrarUsuarioPorEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("juan@ejemplo.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNickname()).isEqualTo("jgarcia");
        assertThat(resultado.get().getNombres()).isEqualTo("Juan Carlos");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando email no existe")
    void deberiaRetornarVacioCuandoEmailNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("inexistente@ejemplo.com");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por nickname y email")
    void deberiaEncontrarUsuarioPorNicknameYEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByNicknameAndEmail("jgarcia", "juan@ejemplo.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo("0504110438");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando nickname y email no coinciden")
    void deberiaRetornarVacioCuandoNicknameYEmailNoCoinciden() {
        Optional<Usuario> resultado = usuarioRepository.findByNicknameAndEmail("jgarcia", "email_incorrecto@ejemplo.com");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por token de verificación")
    void deberiaEncontrarUsuarioPorTokenVerificacion() {
        Optional<Usuario> resultado = usuarioRepository.findByTokenVerificacion("token123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNickname()).isEqualTo("jgarcia");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando token de verificación no existe")
    void deberiaRetornarVacioCuandoTokenVerificacionNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByTokenVerificacion("token_inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por ID y email")
    void deberiaEncontrarUsuarioPorIdYEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByIdUsuarioAndEmail("0504110438", "juan@ejemplo.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNickname()).isEqualTo("jgarcia");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando ID y email no coinciden")
    void deberiaRetornarVacioCuandoIdYEmailNoCoinciden() {
        Optional<Usuario> resultado = usuarioRepository.findByIdUsuarioAndEmail("0504110438", "email_incorrecto@ejemplo.com");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería encontrar usuario por nickname y contraseña")
    void deberiaEncontrarUsuarioPorNicknameYContrasena() {
        Optional<Usuario> resultado = usuarioRepository.findByNicknameAndContrasena("jgarcia", "hashedPassword1");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo("0504110438");
    }

    @Test
    @DisplayName("Debería retornar vacío cuando contraseña es incorrecta")
    void deberiaRetornarVacioCuandoContrasenaEsIncorrecta() {
        Optional<Usuario> resultado = usuarioRepository.findByNicknameAndContrasena("jgarcia", "contrasena_incorrecta");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería guardar nuevo usuario")
    void deberiaGuardarNuevoUsuario() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdUsuario("1756916233");
        nuevoUsuario.setNombres("Ana Sofía");
        nuevoUsuario.setApellidos("Herrera Vega");
        nuevoUsuario.setNickname("aherrera");
        nuevoUsuario.setContrasena("hashedPassword4");
        nuevoUsuario.setEstado("ACTIVO");
        nuevoUsuario.setEmail("ana@ejemplo.com");
        nuevoUsuario.setMfaHabilitado(false);
        nuevoUsuario.setEmailVerificado(false);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getIdUsuario()).isEqualTo("1756916233");
        assertThat(usuarioGuardado.getNickname()).isEqualTo("aherrera");

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByNickname("aherrera");
        assertThat(usuarioEncontrado).isPresent();
    }

    @Test
    @DisplayName("Debería actualizar usuario existente")
    void deberiaActualizarUsuarioExistente() {
        Usuario usuario = usuarioRepository.findByNickname("jgarcia").orElseThrow();
        usuario.setNombres("Juan Carlos Actualizado");
        usuario.setEstado("INACTIVO");

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        assertThat(usuarioActualizado.getNombres()).isEqualTo("Juan Carlos Actualizado");
        assertThat(usuarioActualizado.getEstado()).isEqualTo("INACTIVO");
    }

    @Test
    @DisplayName("Debería eliminar usuario por ID")
    void deberiaEliminarUsuarioPorId() {
        usuarioRepository.deleteById("0504110438");

        Optional<Usuario> usuarioEliminado = usuarioRepository.findByIdUsuario("0504110438");
        assertThat(usuarioEliminado).isEmpty();
    }

    @Test
    @DisplayName("Debería contar usuarios correctamente")
    void deberiaContarUsuariosCorrectamente() {
        long cantidad = usuarioRepository.count();

        assertThat(cantidad).isEqualTo(3);
    }

    @Test
    @DisplayName("Debería verificar existencia de usuario por ID")
    void deberiaVerificarExistenciaDeUsuarioPorId() {
        boolean existe = usuarioRepository.existsById("0504110438");
        boolean noExiste = usuarioRepository.existsById("9999999999");

        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }
}