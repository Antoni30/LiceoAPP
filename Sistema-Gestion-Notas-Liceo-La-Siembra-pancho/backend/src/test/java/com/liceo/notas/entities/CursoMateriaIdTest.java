package com.liceo.notas.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests para clase CursoMateriaId")
class CursoMateriaIdTest {

    private CursoMateriaId cursoMateriaId;

    @BeforeEach
    void setUp() {
        cursoMateriaId = new CursoMateriaId();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor por defecto")
    void deberiaCrearInstanciaConConstructorPorDefecto() {
        CursoMateriaId id = new CursoMateriaId();
        
        assertThat(id).isNotNull();
        assertThat(id.getCurso()).isNull();
        assertThat(id.getMateria()).isNull();
    }

    @Test
    @DisplayName("Debería crear instancia con constructor con parámetros")
    void deberiaCrearInstanciaConConstructorConParametros() {
        Integer cursoId = 1;
        Integer materiaId = 2;
        
        CursoMateriaId id = new CursoMateriaId(cursoId, materiaId);
        
        assertThat(id).isNotNull();
        assertThat(id.getCurso()).isEqualTo(cursoId);
        assertThat(id.getMateria()).isEqualTo(materiaId);
    }

    @Test
    @DisplayName("Debería establecer y obtener curso correctamente")
    void deberiaEstablecerYObtenerCurso() {
        Integer cursoId = 1;
        cursoMateriaId.setCurso(cursoId);
        
        assertThat(cursoMateriaId.getCurso()).isEqualTo(cursoId);
    }

    @Test
    @DisplayName("Debería establecer y obtener materia correctamente")
    void deberiaEstablecerYObtenerMateria() {
        Integer materiaId = 2;
        cursoMateriaId.setMateria(materiaId);
        
        assertThat(cursoMateriaId.getMateria()).isEqualTo(materiaId);
    }

    @Test
    @DisplayName("Debería establecer ambos IDs correctamente")
    void deberiaEstablecerAmbosIdsCorrectamente() {
        Integer cursoId = 1;
        Integer materiaId = 2;
        
        cursoMateriaId.setCurso(cursoId);
        cursoMateriaId.setMateria(materiaId);
        
        assertThat(cursoMateriaId.getCurso()).isEqualTo(cursoId);
        assertThat(cursoMateriaId.getMateria()).isEqualTo(materiaId);
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos iguales")
    void deberiaImplementarEqualsCorrectamenteParaObjetosIguales() {
        CursoMateriaId id1 = new CursoMateriaId(1, 2);
        CursoMateriaId id2 = new CursoMateriaId(1, 2);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id1)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objetos diferentes")
    void deberiaImplementarEqualsCorrectamenteParaObjetosDiferentes() {
        CursoMateriaId id1 = new CursoMateriaId(1, 2);
        CursoMateriaId id2 = new CursoMateriaId(1, 3);
        CursoMateriaId id3 = new CursoMateriaId(2, 2);
        
        assertThat(id1.equals(id2)).isFalse();
        assertThat(id1.equals(id3)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para mismo objeto")
    void deberiaImplementarEqualsCorrectamenteParaMismoObjeto() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        
        assertThat(id.equals(id)).isTrue();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para objeto null")
    void deberiaImplementarEqualsCorrectamenteParaObjetoNull() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        
        assertThat(id.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar equals correctamente para diferente tipo de objeto")
    void deberiaImplementarEqualsCorrectamenteParaDiferenteTipoDeObjeto() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        String otroObjeto = "no es CursoMateriaId";
        
        assertThat(id.equals(otroObjeto)).isFalse();
    }

    @Test
    @DisplayName("Debería implementar hashCode correctamente para objetos iguales")
    void deberiaImplementarHashCodeCorrectamenteParaObjetosIguales() {
        CursoMateriaId id1 = new CursoMateriaId(1, 2);
        CursoMateriaId id2 = new CursoMateriaId(1, 2);
        
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("Debería implementar hashCode de forma consistente")
    void deberiaImplementarHashCodeDeFormaConsistente() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();
        
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en equals")
    void deberiaManejarIdsNulosEnEquals() {
        CursoMateriaId id1 = new CursoMateriaId(null, null);
        CursoMateriaId id2 = new CursoMateriaId(null, null);
        CursoMateriaId id3 = new CursoMateriaId(1, null);
        CursoMateriaId id4 = new CursoMateriaId(null, 2);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id1.equals(id3)).isFalse();
        assertThat(id1.equals(id4)).isFalse();
    }

    @Test
    @DisplayName("Debería manejar IDs nulos en hashCode")
    void deberiaManejarIdsNulosEnHashCode() {
        CursoMateriaId id1 = new CursoMateriaId(null, null);
        CursoMateriaId id2 = new CursoMateriaId(1, null);
        CursoMateriaId id3 = new CursoMateriaId(null, 2);
        
        // No debería lanzar excepción
        assertThat(id1.hashCode()).isNotNull();
        assertThat(id2.hashCode()).isNotNull();
        assertThat(id3.hashCode()).isNotNull();
    }

    @Test
    @DisplayName("Debería implementar toString correctamente")
    void deberiaImplementarToStringCorrectamente() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        String resultado = id.toString();
        
        assertThat(resultado).contains("CursoMateriaId");
        assertThat(resultado).contains("curso=1");
        assertThat(resultado).contains("materia=2");
    }

    @Test
    @DisplayName("Debería implementar toString correctamente con valores nulos")
    void deberiaImplementarToStringCorrectamenteConValoresNulos() {
        CursoMateriaId id = new CursoMateriaId(null, null);
        String resultado = id.toString();
        
        assertThat(resultado).contains("CursoMateriaId");
        assertThat(resultado).contains("curso=null");
        assertThat(resultado).contains("materia=null");
    }

    @Test
    @DisplayName("Debería manejar diferentes combinaciones de IDs válidos")
    void deberiaManejarDiferentesCombinacionesDeIdsValidos() {
        // IDs típicos del sistema educativo
        Integer[] cursosIds = {1, 2, 3, 4, 5}; // 1° a 5° bachillerato
        Integer[] materiasIds = {1, 2, 3, 4, 5, 6, 7}; // Diferentes materias
        
        for (Integer cursoId : cursosIds) {
            for (Integer materiaId : materiasIds) {
                CursoMateriaId id = new CursoMateriaId(cursoId, materiaId);
                
                assertThat(id.getCurso()).isEqualTo(cursoId);
                assertThat(id.getMateria()).isEqualTo(materiaId);
                assertThat(id.toString()).contains("curso=" + cursoId);
                assertThat(id.toString()).contains("materia=" + materiaId);
            }
        }
    }

    @Test
    @DisplayName("Debería validar que equals es simétrico")
    void deberiaValidarQueEqualsEsSimetrico() {
        CursoMateriaId id1 = new CursoMateriaId(1, 2);
        CursoMateriaId id2 = new CursoMateriaId(1, 2);
        
        assertThat(id1.equals(id2)).isEqualTo(id2.equals(id1));
    }

    @Test
    @DisplayName("Debería validar que equals es transitivo")
    void deberiaValidarQueEqualsEsTransitivo() {
        CursoMateriaId id1 = new CursoMateriaId(1, 2);
        CursoMateriaId id2 = new CursoMateriaId(1, 2);
        CursoMateriaId id3 = new CursoMateriaId(1, 2);
        
        assertThat(id1.equals(id2)).isTrue();
        assertThat(id2.equals(id3)).isTrue();
        assertThat(id1.equals(id3)).isTrue();
    }

    @Test
    @DisplayName("Debería ser serializable implícitamente")
    void deberiaSerSerializableImplicitamente() {
        CursoMateriaId id = new CursoMateriaId(1, 2);
        
        // Verificar que implementa Serializable
        assertThat(id).isInstanceOf(java.io.Serializable.class);
    }

    @Test
    @DisplayName("Debería manejar IDs de rango amplio")
    void deberiaManejarIdsDeRangoAmplio() {
        // Valores límite
        CursoMateriaId idMax = new CursoMateriaId(Integer.MAX_VALUE, Integer.MAX_VALUE);
        CursoMateriaId idMin = new CursoMateriaId(1, 1);
        CursoMateriaId idCero = new CursoMateriaId(0, 0);
        
        assertThat(idMax.getCurso()).isEqualTo(Integer.MAX_VALUE);
        assertThat(idMax.getMateria()).isEqualTo(Integer.MAX_VALUE);
        
        assertThat(idMin.getCurso()).isEqualTo(1);
        assertThat(idMin.getMateria()).isEqualTo(1);
        
        assertThat(idCero.getCurso()).isEqualTo(0);
        assertThat(idCero.getMateria()).isEqualTo(0);
    }
}