import '../constants/api_config.dart';
import 'base_service.dart';

class AnioAcademicoService extends BaseService {
  // Singleton pattern
  static final AnioAcademicoService _instance = AnioAcademicoService._internal();
  factory AnioAcademicoService() => _instance;
  AnioAcademicoService._internal();

  /// Obtiene todos los años académicos
  Future<List<Map<String, dynamic>>> getAllAniosAcademicos() async {
    try {
      final response = await get(ApiConfig.aniosAcademicosEndpoint);
      return processListResponse(response, 'Error al obtener años académicos');
    } catch (e) {
      throw Exception('Error al cargar años académicos: ${e.toString()}');
    }
  }

  /// Obtiene un año académico por ID
  Future<Map<String, dynamic>> getAnioAcademicoById(int anioId) async {
    try {
      final response = await get('${ApiConfig.aniosAcademicosEndpoint}/$anioId');
      return processResponse(response, 'Error al obtener año académico');
    } catch (e) {
      throw Exception('Error al cargar año académico: ${e.toString()}');
    }
  }

  /// Obtiene el año académico activo
  Future<Map<String, dynamic>> getAnioAcademicoActivo() async {
    try {
      final response = await get('${ApiConfig.aniosAcademicosEndpoint}/activo');
      return processResponse(response, 'Error al obtener año académico activo');
    } catch (e) {
      throw Exception('Error al cargar año académico activo: ${e.toString()}');
    }
  }

  /// Crea un nuevo año académico
  Future<Map<String, dynamic>> createAnioAcademico(Map<String, dynamic> anioData) async {
    try {
      final response = await post(ApiConfig.aniosAcademicosEndpoint, anioData);
      return processResponse(response, 'Error al crear año académico');
    } catch (e) {
      throw Exception('Error al crear año académico: ${e.toString()}');
    }
  }

  /// Actualiza un año académico existente
  Future<Map<String, dynamic>> updateAnioAcademico(int anioId, Map<String, dynamic> anioData) async {
    try {
      final response = await put('${ApiConfig.aniosAcademicosEndpoint}/$anioId', anioData);
      return processResponse(response, 'Error al actualizar año académico');
    } catch (e) {
      throw Exception('Error al actualizar año académico: ${e.toString()}');
    }
  }

  /// Elimina un año académico
  Future<bool> deleteAnioAcademico(int anioId) async {
    try {
      final response = await delete('${ApiConfig.aniosAcademicosEndpoint}/$anioId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar año académico: ${e.toString()}');
    }
  }

  /// Activa un año académico (desactiva los demás)
  Future<bool> activarAnioAcademico(int anioId) async {
    try {
      final response = await post('${ApiConfig.aniosAcademicosEndpoint}/$anioId/activar', {});
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al activar año académico: ${e.toString()}');
    }
  }

  /// Cierra un año académico
  Future<bool> cerrarAnioAcademico(int anioId) async {
    try {
      final response = await post('${ApiConfig.aniosAcademicosEndpoint}/$anioId/cerrar', {});
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al cerrar año académico: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas de un año académico
  Future<Map<String, dynamic>> getEstadisticasAnioAcademico(int anioId) async {
    try {
      final response = await get('${ApiConfig.aniosAcademicosEndpoint}/$anioId/estadisticas');
      return processResponse(response, 'Error al obtener estadísticas del año académico');
    } catch (e) {
      throw Exception('Error al cargar estadísticas: ${e.toString()}');
    }
  }

  /// Copia la estructura de cursos y materias de un año a otro
  Future<bool> copiarEstructuraAnio(int anioOrigenId, int anioDestinoId) async {
    try {
      final response = await post('${ApiConfig.aniosAcademicosEndpoint}/copiar-estructura', {
        'anioOrigenId': anioOrigenId,
        'anioDestinoId': anioDestinoId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al copiar estructura: ${e.toString()}');
    }
  }

  /// Obtiene el resumen de actividades de un año académico
  Future<Map<String, dynamic>> getResumenActividades(int anioId) async {
    try {
      final response = await get('${ApiConfig.aniosAcademicosEndpoint}/$anioId/resumen');
      return processResponse(response, 'Error al obtener resumen de actividades');
    } catch (e) {
      throw Exception('Error al cargar resumen: ${e.toString()}');
    }
  }

  /// Verifica si se puede eliminar un año académico
  Future<Map<String, dynamic>> verificarEliminacion(int anioId) async {
    try {
      final response = await get('${ApiConfig.aniosAcademicosEndpoint}/$anioId/verificar-eliminacion');
      return processResponse(response, 'Error al verificar eliminación');
    } catch (e) {
      throw Exception('Error al verificar eliminación: ${e.toString()}');
    }
  }
}
