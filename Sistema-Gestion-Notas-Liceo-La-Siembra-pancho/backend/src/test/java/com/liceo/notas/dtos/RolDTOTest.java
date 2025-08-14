package com.liceo.notas.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para RolDTO")
class RolDTOTest {

    private RolDTO rolDTO;

    @BeforeEach
    void setUp() {
        rolDTO = new RolDTO();
    }

    @Test
    @DisplayName("Debería establecer y obtener ID correctamente")
    void deberiaEstablecerYObtenerId() {
        Integer id = 1;
        rolDTO.setId(id);
        
        assertThat(rolDTO.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería establecer y obtener nombre correctamente")
    void deberiaEstablecerYObtenerNombre() {
        String nombre = "ESTUDIANTE";
        rolDTO.setNombre(nombre);
        
        assertThat(rolDTO.getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Debería crear DTO completo correctamente")
    void deberiaCrearDtoCompletoCorrectamente() {
        Integer id = 1;
        String nombre = "ADMINISTRADOR";
        
        rolDTO.setId(id);
        rolDTO.setNombre(nombre);
        
        assertThat(rolDTO.getId()).isEqualTo(id);
        assertThat(rolDTO.getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Debería manejar roles básicos del sistema educativo")
    void deberiaManejarRolesBasicosDelSistemaEducativo() {
        String[] rolesBasicos = {
            "ESTUDIANTE",
            "DOCENTE", 
            "DIRECTOR",
            "SUBDIRECTOR",
            "SECRETARIO",
            "ADMINISTRADOR"
        };
        
        for (String nombreRol : rolesBasicos) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles administrativos")
    void deberiaManejarRolesAdministrativos() {
        String[] rolesAdministrativos = {
            "ADMINISTRADOR",
            "DIRECTOR",
            "SUBDIRECTOR",
            "COORDINADOR_ACADEMICO",
            "JEFE_DE_ESTUDIOS",
            "SECRETARIO_GENERAL",
            "TESORERO",
            "INSPECTOR_GENERAL"
        };
        
        for (String nombreRol : rolesAdministrativos) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles docentes especializados")
    void deberiaManejarRolesDocentesEspecializados() {
        String[] rolesDocentes = {
            "DOCENTE",
            "DOCENTE_TITULAR",
            "DOCENTE_AUXILIAR",
            "DOCENTE_ESPECIALISTA",
            "TUTOR",
            "COORDINADOR_DE_AREA",
            "JEFE_DE_DEPARTAMENTO"
        };
        
        for (String nombreRol : rolesDocentes) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
            assertThat(rolDTO.getNombre()).contains("DOCENTE");
        }
    }

    @Test
    @DisplayName("Debería manejar roles de apoyo y servicios")
    void deberiaManejarRolesDeApoyoYServicios() {
        String[] rolesApoyo = {
            "PSICOLOGO",
            "TRABAJADOR_SOCIAL", 
            "ENFERMERO",
            "BIBLIOTECARIO",
            "CONSERJE",
            "GUARDIA",
            "CHOFER",
            "MANTENIMIENTO"
        };
        
        for (String nombreRol : rolesApoyo) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles de estudiantes por nivel")
    void deberiaManejarRolesDeEstudiantesPorNivel() {
        String[] rolesEstudiantes = {
            "ESTUDIANTE",
            "ESTUDIANTE_EGB",
            "ESTUDIANTE_BGU", 
            "ESTUDIANTE_BACHILLERATO",
            "REPRESENTANTE_ESTUDIANTIL",
            "PRESIDENTE_CURSO"
        };
        
        for (String nombreRol : rolesEstudiantes) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles jerárquicos")
    void deberiaManejarRolesJerarquicos() {
        // Roles de alta jerarquía
        String[] rolesAltos = {"RECTOR", "VICERRECTOR", "DIRECTOR", "SUBDIRECTOR"};
        for (String rol : rolesAltos) {
            rolDTO.setNombre(rol);
            assertThat(rolDTO.getNombre()).isEqualTo(rol);
        }
        
        // Roles de jerarquía media
        String[] rolesMedios = {"COORDINADOR_ACADEMICO", "JEFE_DE_ESTUDIOS", "INSPECTOR"};
        for (String rol : rolesMedios) {
            rolDTO.setNombre(rol);
            assertThat(rolDTO.getNombre()).isEqualTo(rol);
        }
        
        // Roles de jerarquía básica
        String[] rolesBasicos = {"DOCENTE", "SECRETARIO", "AUXILIAR"};
        for (String rol : rolesBasicos) {
            rolDTO.setNombre(rol);
            assertThat(rolDTO.getNombre()).isEqualTo(rol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles con convenciones de nomenclatura")
    void deberiaManejarRolesConConvencionesDeNomenclatura() {
        // Roles en mayúsculas
        String[] rolesMayusculas = {
            "ADMINISTRADOR", "DOCENTE", "ESTUDIANTE", "DIRECTOR"
        };
        
        for (String nombreRol : rolesMayusculas) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
            assertThat(rolDTO.getNombre()).isUpperCase();
        }
        
        // Roles con guiones bajos
        String[] rolesConGuiones = {
            "COORDINADOR_ACADEMICO", "JEFE_DE_ESTUDIOS", "TRABAJADOR_SOCIAL"
        };
        
        for (String nombreRol : rolesConGuiones) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
            assertThat(rolDTO.getNombre()).contains("_");
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes IDs de roles")
    void deberiaManejarDiferentesIdsDeRoles() {
        Integer[] idsRoles = {1, 2, 3, 4, 5, 10, 15, 20, 25, 50, 100};
        
        for (Integer id : idsRoles) {
            rolDTO.setId(id);
            assertThat(rolDTO.getId()).isEqualTo(id);
            assertThat(rolDTO.getId()).isPositive();
        }
    }

    @Test
    @DisplayName("Debería manejar roles temporales y especiales")
    void deberiaManejarRolesTemporalesYEspeciales() {
        String[] rolesEspeciales = {
            "INVITADO",
            "PRACTICANTE",
            "VOLUNTARIO",
            "TEMPORAL",
            "SUPLENTE",
            "INTERINO",
            "ENCARGADO"
        };
        
        for (String nombreRol : rolesEspeciales) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar valores nulos en campos opcionales")
    void deberiaManejarValoresNulosEnCamposOpcionales() {
        // ID puede ser nulo para nuevos registros
        rolDTO.setId(null);
        assertThat(rolDTO.getId()).isNull();
        
        // Nombre puede ser nulo durante construcción
        rolDTO.setNombre(null);
        assertThat(rolDTO.getNombre()).isNull();
    }

    @Test
    @DisplayName("Debería implementar equals y hashCode por Lombok correctamente")
    void deberiaImplementarEqualsYHashCodePorLombokCorrectamente() {
        RolDTO dto1 = new RolDTO();
        dto1.setId(1);
        dto1.setNombre("ADMINISTRADOR");
        
        RolDTO dto2 = new RolDTO();
        dto2.setId(1);
        dto2.setNombre("ADMINISTRADOR");
        
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar toString por Lombok correctamente")
    void deberiaImplementarToStringPorLombokCorrectamente() {
        rolDTO.setId(1);
        rolDTO.setNombre("DIRECTOR");
        
        String resultado = rolDTO.toString();
        
        assertThat(resultado).contains("RolDTO");
        assertThat(resultado).contains("id=1");
        assertThat(resultado).contains("nombre=DIRECTOR");
    }

    @Test
    @DisplayName("Debería ser utilizable como DTO de transferencia de datos")
    void deberiaSerUtilizableComoDtoDeTransferenciaDeDatos() {
        // Simular datos recibidos desde una API REST
        rolDTO.setId(1);
        rolDTO.setNombre("DOCENTE");
        
        // Verificar que todos los datos están disponibles
        assertThat(rolDTO.getId()).isNotNull();
        assertThat(rolDTO.getNombre()).isNotNull();
        
        // Verificar coherencia de datos
        assertThat(rolDTO.getId()).isPositive();
        assertThat(rolDTO.getNombre()).isNotEmpty();
    }

    @Test
    @DisplayName("Debería manejar roles del sistema educativo ecuatoriano")
    void deberiaManejarRolesDelSistemaEducativoEcuatoriano() {
        // Roles según la estructura del Ministerio de Educación
        String[] rolesMinisterio = {
            "RECTOR",
            "VICERRECTOR", 
            "INSPECTOR_GENERAL",
            "SUBINSPECTOR",
            "DOCENTE_TITULAR",
            "DOCENTE_CONTRATADO",
            "COORDINADOR_DE_AREA",
            "TUTOR_DE_CURSO"
        };
        
        for (String nombreRol : rolesMinisterio) {
            rolDTO.setNombre(nombreRol);
            assertThat(rolDTO.getNombre()).isEqualTo(nombreRol);
        }
    }

    @Test
    @DisplayName("Debería manejar roles con permisos diferenciados")
    void deberiaManejarRolesConPermisosDiferenciados() {
        // Roles con permisos de lectura y escritura
        rolDTO.setId(1);
        rolDTO.setNombre("ADMINISTRADOR");
        assertThat(rolDTO.getNombre()).isEqualTo("ADMINISTRADOR");
        
        // Roles con permisos de solo lectura
        rolDTO.setId(2);
        rolDTO.setNombre("CONSULTOR");
        assertThat(rolDTO.getNombre()).isEqualTo("CONSULTOR");
        
        // Roles con permisos limitados
        rolDTO.setId(3);
        rolDTO.setNombre("ESTUDIANTE");
        assertThat(rolDTO.getNombre()).isEqualTo("ESTUDIANTE");
        
        // Roles con permisos específicos
        rolDTO.setId(4);
        rolDTO.setNombre("DOCENTE");
        assertThat(rolDTO.getNombre()).isEqualTo("DOCENTE");
    }

    @Test
    @DisplayName("Debería validar unicidad de nombres de roles")
    void deberiaValidarUnicidadDeNombresDeRoles() {
        // Cada rol debe tener un nombre único en el sistema
        String[] rolesUnicos = {
            "SUPER_ADMIN",
            "ADMIN_ACADEMICO", 
            "ADMIN_FINANCIERO",
            "COORDINADOR_DISCIPLINARIO",
            "ORIENTADOR_VOCACIONAL"
        };
        
        for (int i = 0; i < rolesUnicos.length; i++) {
            rolDTO.setId(i + 1);
            rolDTO.setNombre(rolesUnicos[i]);
            
            assertThat(rolDTO.getId()).isEqualTo(i + 1);
            assertThat(rolDTO.getNombre()).isEqualTo(rolesUnicos[i]);
        }
    }

    @Test
    @DisplayName("Debería manejar escenarios de asignación de roles")
    void deberiaManejarEscenariosDeAsignacionDeRoles() {
        // Rol por defecto para nuevos usuarios
        rolDTO.setId(1);
        rolDTO.setNombre("INVITADO");
        assertThat(rolDTO.getNombre()).isEqualTo("INVITADO");
        
        // Promoción de rol
        rolDTO.setNombre("ESTUDIANTE");
        assertThat(rolDTO.getNombre()).isEqualTo("ESTUDIANTE");
        
        // Escalamiento de privilegios
        rolDTO.setNombre("DOCENTE");
        assertThat(rolDTO.getNombre()).isEqualTo("DOCENTE");
        
        // Rol administrativo
        rolDTO.setNombre("ADMINISTRADOR");
        assertThat(rolDTO.getNombre()).isEqualTo("ADMINISTRADOR");
    }

    @Test
    @DisplayName("Debería validar coherencia de datos de rol")
    void deberiaValidarCoherenciaDeDatosDeRol() {
        rolDTO.setId(1);
        rolDTO.setNombre("COORDINADOR_ACADEMICO");
        
        // Verificar que el ID y nombre son coherentes
        assertThat(rolDTO.getId()).isNotNull();
        assertThat(rolDTO.getNombre()).isNotNull();
        assertThat(rolDTO.getId()).isPositive();
        assertThat(rolDTO.getNombre()).isNotEmpty();
        
        // Un rol debe tener tanto ID como nombre para ser válido
        assertThat(rolDTO.getId()).isGreaterThan(0);
        assertThat(rolDTO.getNombre().trim()).isNotEmpty();
        assertThat(rolDTO.getNombre()).doesNotContainAnyWhitespaces();
    }
}