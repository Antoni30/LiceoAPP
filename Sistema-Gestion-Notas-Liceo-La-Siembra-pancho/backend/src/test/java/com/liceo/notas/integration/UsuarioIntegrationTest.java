package com.liceo.notas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:integrationtest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DisplayName("Tests de integración para Usuario")
class UsuarioIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        // Limpiar la base de datos antes de cada test
        usuarioRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debería crear, obtener, actualizar y eliminar usuario completamente")
    void deberiaCRUDCompletoDeUsuario() throws Exception {
        // Crear usuario
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setIdUsuario("0504110438"); // Cédula ecuatoriana válida
        nuevoUsuario.setNombres("Juan Carlos");
        nuevoUsuario.setApellidos("García López");
        nuevoUsuario.setNickname("jgarcia");
        nuevoUsuario.setContrasena("Password123!");
        nuevoUsuario.setEstado("ACTIVO");
        nuevoUsuario.setEmail("juan@ejemplo.com");
        nuevoUsuario.setMfaHabilitado(false);

        // 1. Crear usuario via POST
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value("0504110438"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.nickname").value("jgarcia"));

        // Verificar que se guardó en la base de datos
        assertThat(usuarioRepository.count()).isEqualTo(1);
        Usuario usuarioGuardado = usuarioRepository.findByNickname("jgarcia").orElse(null);
        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getNombres()).isEqualTo("Juan Carlos");

        // 2. Obtener usuario via GET por ID
        mockMvc.perform(get("/api/usuarios/0504110438"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("0504110438"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.nickname").value("jgarcia"));

        // 3. Listar todos los usuarios
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idUsuario").value("1712345678"));

        // 4. Actualizar usuario via PUT
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setNombres("Juan Carlos Actualizado");
        usuarioActualizado.setApellidos("García López");
        usuarioActualizado.setNickname("jgarcia");
        usuarioActualizado.setContrasena("NewPassword123!");
        usuarioActualizado.setEstado("ACTIVO");
        usuarioActualizado.setEmail("juan_actualizado@ejemplo.com");
        usuarioActualizado.setMfaHabilitado(true);

        mockMvc.perform(put("/api/usuarios/0504110438")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Juan Carlos Actualizado"))
                .andExpect(jsonPath("$.mfaHabilitado").value(true));

        // Verificar actualización en base de datos
        Usuario usuarioActualizadoBD = usuarioRepository.findByNickname("jgarcia").orElse(null);
        assertThat(usuarioActualizadoBD).isNotNull();
        assertThat(usuarioActualizadoBD.getNombres()).isEqualTo("Juan Carlos Actualizado");
        assertThat(usuarioActualizadoBD.getMfaHabilitado()).isTrue();

        // 5. Eliminar usuario via DELETE
        mockMvc.perform(delete("/api/usuarios/0504110438"))
                .andExpect(status().isNoContent());

        // Verificar eliminación en base de datos
        assertThat(usuarioRepository.count()).isEqualTo(0);
        assertThat(usuarioRepository.findByNickname("jgarcia")).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debería manejar múltiples usuarios y filtrado por estado")
    void deberiaManejarMultiplesUsuariosYFiltradoPorEstado() throws Exception {
        // Crear múltiples usuarios
        UsuarioDTO usuario1 = crearUsuarioDTO("0504110438", "Juan", "García", "jgarcia", "ACTIVO");
        UsuarioDTO usuario2 = crearUsuarioDTO("1751983014", "María", "Rodríguez", "mrodriguez", "ACTIVO");
        UsuarioDTO usuario3 = crearUsuarioDTO("1722070560", "Pedro", "Martínez", "pmartinez", "INACTIVO");

        // Crear usuarios
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario3)))
                .andExpect(status().isCreated());

        // Verificar que se crearon 3 usuarios
        assertThat(usuarioRepository.count()).isEqualTo(3);

        // Listar todos los usuarios
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        // Filtrar usuarios activos
        mockMvc.perform(get("/api/usuarios/por-estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"))
                .andExpect(jsonPath("$[1].estado").value("ACTIVO"));

        // Filtrar usuarios inactivos
        mockMvc.perform(get("/api/usuarios/por-estado/INACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado").value("INACTIVO"))
                .andExpect(jsonPath("$[0].nickname").value("pmartinez"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debería manejar errores de validación correctamente")
    void deberiaManejarErroresDeValidacionCorrectamente() throws Exception {
        // Usuario con datos inválidos
        UsuarioDTO usuarioInvalido = new UsuarioDTO();
        usuarioInvalido.setIdUsuario("123"); // Cédula muy corta
        usuarioInvalido.setNombres(""); // Nombres vacíos
        usuarioInvalido.setApellidos("García");
        usuarioInvalido.setNickname("jg"); // Nickname muy corto
        usuarioInvalido.setContrasena("123"); // Contraseña muy corta
        usuarioInvalido.setEstado("ESTADO_INVALIDO"); // Estado inválido
        usuarioInvalido.setEmail("email_invalido"); // Email inválido

        // Intentar crear usuario inválido
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());

        // Verificar que no se guardó en la base de datos
        assertThat(usuarioRepository.count()).isEqualTo(0);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debería manejar usuarios no encontrados correctamente")
    void deberiaManejarUsuariosNoEncontradosCorrectamente() throws Exception {
        // Intentar obtener usuario que no existe
        mockMvc.perform(get("/api/usuarios/9999999999"))
                .andExpect(status().isNotFound());

        // Intentar actualizar usuario que no existe
        UsuarioDTO usuarioActualizado = crearUsuarioDTO("9999999999", "No", "Existe", "noexiste", "ACTIVO");
        
        mockMvc.perform(put("/api/usuarios/9999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound());

        // Intentar eliminar usuario que no existe
        mockMvc.perform(delete("/api/usuarios/9999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debería persistir correctamente en base de datos")
    void deberiaPersistirCorrectamenteEnBaseDeDatos() throws Exception {
        UsuarioDTO nuevoUsuario = crearUsuarioDTO("1754392635", "Test", "Usuario", "testuser", "ACTIVO");

        // Crear usuario
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated());

        // Verificar persistencia usando repository directamente
        Usuario usuarioEnBD = usuarioRepository.findByNickname("testuser").orElse(null);
        assertThat(usuarioEnBD).isNotNull();
        assertThat(usuarioEnBD.getIdUsuario()).isEqualTo("1754392635");
        assertThat(usuarioEnBD.getNombres()).isEqualTo("Test");
        assertThat(usuarioEnBD.getApellidos()).isEqualTo("Usuario");
        assertThat(usuarioEnBD.getNickname()).isEqualTo("testuser");
        assertThat(usuarioEnBD.getEstado()).isEqualTo("ACTIVO");
        assertThat(usuarioEnBD.getEmail()).isEqualTo("test@ejemplo.com");

        // Verificar que la contraseña fue encriptada
        assertThat(usuarioEnBD.getContrasena()).isNotEqualTo("Password123!");
        assertThat(usuarioEnBD.getContrasena()).isNotEmpty();
    }

    private UsuarioDTO crearUsuarioDTO(String cedula, String nombres, String apellidos, String nickname, String estado) {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(cedula);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setNickname(nickname);
        usuario.setContrasena("Password123!");
        usuario.setEstado(estado);
        usuario.setEmail(nickname.toLowerCase() + "@ejemplo.com");
        usuario.setMfaHabilitado(false);
        return usuario;
    }
}