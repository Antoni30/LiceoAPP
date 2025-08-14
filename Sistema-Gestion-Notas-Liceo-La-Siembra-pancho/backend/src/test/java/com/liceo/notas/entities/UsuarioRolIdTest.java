package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para clase UsuarioRolId")
class UsuarioRolIdTest {

    private UsuarioRolId usuarioRolId;

    @BeforeEach
    void setUp() {
        usuarioRolId = new UsuarioRolId();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor por defecto")
    void deberiaCrearInstanciaConConstructorPorDefecto() {
        UsuarioRolId id = new UsuarioRolId();
        
        assertThat(id).isNotNull();
        assertThat(id.getUsuario()).isNull();
        assertThat(id.getRol()).isNull();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor con parámetros")
    void deberiaCrearInstanciaConConstructorConParametros() {
        String usuarioId = "0504110438";
        Integer rolId = 1;
        
        UsuarioRolId id = new UsuarioRolId(usuarioId, rolId);
        
        assertThat(id).isNotNull();
        assertThat(id.getUsuario()).isEqualTo(usuarioId);
        assertThat(id.getRol()).isEqualTo(rolId);
    }

    @Test
    @DisplayName("Debería establecer y obtener usuario correctamente")
    void deberiaEstablecerYObtenerUsuario() {
        String usuarioId = "0504110438";
        usuarioRolId.setUsuario(usuarioId);
        
        assertThat(usuarioRolId.getUsuario()).isEqualTo(usuarioId);
    }

    @Test
    @DisplayName("Debería establecer y obtener rol correctamente")
    void deberiaEstablecerYObtenerRol() {
        Integer rolId = 1;
        usuarioRolId.setRol(rolId);
        
        assertThat(usuarioRolId.getRol()).isEqualTo(rolId);
    }

    @Test
    @DisplayName("Debería establecer ambos IDs correctamente")
    void deberiaEstablecerAmbosIdsCorrectamente() {
        String usuarioId = "0504110438";
        Integer rolId = 1;
        
        usuarioRolId.setUsuario(usuarioId);
        usuarioRolId.setRol(rolId);
        
        assertThat(usuarioRolId.getUsuario()).isEqualTo(usuarioId);
        assertThat(usuarioRolId.getRol()).isEqualTo(rolId);
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos iguales")
    void deberiaImplementarEqualsCorrectamenteParaObjetosIguales() {
        UsuarioRolId id1 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id1)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos diferentes")
    void deberiaImplementarEqualsCorrectamenteParaObjetosDiferentes() {
        UsuarioRolId id1 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", 2);
        UsuarioRolId id3 = new UsuarioRolId("1751983014", 1);
        
        assertThat(id1.equals(id2)).isFalse();
        assertThat(id1.equals(id3)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para mismo objeto")
    void deberiaImplementarEqualsCorrectamenteParaMismoObjeto() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        
        assertThat(id.equals(id)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objeto null")
    void deberiaImplementarEqualsCorrectamenteParaObjetoNull() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        
        assertThat(id.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para diferente tipo de objeto")
    void deberiaImplementarEqualsCorrectamenteParaDiferenteTipoDeObjeto() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        String otroObjeto = "no es UsuarioRolId";
        
        assertThat(id.equals(otroObjeto)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar hashCode correctamente para objetos iguales")
    void deberiaImplementarHashCodeCorrectamenteParaObjetosIguales() {
        UsuarioRolId id1 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", 1);
        
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar hashCode de forma consistente")
    void deberiaImplementarHashCodeDeFormaConsistente() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();
        
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en equals")
    void deberiaManejarIdsNulosEnEquals() {
        UsuarioRolId id1 = new UsuarioRolId(null, null);
        UsuarioRolId id2 = new UsuarioRolId(null, null);
        UsuarioRolId id3 = new UsuarioRolId("0504110438", null);
        UsuarioRolId id4 = new UsuarioRolId(null, 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id1.equals(id3)).isFalse();
        assertThat(id1.equals(id4)).isFalse();
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en hashCode")
    void deberiaManejarIdsNulosEnHashCode() {
        UsuarioRolId id1 = new UsuarioRolId(null, null);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", null);
        UsuarioRolId id3 = new UsuarioRolId(null, 1);
        
        // No debería lanzar excepción
        assertThat(id1.hashCode()).isNotNull();
        assertThat(id2.hashCode()).isNotNull();
        assertThat(id3.hashCode()).isNotNull();
    }

    @Test
    @DisplayName("Debería implementar toString correctamente")
    void deberiaImplementarToStringCorrectamente() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        String resultado = id.toString();
        
        assertThat(resultado).contains("UsuarioRolId");
        assertThat(resultado).contains("usuario='0504110438'");
        assertThat(resultado).contains("rol=1");
    }

    @Test
    @DisplayName("Debería implementar toString correctamente con valores nulos")
    void deberiaImplementarToStringCorrectamenteConValoresNulos() {
        UsuarioRolId id = new UsuarioRolId(null, null);
        String resultado = id.toString();
        
        assertThat(resultado).contains("UsuarioRolId");
        assertThat(resultado).contains("usuario='null'");
        assertThat(resultado).contains("rol=null");
    }

    @Test
    @DisplayName("Debería manejar cédulas ecuatorianas válidas")
    void deberiaManejarCedulasEcuatorianasValidas() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (int i = 0; i < cedulasValidas.length; i++) {
            UsuarioRolId id = new UsuarioRolId(cedulasValidas[i], i + 1);
            
            assertThat(id.getUsuario()).isEqualTo(cedulasValidas[i]);
            assertThat(id.getRol()).isEqualTo(i + 1);
            assertThat(id.toString()).contains("usuario='" + cedulasValidas[i] + "'");
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes tipos de roles")
    void deberiaManejarDiferentesTiposDeRoles() {
        String usuarioId = "0504110438";
        Integer[] rolesIds = {1, 2, 3, 4, 5}; // ESTUDIANTE, DOCENTE, ADMIN, DIRECTOR, SECRETARIO
        
        for (Integer rolId : rolesIds) {
            UsuarioRolId id = new UsuarioRolId(usuarioId, rolId);
            
            assertThat(id.getUsuario()).isEqualTo(usuarioId);
            assertThat(id.getRol()).isEqualTo(rolId);
        }
    }

    @Test
    @DisplayName("Debería validar que equals es simétrico")
    void deberiaValidarQueEqualsEsSimetrico() {
        UsuarioRolId id1 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isEqualTo(id2.equals(id1));
    }

    @Test
    @DisplayName("Debería validar que equals es transitivo")
    void deberiaValidarQueEqualsEsTransitivo() {
        UsuarioRolId id1 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id2 = new UsuarioRolId("0504110438", 1);
        UsuarioRolId id3 = new UsuarioRolId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id3)).isTrue();
        assertThat(id1.equals(id3)).isTrue();
    }

    @Test
    @DisplayName("Debería ser serializable implícitamente")
    void deberiaSerSerializableImplicitamente() {
        UsuarioRolId id = new UsuarioRolId("0504110438", 1);
        
        // Verificar que implementa Serializable
        assertThat(id).isInstanceOf(java.io.Serializable.class);
    }

    @Test
    @DisplayName("Debería manejar cédulas de diferentes provincias del Ecuador")
    void deberiaManejarCedulasDeDiferentesProvinciasDelEcuador() {
        // Cédulas de diferentes provincias del Ecuador
        String[] cedulasPorProvincia = {
            "0504110438", // Cotopaxi
            "1751983014", // Pichincha  
            "1722070560", // Pichincha
            "1756916233", // Pichincha
            "1754392635"  // Pichincha
        };
        
        for (int i = 0; i < cedulasPorProvincia.length; i++) {
            UsuarioRolId id = new UsuarioRolId(cedulasPorProvincia[i], 1);
            
            assertThat(id.getUsuario()).isEqualTo(cedulasPorProvincia[i]);
            assertThat(id.getRol()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("Debería manejar múltiples usuarios con múltiples roles")
    void deberiaManejarMultiplesUsuariosConMultiplesRoles() {
        String[] usuarios = {"0504110438", "1751983014", "1722070560"};
        Integer[] roles = {1, 2, 3}; // ESTUDIANTE, DOCENTE, ADMIN
        
        for (String usuario : usuarios) {
            for (Integer rol : roles) {
                UsuarioRolId id = new UsuarioRolId(usuario, rol);
                
                assertThat(id.getUsuario()).isEqualTo(usuario);
                assertThat(id.getRol()).isEqualTo(rol);
                
                // Verificar que cada combinación es única
                UsuarioRolId otraCombi = new UsuarioRolId(usuario, rol);
                assertThat(id.equals(otraCombi)).isTrue();
            }
        }
    }

    @Test
    @DisplayName("Debería manejar escenarios típicos del sistema educativo")
    void deberiaManejarEscenariosTipicosDelSistemaEducativo() {
        // Estudiante regular
        UsuarioRolId estudianteId = new UsuarioRolId("1756916233", 1);
        assertThat(estudianteId.getUsuario()).isEqualTo("1756916233");
        assertThat(estudianteId.getRol()).isEqualTo(1);
        
        // Docente
        UsuarioRolId docenteId = new UsuarioRolId("1754392635", 2);
        assertThat(docenteId.getUsuario()).isEqualTo("1754392635");
        assertThat(docenteId.getRol()).isEqualTo(2);
        
        // Administrador
        UsuarioRolId adminId = new UsuarioRolId("0504110438", 3);
        assertThat(adminId.getUsuario()).isEqualTo("0504110438");
        assertThat(adminId.getRol()).isEqualTo(3);
        
        // Director
        UsuarioRolId directorId = new UsuarioRolId("1751983014", 4);
        assertThat(directorId.getUsuario()).isEqualTo("1751983014");
        assertThat(directorId.getRol()).isEqualTo(4);
        
        // Secretario
        UsuarioRolId secretarioId = new UsuarioRolId("1722070560", 5);
        assertThat(secretarioId.getUsuario()).isEqualTo("1722070560");
        assertThat(secretarioId.getRol()).isEqualTo(5);
    }

    @Test
    @DisplayName("Debería manejar IDs de rango amplio para roles")
    void deberiaManejarIdsDeRangoAmplioParaRoles() {
        String usuarioId = "0504110438";
        
        // Valores límite
        UsuarioRolId idMax = new UsuarioRolId(usuarioId, Integer.MAX_VALUE);
        UsuarioRolId idMin = new UsuarioRolId(usuarioId, 1);
        UsuarioRolId idCero = new UsuarioRolId(usuarioId, 0);
        
        assertThat(idMax.getUsuario()).isEqualTo(usuarioId);
        assertThat(idMax.getRol()).isEqualTo(Integer.MAX_VALUE);
        
        assertThat(idMin.getUsuario()).isEqualTo(usuarioId);
        assertThat(idMin.getRol()).isEqualTo(1);
        
        assertThat(idCero.getUsuario()).isEqualTo(usuarioId);
        assertThat(idCero.getRol()).isEqualTo(0);
    }

    @Test
    @DisplayName("Debería manejar casos de usuarios con roles múltiples")
    void deberiaManejarCasosDeUsuariosConRolesMultiples() {
        String usuarioMultiRol = "0504110438";
        
        // Un usuario puede tener múltiples roles (ej: docente que también es admin)
        UsuarioRolId rolDocente = new UsuarioRolId(usuarioMultiRol, 2);
        UsuarioRolId rolAdmin = new UsuarioRolId(usuarioMultiRol, 3);
        
        assertThat(rolDocente.getUsuario()).isEqualTo(usuarioMultiRol);
        assertThat(rolAdmin.getUsuario()).isEqualTo(usuarioMultiRol);
        
        assertThat(rolDocente.getRol()).isEqualTo(2);
        assertThat(rolAdmin.getRol()).isEqualTo(3);
        
        // Son objetos diferentes aunque tengan el mismo usuario
        assertThat(rolDocente.equals(rolAdmin)).isFalse();
    }
}