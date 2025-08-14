package com.liceo.notas.repositories;

import com.liceo.notas.entities.Rol;
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
@DisplayName("Tests para RolRepository")
class RolRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RolRepository rolRepository;

    private Rol rolEstudiante;
    private Rol rolDocente;
    private Rol rolAdministrador;

    @BeforeEach
    void setUp() {
        // Crear roles básicos del sistema
        rolEstudiante = new Rol();
        rolEstudiante.setNombre("ESTUDIANTE");
        entityManager.persistAndFlush(rolEstudiante);

        rolDocente = new Rol();
        rolDocente.setNombre("DOCENTE");
        entityManager.persistAndFlush(rolDocente);

        rolAdministrador = new Rol();
        rolAdministrador.setNombre("ADMINISTRADOR");
        entityManager.persistAndFlush(rolAdministrador);

        entityManager.clear();
    }

    @Test
    @DisplayName("Debería encontrar rol por nombre exacto")
    void deberiaEncontrarRolPorNombreExacto() {
        Optional<Rol> rolEncontrado = rolRepository.findByNombre("ESTUDIANTE");
        
        assertThat(rolEncontrado).isPresent();
        assertThat(rolEncontrado.get().getNombre()).isEqualTo("ESTUDIANTE");
        assertThat(rolEncontrado.get().getId()).isEqualTo(rolEstudiante.getId());
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para rol inexistente")
    void deberiaRetornarOptionalVacioParaRolInexistente() {
        Optional<Rol> rolInexistente = rolRepository.findByNombre("SUPERVISOR");
        
        assertThat(rolInexistente).isEmpty();
    }

    @Test
    @DisplayName("Debería ser sensible a mayúsculas y minúsculas")
    void deberiaSensibleAMayusculasYMinusculas() {
        Optional<Rol> rolMinusculas = rolRepository.findByNombre("estudiante");
        Optional<Rol> rolMixto = rolRepository.findByNombre("Estudiante");
        
        assertThat(rolMinusculas).isEmpty();
        assertThat(rolMixto).isEmpty();
    }

    @Test
    @DisplayName("Debería guardar nuevo rol correctamente")
    void deberiaGuardarNuevoRolCorrectamente() {
        // Crear nuevo rol
        Rol rolDirector = new Rol();
        rolDirector.setNombre("DIRECTOR");
        
        // Guardar
        Rol rolGuardado = rolRepository.save(rolDirector);
        
        // Verificar que se guardó
        assertThat(rolGuardado.getId()).isNotNull();
        assertThat(rolGuardado.getNombre()).isEqualTo("DIRECTOR");
        
        // Verificar que se puede encontrar
        Optional<Rol> rolEncontrado = rolRepository.findByNombre("DIRECTOR");
        assertThat(rolEncontrado).isPresent();
        assertThat(rolEncontrado.get().getId()).isEqualTo(rolGuardado.getId());
    }

    @Test
    @DisplayName("Debería encontrar todos los roles")
    void deberiaEncontrarTodosLosRoles() {
        List<Rol> todosLosRoles = rolRepository.findAll();
        
        assertThat(todosLosRoles).hasSize(3);
        assertThat(todosLosRoles).extracting("nombre")
            .containsExactlyInAnyOrder("ESTUDIANTE", "DOCENTE", "ADMINISTRADOR");
    }

    @Test
    @DisplayName("Debería manejar roles básicos del sistema educativo")
    void deberiaManejarRolesBasicosDelSistemaEducativo() {
        String[] rolesBasicos = {
            "DIRECTOR", "SUBDIRECTOR", "COORDINADOR_ACADEMICO", 
            "JEFE_DE_ESTUDIOS", "SECRETARIO", "INSPECTOR"
        };
        
        // Guardar todos los roles
        for (String nombreRol : rolesBasicos) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar que se pueden encontrar
        for (String nombreRol : rolesBasicos) {
            Optional<Rol> rolEncontrado = rolRepository.findByNombre(nombreRol);
            assertThat(rolEncontrado).isPresent();
            assertThat(rolEncontrado.get().getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles docentes especializados")
    void deberiaManejarRolesDocentesEspecializados() {
        String[] rolesDocentes = {
            "DOCENTE_TITULAR", "DOCENTE_AUXILIAR", "DOCENTE_ESPECIALISTA",
            "TUTOR", "COORDINADOR_DE_AREA", "JEFE_DE_DEPARTAMENTO"
        };
        
        // Guardar roles docentes
        for (String nombreRol : rolesDocentes) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar roles específicos
        Optional<Rol> titular = rolRepository.findByNombre("DOCENTE_TITULAR");
        Optional<Rol> tutor = rolRepository.findByNombre("TUTOR");
        Optional<Rol> coordinador = rolRepository.findByNombre("COORDINADOR_DE_AREA");
        
        assertThat(titular).isPresent();
        assertThat(tutor).isPresent();
        assertThat(coordinador).isPresent();
    }

    @Test
    @DisplayName("Debería manejar roles de apoyo y servicios")
    void deberiaManejarRolesDeApoyoYServicios() {
        String[] rolesApoyo = {
            "PSICOLOGO", "TRABAJADOR_SOCIAL", "ENFERMERO",
            "BIBLIOTECARIO", "CONSERJE", "GUARDIA", "MANTENIMIENTO"
        };
        
        // Guardar roles de apoyo
        for (String nombreRol : rolesApoyo) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar algunos roles específicos
        Optional<Rol> psicologo = rolRepository.findByNombre("PSICOLOGO");
        Optional<Rol> bibliotecario = rolRepository.findByNombre("BIBLIOTECARIO");
        Optional<Rol> trabajadorSocial = rolRepository.findByNombre("TRABAJADOR_SOCIAL");
        
        assertThat(psicologo).isPresent();
        assertThat(bibliotecario).isPresent();
        assertThat(trabajadorSocial).isPresent();
    }

    @Test
    @DisplayName("Debería actualizar nombre de rol correctamente")
    void deberiaActualizarNombreDeRolCorrectamente() {
        // Obtener rol existente
        Optional<Rol> rolExistente = rolRepository.findByNombre("ESTUDIANTE");
        assertThat(rolExistente).isPresent();
        
        Rol rol = rolExistente.get();
        String nombreOriginal = rol.getNombre();
        
        // Actualizar nombre
        rol.setNombre("ESTUDIANTE_REGULAR");
        Rol rolActualizado = rolRepository.save(rol);
        
        // Verificar actualización
        assertThat(rolActualizado.getNombre()).isEqualTo("ESTUDIANTE_REGULAR");
        assertThat(rolActualizado.getNombre()).isNotEqualTo(nombreOriginal);
        
        // Verificar que el nombre original ya no existe
        Optional<Rol> nombreAnterior = rolRepository.findByNombre("ESTUDIANTE");
        assertThat(nombreAnterior).isEmpty();
        
        // Verificar que el nuevo nombre existe
        Optional<Rol> nombreNuevo = rolRepository.findByNombre("ESTUDIANTE_REGULAR");
        assertThat(nombreNuevo).isPresent();
    }

    @Test
    @DisplayName("Debería eliminar rol correctamente")
    void deberiaEliminarRolCorrectamente() {
        Integer rolId = rolAdministrador.getId();
        String nombreRol = rolAdministrador.getNombre();
        
        // Verificar que existe
        assertThat(rolRepository.findById(rolId)).isPresent();
        assertThat(rolRepository.findByNombre(nombreRol)).isPresent();
        
        // Eliminar
        rolRepository.deleteById(rolId);
        
        // Verificar que se eliminó
        assertThat(rolRepository.findById(rolId)).isEmpty();
        assertThat(rolRepository.findByNombre(nombreRol)).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar roles jerárquicos")
    void deberiaManejarRolesJerarquicos() {
        String[] rolesJerarquicos = {
            "RECTOR", "VICERRECTOR", "DIRECTOR", "SUBDIRECTOR",
            "COORDINADOR_ACADEMICO", "JEFE_DE_ESTUDIOS", "INSPECTOR_GENERAL"
        };
        
        // Guardar roles jerárquicos
        for (String nombreRol : rolesJerarquicos) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar jerarquía alta
        Optional<Rol> rector = rolRepository.findByNombre("RECTOR");
        Optional<Rol> vicerrector = rolRepository.findByNombre("VICERRECTOR");
        
        assertThat(rector).isPresent();
        assertThat(vicerrector).isPresent();
        
        // Verificar jerarquía media
        Optional<Rol> coordinador = rolRepository.findByNombre("COORDINADOR_ACADEMICO");
        Optional<Rol> inspector = rolRepository.findByNombre("INSPECTOR_GENERAL");
        
        assertThat(coordinador).isPresent();
        assertThat(inspector).isPresent();
    }

    @Test
    @DisplayName("Debería manejar roles temporales y especiales")
    void deberiaManejarRolesTemporalesYEspeciales() {
        String[] rolesEspeciales = {
            "INVITADO", "PRACTICANTE", "VOLUNTARIO", "TEMPORAL",
            "SUPLENTE", "INTERINO", "ENCARGADO"
        };
        
        // Guardar roles especiales
        for (String nombreRol : rolesEspeciales) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar algunos roles especiales
        Optional<Rol> invitado = rolRepository.findByNombre("INVITADO");
        Optional<Rol> practicante = rolRepository.findByNombre("PRACTICANTE");
        Optional<Rol> suplente = rolRepository.findByNombre("SUPLENTE");
        
        assertThat(invitado).isPresent();
        assertThat(practicante).isPresent();
        assertThat(suplente).isPresent();
    }

    @Test
    @DisplayName("Debería manejar roles con nombres compuestos")
    void deberiaManejarRolesConNombresCompuestos() {
        String[] rolesCompuestos = {
            "JEFE_DE_ESTUDIOS", "COORDINADOR_DE_AREA", "TRABAJADOR_SOCIAL",
            "DOCENTE_CONTRATADO", "ESTUDIANTE_BECARIO", "INSPECTOR_GENERAL"
        };
        
        // Guardar roles compuestos
        for (String nombreRol : rolesCompuestos) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar que se pueden encontrar con guiones bajos
        for (String nombreRol : rolesCompuestos) {
            Optional<Rol> rolEncontrado = rolRepository.findByNombre(nombreRol);
            assertThat(rolEncontrado).isPresent();
            assertThat(rolEncontrado.get().getNombre()).contains("_");
        }
    }

    @Test
    @DisplayName("Debería contar roles correctamente")
    void deberiaContarRolesCorrectamente() {
        long totalRoles = rolRepository.count();
        assertThat(totalRoles).isEqualTo(3); // 3 roles del setUp
        
        // Agregar un rol más
        Rol rolSecretario = new Rol();
        rolSecretario.setNombre("SECRETARIO");
        rolRepository.save(rolSecretario);
        
        // Verificar nuevo conteo
        long nuevoTotal = rolRepository.count();
        assertThat(nuevoTotal).isEqualTo(4);
    }

    @Test
    @DisplayName("Debería existir por ID correctamente")
    void deberiaExistirPorIdCorrectamente() {
        // Verificar que existen los roles del setUp
        assertThat(rolRepository.existsById(rolEstudiante.getId())).isTrue();
        assertThat(rolRepository.existsById(rolDocente.getId())).isTrue();
        assertThat(rolRepository.existsById(rolAdministrador.getId())).isTrue();
        
        // Verificar que no existe ID inexistente
        assertThat(rolRepository.existsById(999)).isFalse();
    }

    @Test
    @DisplayName("Debería encontrar rol por ID correctamente")
    void deberiaEncontrarRolPorIdCorrectamente() {
        Optional<Rol> rolEncontrado = rolRepository.findById(rolDocente.getId());
        
        assertThat(rolEncontrado).isPresent();
        assertThat(rolEncontrado.get().getNombre()).isEqualTo("DOCENTE");
        assertThat(rolEncontrado.get().getId()).isEqualTo(rolDocente.getId());
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para ID inexistente")
    void deberiaRetornarOptionalVacioParaIdInexistente() {
        Optional<Rol> rolInexistente = rolRepository.findById(999);
        
        assertThat(rolInexistente).isEmpty();
    }

    @Test
    @DisplayName("Debería manejar roles del sistema educativo ecuatoriano")
    void deberiaManejarRolesDelSistemaEducativoEcuatoriano() {
        // Roles según estructura del Ministerio de Educación
        String[] rolesMinisterio = {
            "RECTOR", "VICERRECTOR", "INSPECTOR_GENERAL", "SUBINSPECTOR",
            "DOCENTE_TITULAR", "DOCENTE_CONTRATADO", "COORDINADOR_DE_AREA", "TUTOR_DE_CURSO"
        };
        
        // Guardar roles del ministerio
        for (String nombreRol : rolesMinisterio) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
        }
        
        // Verificar roles específicos del sistema ecuatoriano
        Optional<Rol> rector = rolRepository.findByNombre("RECTOR");
        Optional<Rol> inspectorGeneral = rolRepository.findByNombre("INSPECTOR_GENERAL");
        Optional<Rol> docenteTitular = rolRepository.findByNombre("DOCENTE_TITULAR");
        
        assertThat(rector).isPresent();
        assertThat(inspectorGeneral).isPresent();
        assertThat(docenteTitular).isPresent();
    }

    @Test
    @DisplayName("Debería manejar roles con diferentes niveles de permisos")
    void deberiaManejarRolesConDiferentesNivelesDePermisos() {
        // Roles con diferentes niveles de acceso
        Rol superAdmin = new Rol();
        superAdmin.setNombre("SUPER_ADMIN");
        rolRepository.save(superAdmin);

        Rol adminAcademico = new Rol();
        adminAcademico.setNombre("ADMIN_ACADEMICO");
        rolRepository.save(adminAcademico);

        Rol adminFinanciero = new Rol();
        adminFinanciero.setNombre("ADMIN_FINANCIERO");
        rolRepository.save(adminFinanciero);

        Rol consultor = new Rol();
        consultor.setNombre("CONSULTOR");
        rolRepository.save(consultor);

        // Verificar que todos los roles se guardaron
        assertThat(rolRepository.findByNombre("SUPER_ADMIN")).isPresent();
        assertThat(rolRepository.findByNombre("ADMIN_ACADEMICO")).isPresent();
        assertThat(rolRepository.findByNombre("ADMIN_FINANCIERO")).isPresent();
        assertThat(rolRepository.findByNombre("CONSULTOR")).isPresent();

        // Verificar total de roles
        long totalRoles = rolRepository.count();
        assertThat(totalRoles).isEqualTo(7); // 3 del setUp + 4 nuevos
    }
}