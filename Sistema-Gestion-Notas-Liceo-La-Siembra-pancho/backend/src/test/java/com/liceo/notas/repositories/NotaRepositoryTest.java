package com.liceo.notas.repositories;

import com.liceo.notas.entities.Nota;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.entities.Materia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/*
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests para NotaRepository")
class NotaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotaRepository notaRepository;

    private Usuario estudiante1;
    private Usuario estudiante2;
    private Materia matematicas;
    private Materia fisica;
    private Nota nota1;
    private Nota nota2;
    private Nota nota3;
    private Nota nota4;

    @BeforeEach
    void setUp() {
        // Crear estudiantes
        estudiante1 = new Usuario();
        estudiante1.setCedula("0504110438");
        estudiante1.setNombre("Juan");
        estudiante1.setApellido("Pérez");
        estudiante1.setEmail("juan.perez@test.com");
        estudiante1.setTelefono("0987654321");
        estudiante1.setPassword("password123");
        entityManager.persistAndFlush(estudiante1);

        estudiante2 = new Usuario();
        estudiante2.setCedula("1751983014");
        estudiante2.setNombre("María");
        estudiante2.setApellido("González");
        estudiante2.setEmail("maria.gonzalez@test.com");
        estudiante2.setTelefono("0987654322");
        estudiante2.setPassword("password456");
        entityManager.persistAndFlush(estudiante2);

        // Crear materias
        matematicas = new Materia();
        matematicas.setNombreMateria("Matemáticas");
        entityManager.persistAndFlush(matematicas);

        fisica = new Materia();
        fisica.setNombreMateria("Física");
        entityManager.persistAndFlush(fisica);

        // Crear notas
        nota1 = new Nota();
        nota1.setUsuario(estudiante1);
        nota1.setMateria(matematicas);
        nota1.setNota(18.5);
        nota1.setParcial(1);
        entityManager.persistAndFlush(nota1);

        nota2 = new Nota();
        nota2.setUsuario(estudiante1);
        nota2.setMateria(matematicas);
        nota2.setNota(17.0);
        nota2.setParcial(2);
        entityManager.persistAndFlush(nota2);

        nota3 = new Nota();
        nota3.setUsuario(estudiante1);
        nota3.setMateria(fisica);
        nota3.setNota(16.5);
        nota3.setParcial(1);
        entityManager.persistAndFlush(nota3);

        nota4 = new Nota();
        nota4.setUsuario(estudiante2);
        nota4.setMateria(matematicas);
        nota4.setNota(19.0);
        nota4.setParcial(1);
        entityManager.persistAndFlush(nota4);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar notas por ID de usuario")
    void deberiaEncontrarNotasPorIdUsuario() {
        List<Nota> notasEstudiante1 = notaRepository.findByUsuarioIdUsuario("0504110438");
        
        assertThat(notasEstudiante1).hasSize(3);
        assertThat(notasEstudiante1).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 17.0, 16.5);
    }

    @Test
    @DisplayName("Debería encontrar notas por ID de materia")
    void deberiaEncontrarNotasPorIdMateria() {
        List<Nota> notasMatematicas = notaRepository.findByMateriaId(matematicas.getId());
        
        assertThat(notasMatematicas).hasSize(3);
        assertThat(notasMatematicas).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 17.0, 19.0);
    }

    @Test
    @DisplayName("Debería encontrar notas por usuario y materia")
    void deberiaEncontrarNotasPorUsuarioYMateria() {
        List<Nota> notasEstudiante1Matematicas = notaRepository
            .findByUsuarioIdUsuarioAndMateriaId("0504110438", matematicas.getId());
        
        assertThat(notasEstudiante1Matematicas).hasSize(2);
        assertThat(notasEstudiante1Matematicas).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 17.0);
        assertThat(notasEstudiante1Matematicas).extracting("parcial")
            .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    @DisplayName("Debería encontrar notas por usuario y parcial")
    void deberiaEncontrarNotasPorUsuarioYParcial() {
        List<Nota> notasEstudiante1Parcial1 = notaRepository
            .findByUsuarioIdUsuarioAndParcial("0504110438", 1);
        
        assertThat(notasEstudiante1Parcial1).hasSize(2);
        assertThat(notasEstudiante1Parcial1).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 16.5);
        assertThat(notasEstudiante1Parcial1).extracting("materia.nombreMateria")
            .containsExactlyInAnyOrder("Matemáticas", "Física");
    }

    @Test
    @DisplayName("Debería encontrar notas por materia y parcial")
    void deberiaEncontrarNotasPorMateriaYParcial() {
        List<Nota> notasMatematicasParcial1 = notaRepository
            .findByMateriaIdAndParcial(matematicas.getId(), 1);
        
        assertThat(notasMatematicasParcial1).hasSize(2);
        assertThat(notasMatematicasParcial1).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 19.0);
        assertThat(notasMatematicasParcial1).extracting("usuario.cedula")
            .containsExactlyInAnyOrder("0504110438", "1751983014");
    }

    @Test
    @DisplayName("Debería encontrar nota específica por usuario, materia y parcial")
    void deberiaEncontrarNotaEspecificaPorUsuarioMateriaYParcial() {
        Nota notaEspecifica = notaRepository
            .findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", matematicas.getId(), 1);
        
        assertThat(notaEspecifica).isNotNull();
        assertThat(notaEspecifica.getNota()).isEqualTo(18.5);
        assertThat(notaEspecifica.getUsuario().getCedula()).isEqualTo("0504110438");
        assertThat(notaEspecifica.getMateria().getNombreMateria()).isEqualTo("Matemáticas");
        assertThat(notaEspecifica.getParcial()).isEqualTo(1);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay notas para usuario")
    void deberiaRetornarListaVaciaCuandoNoHayNotasParaUsuario() {
        List<Nota> notasUsuarioInexistente = notaRepository.findByUsuarioIdUsuario("1234567890");
        
        assertThat(notasUsuarioInexistente).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay notas para materia")
    void deberiaRetornarListaVaciaCuandoNoHayNotasParaMateria() {
        List<Nota> notasMateriaInexistente = notaRepository.findByMateriaId(999);
        
        assertThat(notasMateriaInexistente).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar null cuando no encuentra nota específica")
    void deberiaRetornarNullCuandoNoEncuentraNotaEspecifica() {
        Nota notaInexistente = notaRepository
            .findByUsuarioIdUsuarioAndMateriaIdAndParcial("0504110438", matematicas.getId(), 3);
        
        assertThat(notaInexistente).isNull();
    }

    @Test
    @DisplayName("Debería manejar múltiples estudiantes con cédulas válidas")
    void deberiaManejarMultiplesEstudiantesConCedulasValidas() {
        // Crear más estudiantes con cédulas válidas
        Usuario estudiante3 = new Usuario();
        estudiante3.setCedula("1722070560");
        estudiante3.setNombre("Carlos");
        estudiante3.setApellido("Rodríguez");
        estudiante3.setEmail("carlos.rodriguez@test.com");
        estudiante3.setTelefono("0987654323");
        estudiante3.setPassword("password789");
        entityManager.persistAndFlush(estudiante3);

        Usuario estudiante4 = new Usuario();
        estudiante4.setCedula("1756916233");
        estudiante4.setNombre("Ana");
        estudiante4.setApellido("López");
        estudiante4.setEmail("ana.lopez@test.com");
        estudiante4.setTelefono("0987654324");
        estudiante4.setPassword("passwordabc");
        entityManager.persistAndFlush(estudiante4);

        // Crear notas para estos estudiantes
        Nota notaCarlos = new Nota();
        notaCarlos.setUsuario(estudiante3);
        notaCarlos.setMateria(matematicas);
        notaCarlos.setNota(15.0);
        notaCarlos.setParcial(1);
        entityManager.persistAndFlush(notaCarlos);

        Nota notaAna = new Nota();
        notaAna.setUsuario(estudiante4);
        notaAna.setMateria(fisica);
        notaAna.setNota(20.0);
        notaAna.setParcial(1);
        entityManager.persistAndFlush(notaAna);

        entityManager.clear();

        // Verificar consultas
        List<Nota> notasCarlos = notaRepository.findByUsuarioIdUsuario("1722070560");
        List<Nota> notasAna = notaRepository.findByUsuarioIdUsuario("1756916233");

        assertThat(notasCarlos).hasSize(1);
        assertThat(notasCarlos.get(0).getNota()).isEqualTo(15.0);

        assertThat(notasAna).hasSize(1);
        assertThat(notasAna.get(0).getNota()).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Debería manejar diferentes parciales correctamente")
    void deberiaManejarDiferentesParcialesCorrectamente() {
        // Crear notas para los 3 parciales
        Nota notaParcial2 = new Nota();
        notaParcial2.setUsuario(estudiante2);
        notaParcial2.setMateria(matematicas);
        notaParcial2.setNota(18.0);
        notaParcial2.setParcial(2);
        entityManager.persistAndFlush(notaParcial2);

        Nota notaParcial3 = new Nota();
        notaParcial3.setUsuario(estudiante2);
        notaParcial3.setMateria(matematicas);
        notaParcial3.setNota(17.5);
        notaParcial3.setParcial(3);
        entityManager.persistAndFlush(notaParcial3);

        entityManager.clear();

        // Verificar parciales
        List<Nota> notasParcial1 = notaRepository.findByMateriaIdAndParcial(matematicas.getId(), 1);
        List<Nota> notasParcial2 = notaRepository.findByMateriaIdAndParcial(matematicas.getId(), 2);
        List<Nota> notasParcial3 = notaRepository.findByMateriaIdAndParcial(matematicas.getId(), 3);

        assertThat(notasParcial1).hasSize(2); // Juan y María
        assertThat(notasParcial2).hasSize(2); // Juan y María
        assertThat(notasParcial3).hasSize(1); // Solo María
    }

    @Test
    @DisplayName("Debería manejar notas con diferentes rangos de calificación")
    void deberiaManejarNotasConDiferentesRangosDeCalificacion() {
        // Crear notas con diferentes rangos
        Usuario estudiante5 = new Usuario();
        estudiante5.setCedula("1754392635");
        estudiante5.setNombre("Pedro");
        estudiante5.setApellido("Silva");
        estudiante5.setEmail("pedro.silva@test.com");
        estudiante5.setTelefono("0987654325");
        estudiante5.setPassword("passwordxyz");
        entityManager.persistAndFlush(estudiante5);

        // Nota reprobatoria
        Nota notaReprobatoria = new Nota();
        notaReprobatoria.setUsuario(estudiante5);
        notaReprobatoria.setMateria(matematicas);
        notaReprobatoria.setNota(5.5);
        notaReprobatoria.setParcial(1);
        entityManager.persistAndFlush(notaReprobatoria);

        // Nota aprobatoria mínima
        Nota notaAprobatoria = new Nota();
        notaAprobatoria.setUsuario(estudiante5);
        notaAprobatoria.setMateria(fisica);
        notaAprobatoria.setNota(7.0);
        notaAprobatoria.setParcial(1);
        entityManager.persistAndFlush(notaAprobatoria);

        // Nota perfecta
        Nota notaPerfecta = new Nota();
        notaPerfecta.setUsuario(estudiante5);
        notaPerfecta.setMateria(matematicas);
        notaPerfecta.setNota(20.0);
        notaPerfecta.setParcial(2);
        entityManager.persistAndFlush(notaPerfecta);

        entityManager.clear();

        List<Nota> notasPedro = notaRepository.findByUsuarioIdUsuario("1754392635");

        assertThat(notasPedro).hasSize(3);
        assertThat(notasPedro).extracting("nota")
            .containsExactlyInAnyOrder(5.5, 7.0, 20.0);
    }

    @Test
    @DisplayName("Debería guardar y recuperar nota correctamente")
    void deberiaGuardarYRecuperarNotaCorrectamente() {
        // Crear nueva nota
        Nota nuevaNota = new Nota();
        nuevaNota.setUsuario(estudiante1);
        nuevaNota.setMateria(fisica);
        nuevaNota.setNota(14.75);
        nuevaNota.setParcial(2);

        // Guardar
        Nota notaGuardada = notaRepository.save(nuevaNota);

        // Verificar que se guardó correctamente
        assertThat(notaGuardada.getId()).isNotNull();
        assertThat(notaGuardada.getNota()).isEqualTo(14.75);

        // Recuperar por ID
        Nota notaRecuperada = notaRepository.findById(notaGuardada.getId()).orElse(null);
        
        assertThat(notaRecuperada).isNotNull();
        assertThat(notaRecuperada.getNota()).isEqualTo(14.75);
        assertThat(notaRecuperada.getUsuario().getCedula()).isEqualTo("0504110438");
        assertThat(notaRecuperada.getMateria().getNombreMateria()).isEqualTo("Física");
        assertThat(notaRecuperada.getParcial()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debería eliminar nota correctamente")
    void deberiaEliminarNotaCorrectamente() {
        Integer notaId = nota1.getId();
        
        // Verificar que existe
        assertThat(notaRepository.findById(notaId)).isPresent();
        
        // Eliminar
        notaRepository.deleteById(notaId);
        
        // Verificar que se eliminó
        assertThat(notaRepository.findById(notaId)).isEmpty();
    }

    @Test
    @DisplayName("Debería actualizar nota correctamente")
    void deberiaActualizarNotaCorrectamente() {
        // Obtener nota existente
        Nota notaExistente = notaRepository.findById(nota1.getId()).orElse(null);
        assertThat(notaExistente).isNotNull();
        
        Double notaOriginal = notaExistente.getNota();
        Double nuevaNota = 19.5;
        
        // Actualizar
        notaExistente.setNota(nuevaNota);
        Nota notaActualizada = notaRepository.save(notaExistente);
        
        // Verificar actualización
        assertThat(notaActualizada.getNota()).isEqualTo(nuevaNota);
        assertThat(notaActualizada.getNota()).isNotEqualTo(notaOriginal);
        
        // Verificar en base de datos
        Nota notaVerificada = notaRepository.findById(nota1.getId()).orElse(null);
        assertThat(notaVerificada.getNota()).isEqualTo(nuevaNota);
    }

    @Test
    @DisplayName("Debería contar notas correctamente")
    void deberiaContarNotasCorrectamente() {
        long totalNotas = notaRepository.count();
        assertThat(totalNotas).isEqualTo(4); // 4 notas creadas en setUp
    }

    @Test
    @DisplayName("Debería encontrar todas las notas")
    void deberiaEncontrarTodasLasNotas() {
        List<Nota> todasLasNotas = notaRepository.findAll();
        
        assertThat(todasLasNotas).hasSize(4);
        assertThat(todasLasNotas).extracting("nota")
            .containsExactlyInAnyOrder(18.5, 17.0, 16.5, 19.0);
    }
}*/