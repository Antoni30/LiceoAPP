package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para clase UsuarioCursoId")
class UsuarioCursoIdTest {

    private UsuarioCursoId usuarioCursoId;

    @BeforeEach
    void setUp() {
        usuarioCursoId = new UsuarioCursoId();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor por defecto")
    void deberiaCrearInstanciaConConstructorPorDefecto() {
        UsuarioCursoId id = new UsuarioCursoId();
        
        assertThat(id).isNotNull();
        assertThat(id.getUsuario()).isNull();
        assertThat(id.getCurso()).isNull();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor con parámetros")
    void deberiaCrearInstanciaConConstructorConParametros() {
        String usuarioId = "0504110438";
        Integer cursoId = 1;
        
        UsuarioCursoId id = new UsuarioCursoId(usuarioId, cursoId);
        
        assertThat(id).isNotNull();
        assertThat(id.getUsuario()).isEqualTo(usuarioId);
        assertThat(id.getCurso()).isEqualTo(cursoId);
    }

    @Test
    @DisplayName("Debería establecer y obtener usuario correctamente")
    void deberiaEstablecerYObtenerUsuario() {
        String usuarioId = "0504110438";
        usuarioCursoId.setUsuario(usuarioId);
        
        assertThat(usuarioCursoId.getUsuario()).isEqualTo(usuarioId);
    }

    @Test
    @DisplayName("Debería establecer y obtener curso correctamente")
    void deberiaEstablecerYObtenerCurso() {
        Integer cursoId = 1;
        usuarioCursoId.setCurso(cursoId);
        
        assertThat(usuarioCursoId.getCurso()).isEqualTo(cursoId);
    }

    @Test
    @DisplayName("Debería establecer ambos IDs correctamente")
    void deberiaEstablecerAmbosIdsCorrectamente() {
        String usuarioId = "0504110438";
        Integer cursoId = 1;
        
        usuarioCursoId.setUsuario(usuarioId);
        usuarioCursoId.setCurso(cursoId);
        
        assertThat(usuarioCursoId.getUsuario()).isEqualTo(usuarioId);
        assertThat(usuarioCursoId.getCurso()).isEqualTo(cursoId);
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos iguales")
    void deberiaImplementarEqualsCorrectamenteParaObjetosIguales() {
        UsuarioCursoId id1 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id1)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos diferentes")
    void deberiaImplementarEqualsCorrectamenteParaObjetosDiferentes() {
        UsuarioCursoId id1 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", 2);
        UsuarioCursoId id3 = new UsuarioCursoId("1751983014", 1);
        
        assertThat(id1.equals(id2)).isFalse();
        assertThat(id1.equals(id3)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para mismo objeto")
    void deberiaImplementarEqualsCorrectamenteParaMismoObjeto() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id.equals(id)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objeto null")
    void deberiaImplementarEqualsCorrectamenteParaObjetoNull() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para diferente tipo de objeto")
    void deberiaImplementarEqualsCorrectamenteParaDiferenteTipoDeObjeto() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        String otroObjeto = "no es UsuarioCursoId";
        
        assertThat(id.equals(otroObjeto)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar hashCode correctamente para objetos iguales")
    void deberiaImplementarHashCodeCorrectamenteParaObjetosIguales() {
        UsuarioCursoId id1 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar hashCode de forma consistente")
    void deberiaImplementarHashCodeDeFormaConsistente() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();
        
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en equals")
    void deberiaManejarIdsNulosEnEquals() {
        UsuarioCursoId id1 = new UsuarioCursoId(null, null);
        UsuarioCursoId id2 = new UsuarioCursoId(null, null);
        UsuarioCursoId id3 = new UsuarioCursoId("0504110438", null);
        UsuarioCursoId id4 = new UsuarioCursoId(null, 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id1.equals(id3)).isFalse();
        assertThat(id1.equals(id4)).isFalse();
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en hashCode")
    void deberiaManejarIdsNulosEnHashCode() {
        UsuarioCursoId id1 = new UsuarioCursoId(null, null);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", null);
        UsuarioCursoId id3 = new UsuarioCursoId(null, 1);
        
        // No debería lanzar excepción
        assertThat(id1.hashCode()).isNotNull();
        assertThat(id2.hashCode()).isNotNull();
        assertThat(id3.hashCode()).isNotNull();
    }

    @Test
    @DisplayName("Debería implementar toString correctamente")
    void deberiaImplementarToStringCorrectamente() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        String resultado = id.toString();
        
        assertThat(resultado).contains("UsuarioCursoId");
        assertThat(resultado).contains("usuario='0504110438'");
        assertThat(resultado).contains("curso=1");
    }

    @Test
    @DisplayName("Debería implementar toString correctamente con valores nulos")
    void deberiaImplementarToStringCorrectamenteConValoresNulos() {
        UsuarioCursoId id = new UsuarioCursoId(null, null);
        String resultado = id.toString();
        
        assertThat(resultado).contains("UsuarioCursoId");
        assertThat(resultado).contains("usuario='null'");
        assertThat(resultado).contains("curso=null");
    }

    @Test
    @DisplayName("Debería manejar cédulas ecuatorianas válidas")
    void deberiaManejarCedulasEcuatorianasValidas() {
        String[] cedulasValidas = {
            "0504110438", "1751983014", "1722070560", "1756916233", "1754392635"
        };
        
        for (int i = 0; i < cedulasValidas.length; i++) {
            UsuarioCursoId id = new UsuarioCursoId(cedulasValidas[i], i + 1);
            
            assertThat(id.getUsuario()).isEqualTo(cedulasValidas[i]);
            assertThat(id.getCurso()).isEqualTo(i + 1);
            assertThat(id.toString()).contains("usuario='" + cedulasValidas[i] + "'");
        }
    }

    @Test
    @DisplayName("Debería manejar diferentes IDs de curso")
    void deberiaManejarDiferentesIdsDeCurso() {
        String usuarioId = "0504110438";
        Integer[] cursosIds = {1, 2, 3, 4, 5}; // 1° a 5° bachillerato
        
        for (Integer cursoId : cursosIds) {
            UsuarioCursoId id = new UsuarioCursoId(usuarioId, cursoId);
            
            assertThat(id.getUsuario()).isEqualTo(usuarioId);
            assertThat(id.getCurso()).isEqualTo(cursoId);
        }
    }

    @Test
    @DisplayName("Debería validar que equals es simétrico")
    void deberiaValidarQueEqualsEsSimetrico() {
        UsuarioCursoId id1 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isEqualTo(id2.equals(id1));
    }

    @Test
    @DisplayName("Debería validar que equals es transitivo")
    void deberiaValidarQueEqualsEsTransitivo() {
        UsuarioCursoId id1 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id2 = new UsuarioCursoId("0504110438", 1);
        UsuarioCursoId id3 = new UsuarioCursoId("0504110438", 1);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id3)).isTrue();
        assertThat(id1.equals(id3)).isTrue();
    }

    @Test
    @DisplayName("Debería ser serializable implícitamente")
    void deberiaSerSerializableImplicitamente() {
        UsuarioCursoId id = new UsuarioCursoId("0504110438", 1);
        
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
            UsuarioCursoId id = new UsuarioCursoId(cedulasPorProvincia[i], 1);
            
            assertThat(id.getUsuario()).isEqualTo(cedulasPorProvincia[i]);
            assertThat(id.getCurso()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("Debería manejar múltiples estudiantes en múltiples cursos")
    void deberiaManejarMultiplesEstudiantesEnMultiplesCursos() {
        String[] usuarios = {"0504110438", "1751983014", "1722070560"};
        Integer[] cursos = {1, 2, 3};
        
        for (String usuario : usuarios) {
            for (Integer curso : cursos) {
                UsuarioCursoId id = new UsuarioCursoId(usuario, curso);
                
                assertThat(id.getUsuario()).isEqualTo(usuario);
                assertThat(id.getCurso()).isEqualTo(curso);
                
                // Verificar que cada combinación es única
                UsuarioCursoId otraCombi = new UsuarioCursoId(usuario, curso);
                assertThat(id.equals(otraCombi)).isTrue();
            }
        }
    }

    @Test
    @DisplayName("Debería manejar IDs de rango amplio para cursos")
    void deberiaManejarIdsDeRangoAmplioParaCursos() {
        String usuarioId = "0504110438";
        
        // Valores límite
        UsuarioCursoId idMax = new UsuarioCursoId(usuarioId, Integer.MAX_VALUE);
        UsuarioCursoId idMin = new UsuarioCursoId(usuarioId, 1);
        UsuarioCursoId idCero = new UsuarioCursoId(usuarioId, 0);
        
        assertThat(idMax.getUsuario()).isEqualTo(usuarioId);
        assertThat(idMax.getCurso()).isEqualTo(Integer.MAX_VALUE);
        
        assertThat(idMin.getUsuario()).isEqualTo(usuarioId);
        assertThat(idMin.getCurso()).isEqualTo(1);
        
        assertThat(idCero.getUsuario()).isEqualTo(usuarioId);
        assertThat(idCero.getCurso()).isEqualTo(0);
    }
}