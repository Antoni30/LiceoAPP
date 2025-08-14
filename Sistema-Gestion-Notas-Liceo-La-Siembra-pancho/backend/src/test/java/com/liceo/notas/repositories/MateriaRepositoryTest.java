package com.liceo.notas.repositories;

import com.liceo.notas.entities.Materia;
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
@DisplayName("Tests para MateriaRepository")
class MateriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MateriaRepository materiaRepository;

    private Materia matematicas;
    private Materia fisica;
    private Materia quimica;

    @BeforeEach
    void setUp() {
        // Crear materias básicas
        matematicas = new Materia();
        matematicas.setNombreMateria("Matemáticas");
        entityManager.persistAndFlush(matematicas);

        fisica = new Materia();
        fisica.setNombreMateria("Física");
        entityManager.persistAndFlush(fisica);

        quimica = new Materia();
        quimica.setNombreMateria("Química");
        entityManager.persistAndFlush(quimica);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar materia por nombre exacto")
    void deberiaEncontrarMateriaPorNombreExacto() {
        Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria("Matemáticas");
        
        assertThat(materiaEncontrada).isPresent();
        assertThat(materiaEncontrada.get().getNombreMateria()).isEqualTo("Matemáticas");
        assertThat(materiaEncontrada.get().getId()).isEqualTo(matematicas.getId());
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para materia inexistente")
    void deberiaRetornarOptionalVacioParaMateriaInexistente() {
        Optional<Materia> materiaInexistente = materiaRepository.findByNombreMateria("Biología");
        
        assertThat(materiaInexistente).isEmpty();
    }

    @Test
    @DisplayName("Debería ser sensible a mayúsculas y minúsculas")
    void deberiaSensibleAMayusculasYMinusculas() {
        Optional<Materia> materiaMinusculas = materiaRepository.findByNombreMateria("matemáticas");
        Optional<Materia> materiaMayusculas = materiaRepository.findByNombreMateria("MATEMÁTICAS");
        
        assertThat(materiaMinusculas).isEmpty();
        assertThat(materiaMayusculas).isEmpty();
    }

    @Test
    @DisplayName("Debería guardar nueva materia correctamente")
    void deberiaGuardarNuevaMateriaCorrectamente() {
        // Crear nueva materia
        Materia biologia = new Materia();
        biologia.setNombreMateria("Biología");
        
        // Guardar
        Materia materiaGuardada = materiaRepository.save(biologia);
        
        // Verificar que se guardó
        assertThat(materiaGuardada.getId()).isNotNull();
        assertThat(materiaGuardada.getNombreMateria()).isEqualTo("Biología");
        
        // Verificar que se puede encontrar
        Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria("Biología");
        assertThat(materiaEncontrada).isPresent();
        assertThat(materiaEncontrada.get().getId()).isEqualTo(materiaGuardada.getId());
    }

    @Test
    @DisplayName("Debería encontrar todas las materias")
    void deberiaEncontrarTodasLasMaterias() {
        List<Materia> todasLasMaterias = materiaRepository.findAll();
        
        assertThat(todasLasMaterias).hasSize(3);
        assertThat(todasLasMaterias).extracting("nombreMateria")
            .containsExactlyInAnyOrder("Matemáticas", "Física", "Química");
    }

    @Test
    @DisplayName("Debería manejar materias del currículo ecuatoriano EGB")
    void deberiaManejarMateriasDelCurriculoEcuatorianoEGB() {
        String[] materiasEGB = {
            "Lengua y Literatura",
            "Ciencias Naturales", 
            "Ciencias Sociales",
            "Inglés",
            "Educación Física",
            "Educación Cultural y Artística"
        };
        
        // Guardar todas las materias
        for (String nombreMateria : materiasEGB) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se pueden encontrar
        for (String nombreMateria : materiasEGB) {
            Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria(nombreMateria);
            assertThat(materiaEncontrada).isPresent();
            assertThat(materiaEncontrada.get().getNombreMateria()).isEqualTo(nombreMateria);
        }
    }

    @Test
    @DisplayName("Debería manejar materias del bachillerato general unificado")
    void deberiaManejarMateriasDelBachilleratoGeneralUnificado() {
        String[] materiasBGU = {
            "Historia",
            "Educación para la Ciudadanía",
            "Filosofía",
            "Emprendimiento y Gestión"
        };
        
        // Guardar materias del BGU
        for (String nombreMateria : materiasBGU) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se guardaron correctamente
        for (String nombreMateria : materiasBGU) {
            Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria(nombreMateria);
            assertThat(materiaEncontrada).isPresent();
        }
        
        // Verificar total de materias
        long totalMaterias = materiaRepository.count();
        assertThat(totalMaterias).isEqualTo(3 + materiasBGU.length); // 3 del setUp + nuevas
    }

    @Test
    @DisplayName("Debería manejar materias técnicas especializadas")
    void deberiaManejarMateriasTecnicasEspecializadas() {
        String[] materiasTecnicas = {
            "Informática Aplicada",
            "Programación",
            "Redes y Comunicaciones",
            "Base de Datos",
            "Contabilidad General",
            "Administración"
        };
        
        // Guardar materias técnicas
        for (String nombreMateria : materiasTecnicas) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se pueden buscar por nombre
        Optional<Materia> informatica = materiaRepository.findByNombreMateria("Informática Aplicada");
        Optional<Materia> programacion = materiaRepository.findByNombreMateria("Programación");
        Optional<Materia> baseDatos = materiaRepository.findByNombreMateria("Base de Datos");
        
        assertThat(informatica).isPresent();
        assertThat(programacion).isPresent();
        assertThat(baseDatos).isPresent();
    }

    @Test
    @DisplayName("Debería actualizar nombre de materia correctamente")
    void deberiaActualizarNombreDeMateriaCorrectamente() {
        // Obtener materia existente
        Optional<Materia> materiaExistente = materiaRepository.findByNombreMateria("Matemáticas");
        assertThat(materiaExistente).isPresent();
        
        Materia materia = materiaExistente.get();
        String nombreOriginal = materia.getNombreMateria();
        
        // Actualizar nombre
        materia.setNombreMateria("Matemática Avanzada");
        Materia materiaActualizada = materiaRepository.save(materia);
        
        // Verificar actualización
        assertThat(materiaActualizada.getNombreMateria()).isEqualTo("Matemática Avanzada");
        assertThat(materiaActualizada.getNombreMateria()).isNotEqualTo(nombreOriginal);
        
        // Verificar que el nombre original ya no existe
        Optional<Materia> nombreAnterior = materiaRepository.findByNombreMateria("Matemáticas");
        assertThat(nombreAnterior).isEmpty();
        
        // Verificar que el nuevo nombre existe
        Optional<Materia> nombreNuevo = materiaRepository.findByNombreMateria("Matemática Avanzada");
        assertThat(nombreNuevo).isPresent();
    }

    @Test
    @DisplayName("Debería eliminar materia correctamente")
    void deberiaEliminarMateriaCorrectamente() {
        Integer materiaId = fisica.getId();
        String nombreMateria = fisica.getNombreMateria();
        
        // Verificar que existe
        assertThat(materiaRepository.findById(materiaId)).isPresent();
        assertThat(materiaRepository.findByNombreMateria(nombreMateria)).isPresent();
        
        // Eliminar
        materiaRepository.deleteById(materiaId);
        
        // Verificar que se eliminó
        assertThat(materiaRepository.findById(materiaId)).isEmpty();
        assertThat(materiaRepository.findByNombreMateria(nombreMateria)).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar materias con caracteres especiales y acentos")
    void deberiaManejarMateriasConCaracteresEspecialesYAcentos() {
        String[] materiasConAcentos = {
            "Educación Física",
            "Filosofía", 
            "Educación Artística",
            "Ciencias Sociales",
            "Informática"
        };
        
        // Guardar materias con acentos
        for (String nombreMateria : materiasConAcentos) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se pueden encontrar con acentos
        for (String nombreMateria : materiasConAcentos) {
            Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria(nombreMateria);
            assertThat(materiaEncontrada).isPresent();
            assertThat(materiaEncontrada.get().getNombreMateria()).isEqualTo(nombreMateria);
        }
    }

    @Test
    @DisplayName("Debería manejar nombres de materias largos")
    void deberiaManejarNombresDeMateriasLargos() {
        String[] materiasLargas = {
            "Educación Cultural y Artística",
            "Educación para la Ciudadanía",
            "Desarrollo del Pensamiento Filosófico",
            "Investigación de Ciencia y Tecnología",
            "Emprendimiento y Gestión"
        };
        
        // Guardar materias con nombres largos
        for (String nombreMateria : materiasLargas) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se guardaron correctamente
        for (String nombreMateria : materiasLargas) {
            Optional<Materia> materiaEncontrada = materiaRepository.findByNombreMateria(nombreMateria);
            assertThat(materiaEncontrada).isPresent();
            assertThat(materiaEncontrada.get().getNombreMateria()).hasSize(nombreMateria.length());
        }
    }

    @Test
    @DisplayName("Debería manejar materias optativas")
    void deberiaManejarMateriasOptativas() {
        String[] materiasOptativas = {
            "Música",
            "Teatro",
            "Danza",
            "Artes Plásticas",
            "Ajedrez",
            "Robótica",
            "Oratoria"
        };
        
        // Guardar materias optativas
        for (String nombreMateria : materiasOptativas) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar algunas materias optativas específicas
        Optional<Materia> musica = materiaRepository.findByNombreMateria("Música");
        Optional<Materia> robotica = materiaRepository.findByNombreMateria("Robótica");
        Optional<Materia> ajedrez = materiaRepository.findByNombreMateria("Ajedrez");
        
        assertThat(musica).isPresent();
        assertThat(robotica).isPresent();
        assertThat(ajedrez).isPresent();
    }

    @Test
    @DisplayName("Debería manejar materias por niveles")
    void deberiaManejarMateriasPorNiveles() {
        String[] materiasPorNiveles = {
            "Matemática 1° BGU",
            "Matemática 2° BGU", 
            "Matemática 3° BGU",
            "Física 1° BGU",
            "Física 2° BGU",
            "Química 2° BGU",
            "Química 3° BGU"
        };
        
        // Guardar materias por niveles
        for (String nombreMateria : materiasPorNiveles) {
            Materia materia = new Materia();
            materia.setNombreMateria(nombreMateria);
            materiaRepository.save(materia);
        }
        
        // Verificar que se pueden encontrar por nivel específico
        Optional<Materia> mate1 = materiaRepository.findByNombreMateria("Matemática 1° BGU");
        Optional<Materia> mate2 = materiaRepository.findByNombreMateria("Matemática 2° BGU");
        Optional<Materia> mate3 = materiaRepository.findByNombreMateria("Matemática 3° BGU");
        
        assertThat(mate1).isPresent();
        assertThat(mate2).isPresent();
        assertThat(mate3).isPresent();
        
        // Verificar que los nombres son exactos
        assertThat(mate1.get().getNombreMateria()).isEqualTo("Matemática 1° BGU");
        assertThat(mate2.get().getNombreMateria()).isEqualTo("Matemática 2° BGU");
        assertThat(mate3.get().getNombreMateria()).isEqualTo("Matemática 3° BGU");
    }

    @Test
    @DisplayName("Debería contar materias correctamente")
    void deberiaContarMateriasCorrectamente() {
        long totalMaterias = materiaRepository.count();
        assertThat(totalMaterias).isEqualTo(3); // 3 materias del setUp
        
        // Agregar una materia más
        Materia historia = new Materia();
        historia.setNombreMateria("Historia");
        materiaRepository.save(historia);
        
        // Verificar nuevo conteo
        long nuevoTotal = materiaRepository.count();
        assertThat(nuevoTotal).isEqualTo(4);
    }

    @Test
    @DisplayName("Debería existir por ID correctamente")
    void deberiaExistirPorIdCorrectamente() {
        // Verificar que existen las materias del setUp
        assertThat(materiaRepository.existsById(matematicas.getId())).isTrue();
        assertThat(materiaRepository.existsById(fisica.getId())).isTrue();
        assertThat(materiaRepository.existsById(quimica.getId())).isTrue();
        
        // Verificar que no existe ID inexistente
        assertThat(materiaRepository.existsById(999)).isFalse();
    }

    @Test
    @DisplayName("Debería encontrar materia por ID correctamente")
    void deberiaEncontrarMateriaPorIdCorrectamente() {
        Optional<Materia> materiaEncontrada = materiaRepository.findById(matematicas.getId());
        
        assertThat(materiaEncontrada).isPresent();
        assertThat(materiaEncontrada.get().getNombreMateria()).isEqualTo("Matemáticas");
        assertThat(materiaEncontrada.get().getId()).isEqualTo(matematicas.getId());
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para ID inexistente")
    void deberiaRetornarOptionalVacioParaIdInexistente() {
        Optional<Materia> materiaInexistente = materiaRepository.findById(999);
        
        assertThat(materiaInexistente).isEmpty();
    }
}