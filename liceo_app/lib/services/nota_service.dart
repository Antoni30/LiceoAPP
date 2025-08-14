import '../constants/api_config.dart';
import 'base_service.dart';

class NotaService extends BaseService {
  // Singleton pattern
  static final NotaService _instance = NotaService._internal();
  factory NotaService() => _instance;
  NotaService._internal();

  /// Obtiene todas las notas de un estudiante
  Future<List<Map<String, dynamic>>> getNotasByEstudiante(String estudianteId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/estudiante/$estudianteId');
      return processListResponse(response, 'Error al obtener notas del estudiante');
    } catch (e) {
      throw Exception('Error al cargar notas del estudiante: ${e.toString()}');
    }
  }

  /// Obtiene todas las notas de una materia
  Future<List<Map<String, dynamic>>> getNotasByMateria(int materiaId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/materia/$materiaId');
      return processListResponse(response, 'Error al obtener notas de la materia');
    } catch (e) {
      throw Exception('Error al cargar notas de la materia: ${e.toString()}');
    }
  }

  /// Obtiene las notas de un curso específico
  Future<List<Map<String, dynamic>>> getNotasByCurso(int cursoId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/curso/$cursoId');
      return processListResponse(response, 'Error al obtener notas del curso');
    } catch (e) {
      throw Exception('Error al cargar notas del curso: ${e.toString()}');
    }
  }

  /// Obtiene las notas de un profesor específico
  Future<List<Map<String, dynamic>>> getNotasByProfesor(String profesorId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/profesor/$profesorId');
      return processListResponse(response, 'Error al obtener notas del profesor');
    } catch (e) {
      throw Exception('Error al cargar notas del profesor: ${e.toString()}');
    }
  }

  /// Obtiene las notas de un usuario específico en una materia
  Future<List<Map<String, dynamic>>> getNotasByUsuarioMateria(int userId, int materiaId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/usuario/$userId/materia/$materiaId');
      return processListResponse(response, 'Error al obtener notas del usuario en la materia');
    } catch (e) {
      throw Exception('Error al cargar notas del usuario: ${e.toString()}');
    }
  }

  /// Obtiene una nota específica por ID
  Future<Map<String, dynamic>> getNotaById(int notaId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/$notaId');
      return processResponse(response, 'Error al obtener nota');
    } catch (e) {
      throw Exception('Error al cargar nota: ${e.toString()}');
    }
  }

  /// Crea una nueva nota
  Future<Map<String, dynamic>> createNota(Map<String, dynamic> notaData) async {
    try {
      final response = await post(ApiConfig.notasEndpoint, notaData);
      return processResponse(response, 'Error al crear nota');
    } catch (e) {
      throw Exception('Error al crear nota: ${e.toString()}');
    }
  }

  /// Actualiza una nota existente
  Future<Map<String, dynamic>> updateNota(int notaId, Map<String, dynamic> notaData) async {
    try {
      final response = await put('${ApiConfig.notasEndpoint}/$notaId', notaData);
      return processResponse(response, 'Error al actualizar nota');
    } catch (e) {
      throw Exception('Error al actualizar nota: ${e.toString()}');
    }
  }

  /// Elimina una nota
  Future<bool> deleteNota(int notaId) async {
    try {
      final response = await delete('${ApiConfig.notasEndpoint}/$notaId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar nota: ${e.toString()}');
    }
  }

  /// Crea múltiples notas en una sola operación
  Future<List<Map<String, dynamic>>> createMultipleNotas(List<Map<String, dynamic>> notasData) async {
    try {
      final response = await post('${ApiConfig.notasEndpoint}/multiple', {
        'notas': notasData,
      });
      return processListResponse(response, 'Error al crear notas múltiples');
    } catch (e) {
      throw Exception('Error al crear notas múltiples: ${e.toString()}');
    }
  }

  /// Obtiene el promedio de notas de un estudiante en una materia
  Future<Map<String, dynamic>> getPromedioEstudianteMateria(String estudianteId, int materiaId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/promedio/$estudianteId/$materiaId');
      return processResponse(response, 'Error al obtener promedio');
    } catch (e) {
      throw Exception('Error al calcular promedio: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas de notas de una materia
  Future<Map<String, dynamic>> getEstadisticasMateria(int materiaId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/estadisticas/materia/$materiaId');
      return processResponse(response, 'Error al obtener estadísticas de la materia');
    } catch (e) {
      throw Exception('Error al cargar estadísticas: ${e.toString()}');
    }
  }

  /// Obtiene el reporte de notas de un curso
  Future<Map<String, dynamic>> getReporteNotasCurso(int cursoId) async {
    try {
      final response = await get('${ApiConfig.notasEndpoint}/reporte/curso/$cursoId');
      return processResponse(response, 'Error al generar reporte del curso');
    } catch (e) {
      throw Exception('Error al generar reporte: ${e.toString()}');
    }
  }

  /// Importa notas desde un archivo CSV
  Future<Map<String, dynamic>> importNotasFromCSV(String csvData, int materiaId) async {
    try {
      final response = await post('${ApiConfig.notasEndpoint}/import', {
        'csvData': csvData,
        'materiaId': materiaId,
      });
      return processResponse(response, 'Error al importar notas');
    } catch (e) {
      throw Exception('Error al importar notas: ${e.toString()}');
    }
  }
}
