package com.liceo.notas.services;
/*
import com.liceo.notas.dtos.NotaDTO;
import com.liceo.notas.entities.Nota;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.Materia;
import com.liceo.notas.repositories.NotaRepository;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.repositories.MateriaRepository;
import com.liceo.notas.services.ServiceImpl.NotaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para NotaServiceImpl")
class NotaServiceImplTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MateriaRepository materiaRepository;

    @InjectMocks
    private NotaServiceImpl notaService;

    private Usuario usuario;
    private Materia materia;
    private Nota nota;
    private NotaDTO notaDTO;

    @BeforeEach
    void setUp() {
        // Usuario de prueba
        usuario = new Usuario();
        usuario.setCedula("0504110438");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");

        // Materia de prueba
        materia = new Materia();
        materia.setId(1);
        materia.setNombreMateria("Matemáticas");

        // Nota de prueba
        nota = new Nota();
        nota.setId(1);
        nota.setUsuario(usuario);
        nota.setMateria(materia);
        nota.setNota(18.5);
        nota.setParcial(1);

        // DTO de prueba
        notaDTO = new NotaDTO();
        notaDTO.setId(1);
        notaDTO.setIdUsuario("0504110438");
        notaDTO.setIdMateria(1);
        notaDTO.setNota(18.5);
        notaDTO.setParcial(1);
    }

    @Test
    @DisplayName("Debería registrar nota correctamente")
    void deberiaRegistrarNotaCorrectamente() {
        // Given
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1)).thenReturn(null);
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        // When
        NotaDTO resultado = notaService.registrarNota(notaDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.getIdMateria()).isEqualTo(1);
        assertThat(resultado.getNota()).isEqualTo(18.5);
        assertThat(resultado.getParcial()).isEqualTo(1);

        verify(usuarioRepository).findById("0504110438");
        verify(materiaRepository).findById(1);
        verify(notaRepository).findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1);
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando usuario no existe")
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> notaService.registrarNota(notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuario no encontrado");

        verify(usuarioRepository).findById("0504110438");
        verify(materiaRepository, never()).findById(any());
        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando materia no existe")
    void deberiaLanzarExcepcionCuandoMateriaNoExiste() {
        // Given
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> notaService.registrarNota(notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Materia no encontrada");

        verify(usuarioRepository).findById("0504110438");
        verify(materiaRepository).findById(1);
        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando nota está fuera del rango válido")
    void deberiaLanzarExcepcionCuandoNotaEstaFueraDelRangoValido() {
        // Given
        notaDTO.setNota(25.0); // Nota inválida
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));

        // When & Then
        assertThatThrownBy(() -> notaService.registrarNota(notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("La nota debe estar entre 0 y 20");

        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando parcial está fuera del rango válido")
    void deberiaLanzarExcepcionCuandoParcialEstaFueraDelRangoValido() {
        // Given
        notaDTO.setParcial(4); // Parcial inválido
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));

        // When & Then
        assertThatThrownBy(() -> notaService.registrarNota(notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("El parcial debe estar entre 1 y 3");

        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando ya existe nota para usuario, materia y parcial")
    void deberiaLanzarExcepcionCuandoYaExisteNotaParaUsuarioMateriaYParcial() {
        // Given
        Nota notaExistente = new Nota();
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1))
            .thenReturn(notaExistente);

        // When & Then
        assertThatThrownBy(() -> notaService.registrarNota(notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Ya existe una nota para este estudiante en esta materia y parcial");

        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería listar notas por usuario")
    void deberiaListarNotasPorUsuario() {
        // Given
        List<Nota> notas = Arrays.asList(nota);
        when(notaRepository.findByUsuarioIdUsuario("0504110438")).thenReturn(notas);

        // When
        List<NotaDTO> resultado = notaService.listarPorUsuario("0504110438");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo("0504110438");

        verify(notaRepository).findByUsuarioIdUsuario("0504110438");
    }

    @Test
    @DisplayName("Debería listar notas por materia")
    void deberiaListarNotasPorMateria() {
        // Given
        List<Nota> notas = Arrays.asList(nota);
        when(notaRepository.findByMateriaId(1)).thenReturn(notas);

        // When
        List<NotaDTO> resultado = notaService.listarPorMateria(1);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdMateria()).isEqualTo(1);

        verify(notaRepository).findByMateriaId(1);
    }

    @Test
    @DisplayName("Debería listar notas por usuario y materia")
    void deberiaListarNotasPorUsuarioYMateria() {
        // Given
        List<Nota> notas = Arrays.asList(nota);
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaId("0504110438", 1)).thenReturn(notas);

        // When
        List<NotaDTO> resultado = notaService.listarPorUsuarioYMateria("0504110438", 1);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.get(0).getIdMateria()).isEqualTo(1);

        verify(notaRepository).findByUsuarioIdUsuarioAndMateriaId("0504110438", 1);
    }

    @Test
    @DisplayName("Debería listar notas por usuario y parcial")
    void deberiaListarNotasPorUsuarioYParcial() {
        // Given
        List<Nota> notas = Arrays.asList(nota);
        when(notaRepository.findByUsuarioIdUsuarioAndParcial("0504110438", 1)).thenReturn(notas);

        // When
        List<NotaDTO> resultado = notaService.listarPorUsuarioYParcial("0504110438", 1);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.get(0).getParcial()).isEqualTo(1);

        verify(notaRepository).findByUsuarioIdUsuarioAndParcial("0504110438", 1);
    }

    @Test
    @DisplayName("Debería listar notas por materia y parcial")
    void deberiaListarNotasPorMateriaYParcial() {
        // Given
        List<Nota> notas = Arrays.asList(nota);
        when(notaRepository.findByMateriaIdAndParcial(1, 1)).thenReturn(notas);

        // When
        List<NotaDTO> resultado = notaService.listarPorMateriaYParcial(1, 1);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdMateria()).isEqualTo(1);
        assertThat(resultado.get(0).getParcial()).isEqualTo(1);

        verify(notaRepository).findByMateriaIdAndParcial(1, 1);
    }

    @Test
    @DisplayName("Debería obtener nota específica")
    void deberiaObtenerNotaEspecifica() {
        // Given
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1))
            .thenReturn(nota);

        // When
        NotaDTO resultado = notaService.obtenerNotaEspecifica("0504110438", 1, 1);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdUsuario()).isEqualTo("0504110438");
        assertThat(resultado.getIdMateria()).isEqualTo(1);
        assertThat(resultado.getParcial()).isEqualTo(1);

        verify(notaRepository).findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1);
    }

    @Test
    @DisplayName("Debería actualizar nota correctamente")
    void deberiaActualizarNotaCorrectamente() {
        // Given
        NotaDTO notaActualizada = new NotaDTO();
        notaActualizada.setIdUsuario("0504110438");
        notaActualizada.setIdMateria(1);
        notaActualizada.setNota(19.0);
        notaActualizada.setParcial(1);

        when(notaRepository.findById(1)).thenReturn(Optional.of(nota));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        // When
        NotaDTO resultado = notaService.actualizarNota(1, notaActualizada);

        // Then
        assertThat(resultado).isNotNull();
        verify(notaRepository).findById(1);
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar nota inexistente")
    void deberiaLanzarExcepcionAlActualizarNotaInexistente() {
        // Given
        when(notaRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> notaService.actualizarNota(999, notaDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Nota no encontrada");

        verify(notaRepository).findById(999);
        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar con nota fuera de rango")
    void deberiaLanzarExcepcionAlActualizarConNotaFueraDeRango() {
        // Given
        NotaDTO notaInvalida = new NotaDTO();
        notaInvalida.setIdUsuario("0504110438");
        notaInvalida.setIdMateria(1);
        notaInvalida.setNota(-5.0); // Nota inválida
        notaInvalida.setParcial(1);

        when(notaRepository.findById(1)).thenReturn(Optional.of(nota));

        // When & Then
        assertThatThrownBy(() -> notaService.actualizarNota(1, notaInvalida))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("La nota debe estar entre 0 y 20");

        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería eliminar nota correctamente")
    void deberiaEliminarNotaCorrectamente() {
        // Given
        when(notaRepository.existsById(1)).thenReturn(true);

        // When
        notaService.eliminarNota(1);

        // Then
        verify(notaRepository).existsById(1);
        verify(notaRepository).deleteById(1);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar nota inexistente")
    void deberiaLanzarExcepcionAlEliminarNotaInexistente() {
        // Given
        when(notaRepository.existsById(999)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> notaService.eliminarNota(999))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Nota no encontrada");

        verify(notaRepository).existsById(999);
        verify(notaRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("Debería listar todas las notas")
    void deberiaListarTodasLasNotas() {
        // Given
        List<Nota> todasLasNotas = Arrays.asList(nota);
        when(notaRepository.findAll()).thenReturn(todasLasNotas);

        // When
        List<NotaDTO> resultado = notaService.listarTodas();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1);

        verify(notaRepository).findAll();
    }

    @Test
    @DisplayName("Debería manejar cédulas ecuatorianas válidas")
    void deberiaManejarCedulasEcuatorianasValidas() {
        // Given
        String[] cedulasValidas = {"0504110438", "1751983014", "1722070560", "1756916233", "1754392635"};
        
        for (String cedula : cedulasValidas) {
            Usuario usuarioValido = new Usuario();
            usuarioValido.setCedula(cedula);
            
            NotaDTO notaConCedulaValida = new NotaDTO();
            notaConCedulaValida.setIdUsuario(cedula);
            notaConCedulaValida.setIdMateria(1);
            notaConCedulaValida.setNota(15.0);
            notaConCedulaValida.setParcial(1);

            when(usuarioRepository.findById(cedula)).thenReturn(Optional.of(usuarioValido));
            when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));
            when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial(cedula, 1, 1)).thenReturn(null);
            when(notaRepository.save(any(Nota.class))).thenReturn(nota);

            // When
            NotaDTO resultado = notaService.registrarNota(notaConCedulaValida);

            // Then
            assertThat(resultado).isNotNull();
            verify(usuarioRepository).findById(cedula);
        }
    }

    @Test
    @DisplayName("Debería validar rangos de notas del sistema ecuatoriano")
    void deberiaValidarRangosDeNotasDelSistemaEcuatoriano() {
        // Given
        Double[] notasValidas = {0.0, 7.0, 10.0, 14.0, 18.0, 20.0};
        
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, 1)).thenReturn(null);
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        for (Double notaValida : notasValidas) {
            NotaDTO notaConValorValido = new NotaDTO();
            notaConValorValido.setIdUsuario("0504110438");
            notaConValorValido.setIdMateria(1);
            notaConValorValido.setNota(notaValida);
            notaConValorValido.setParcial(1);

            // When & Then
            assertThat(notaService.registrarNota(notaConValorValido)).isNotNull();
        }
    }

    @Test
    @DisplayName("Debería validar los tres parciales del sistema")
    void deberiaValidarLosTresParcialesDelSistema() {
        // Given
        Integer[] parcialesValidos = {1, 2, 3};
        
        when(usuarioRepository.findById("0504110438")).thenReturn(Optional.of(usuario));
        when(materiaRepository.findById(1)).thenReturn(Optional.of(materia));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        for (Integer parcial : parcialesValidos) {
            when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 1, parcial)).thenReturn(null);
            
            NotaDTO notaConParcialValido = new NotaDTO();
            notaConParcialValido.setIdUsuario("0504110438");
            notaConParcialValido.setIdMateria(1);
            notaConParcialValido.setNota(15.0);
            notaConParcialValido.setParcial(parcial);

            // When & Then
            assertThat(notaService.registrarNota(notaConParcialValido)).isNotNull();
        }
    }

    @Test
    @DisplayName("Debería retornar null cuando no existe nota específica")
    void deberiaRetornarNullCuandoNoExisteNotaEspecifica() {
        // Given
        when(notaRepository.findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 999, 1))
            .thenReturn(null);

        // When
        NotaDTO resultado = notaService.obtenerNotaEspecifica("0504110438", 999, 1);

        // Then
        assertThat(resultado).isNull();

        verify(notaRepository).findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", 999, 1);
    }
}*/