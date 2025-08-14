package com.liceo.notas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.services.UsuarioService;
import com.liceo.notas.services.AuthService; // <-- mockeado para evitar NoSuchBeanDefinition

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // <-- addFilters=false
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // <-- desactiva los SecurityFilters (incluye JwtAuthenticationFilter)
@ActiveProfiles("test")
@DisplayName("Tests para UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    // Mock necesario para que el contexto arranque si el filtro/security config lo requiere
    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario("0504110438");
        usuarioDTO.setNombres("Juan Carlos");
        usuarioDTO.setApellidos("García López");
        usuarioDTO.setNickname("jgarcia");
        usuarioDTO.setContrasena("Password123!");
        usuarioDTO.setEstado("ACTIVO");
        usuarioDTO.setEmail("juan@ejemplo.com");
        usuarioDTO.setMfaHabilitado(false);
        usuarioDTO.setEmailVerificado(true);
    }

    @Test
    @DisplayName("POST /api/usuarios - Debería crear usuario exitosamente")
    void deberiaCrearUsuarioExitosamente() throws Exception {
        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value("0504110438"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.apellidos").value("García López"))
                .andExpect(jsonPath("$.nickname").value("jgarcia"))
                .andExpect(jsonPath("$.email").value("juan@ejemplo.com"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    @DisplayName("POST /api/usuarios - Debería fallar con datos inválidos")
    void deberiaFallarConDatosInvalidos() throws Exception {
        UsuarioDTO usuarioInvalido = new UsuarioDTO();
        usuarioInvalido.setIdUsuario("");           // cédula vacía
        usuarioInvalido.setNombres("");             // nombres vacíos
        usuarioInvalido.setEmail("email_invalido"); // email inválido

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Debería obtener usuario por ID exitosamente")
    void deberiaObtenerUsuarioPorIdExitosamente() throws Exception {
        when(usuarioService.obtenerPorId("0504110438")).thenReturn(Optional.of(usuarioDTO));

        mockMvc.perform(get("/api/usuarios/0504110438"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("0504110438"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.apellidos").value("García López"))
                .andExpect(jsonPath("$.nickname").value("jgarcia"));
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Debería retornar 404 cuando usuario no existe")
    void deberiaRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.obtenerPorId("9999999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/9999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/usuarios - Debería listar todos los usuarios")
    void deberiaListarTodosLosUsuarios() throws Exception {
        UsuarioDTO usuario2 = new UsuarioDTO();
        usuario2.setIdUsuario("1751983014");
        usuario2.setNombres("María Elena");
        usuario2.setApellidos("Rodríguez Pérez");
        usuario2.setNickname("mrodriguez");
        usuario2.setEmail("maria@ejemplo.com");
        usuario2.setEstado("ACTIVO");

        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO, usuario2);
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idUsuario").value("0504110438"))
                .andExpect(jsonPath("$[1].idUsuario").value("1751983014"))
                .andExpect(jsonPath("$[0].nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$[1].nombres").value("María Elena"));
    }

    @Test
    @DisplayName("GET /api/usuarios - Debería retornar lista vacía cuando no hay usuarios")
    void deberiaRetornarListaVaciaCuandoNoHayUsuarios() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Debería actualizar usuario exitosamente")
    void deberiaActualizarUsuarioExitosamente() throws Exception {
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setIdUsuario("0504110438");
        usuarioActualizado.setNombres("Juan Carlos Actualizado");
        usuarioActualizado.setApellidos("García López");
        usuarioActualizado.setNickname("jgarcia");
        usuarioActualizado.setContrasena("NewPassword123!");
        usuarioActualizado.setEstado("ACTIVO");
        usuarioActualizado.setEmail("juan_nuevo@ejemplo.com");

        when(usuarioService.actualizarUsuario(anyString(), any(UsuarioDTO.class)))
                .thenReturn(Optional.of(usuarioActualizado));

        mockMvc.perform(put("/api/usuarios/0504110438")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Juan Carlos Actualizado"))
                .andExpect(jsonPath("$.email").value("juan_nuevo@ejemplo.com"));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Debería retornar 404 cuando usuario a actualizar no existe")
    void deberiaRetornar404CuandoUsuarioAActualizarNoExiste() throws Exception {
        when(usuarioService.actualizarUsuario(anyString(), any(UsuarioDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/9999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Debería eliminar usuario exitosamente")
    void deberiaEliminarUsuarioExitosamente() throws Exception {
        when(usuarioService.eliminarUsuario("0504110438")).thenReturn(true);

        mockMvc.perform(delete("/api/usuarios/0504110438"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Debería retornar 404 cuando usuario a eliminar no existe")
    void deberiaRetornar404CuandoUsuarioAEliminarNoExiste() throws Exception {
        when(usuarioService.eliminarUsuario("9999999999")).thenReturn(false);

        mockMvc.perform(delete("/api/usuarios/9999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/usuarios/por-estado/{estado} - Debería listar usuarios por estado")
    void deberiaListarUsuariosPorEstado() throws Exception {
        List<UsuarioDTO> usuariosActivos = Arrays.asList(usuarioDTO);
        when(usuarioService.listarPorEstado("ACTIVO")).thenReturn(usuariosActivos);

        mockMvc.perform(get("/api/usuarios/por-estado/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"))
                .andExpect(jsonPath("$[0].idUsuario").value("0504110438"));
    }

    @Test
    @DisplayName("GET /api/usuarios/por-estado/{estado} - Debería retornar lista vacía para estado sin usuarios")
    void deberiaRetornarListaVaciaParaEstadoSinUsuarios() throws Exception {
        when(usuarioService.listarPorEstado("INACTIVO")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/usuarios/por-estado/INACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /api/usuarios - Debería fallar con nickname muy corto")
    void deberiaFallarConNicknameMuyCorto() throws Exception {
        UsuarioDTO usuarioConNicknameCorto = new UsuarioDTO();
        usuarioConNicknameCorto.setIdUsuario("0504110438");
        usuarioConNicknameCorto.setNombres("Juan");
        usuarioConNicknameCorto.setApellidos("García");
        usuarioConNicknameCorto.setNickname("abc"); // Muy corto
        usuarioConNicknameCorto.setContrasena("Password123!");
        usuarioConNicknameCorto.setEstado("ACTIVO");
        usuarioConNicknameCorto.setEmail("juan@ejemplo.com");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioConNicknameCorto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/usuarios - Debería fallar con contraseña débil")
    void deberiaFallarConContrasenaDebil() throws Exception {
        UsuarioDTO usuarioConContrasenaDebil = new UsuarioDTO();
        usuarioConContrasenaDebil.setIdUsuario("0504110438");
        usuarioConContrasenaDebil.setNombres("Juan");
        usuarioConContrasenaDebil.setApellidos("García");
        usuarioConContrasenaDebil.setNickname("jgarcia");
        usuarioConContrasenaDebil.setContrasena("123456"); // Contraseña débil
        usuarioConContrasenaDebil.setEstado("ACTIVO");
        usuarioConContrasenaDebil.setEmail("juan@ejemplo.com");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioConContrasenaDebil)))
                .andExpect(status().isBadRequest());
    }
}
