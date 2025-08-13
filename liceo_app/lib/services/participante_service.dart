import '../constants/api_config.dart';
import 'base_service.dart';

class ParticipanteService extends BaseService {
  // Singleton pattern
  static final ParticipanteService _instance = ParticipanteService._internal();
  factory ParticipanteService() => _instance;
  ParticipanteService._internal();

  /// Obtiene todos los participantes de un curso
  Future<List<Map<String, dynamic>>> getParticipantesByCurso(int cursoId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/curso/$cursoId');
      return processListResponse(response, 'Error al obtener participantes del curso');
    } catch (e) {
      throw Exception('Error al cargar participantes del curso: ${e.toString()}');
    }
  }

  /// Obtiene los cursos en los que participa un estudiante
  Future<List<Map<String, dynamic>>> getCursosByEstudiante(String estudianteId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/estudiante/$estudianteId');
      return processListResponse(response, 'Error al obtener cursos del estudiante');
    } catch (e) {
      throw Exception('Error al cargar cursos del estudiante: ${e.toString()}');
    }
  }

  /// Agrega un participante a un curso
  Future<bool> addParticipanteToCurso(int cursoId, String estudianteId) async {
    try {
      final response = await post(ApiConfig.participantesEndpoint, {
        'idCurso': cursoId,
        'idEstudiante': estudianteId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al agregar participante: ${e.toString()}');
    }
  }

  /// Remueve un participante de un curso
  Future<bool> removeParticipanteFromCurso(int cursoId, String estudianteId) async {
    try {
      final response = await delete('${ApiConfig.participantesEndpoint}/$cursoId/$estudianteId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al remover participante: ${e.toString()}');
    }
  }

  /// Agrega múltiples participantes a un curso
  Future<Map<String, dynamic>> addMultipleParticipantes(int cursoId, List<String> estudiantesIds) async {
    try {
      final response = await post('${ApiConfig.participantesEndpoint}/multiple', {
        'idCurso': cursoId,
        'estudiantes': estudiantesIds,
      });
      return processResponse(response, 'Error al agregar participantes múltiples');
    } catch (e) {
      throw Exception('Error al agregar participantes múltiples: ${e.toString()}');
    }
  }

  /// Transfiere un estudiante de un curso a otro
  Future<bool> transferirEstudiante(String estudianteId, int cursoOrigenId, int cursoDestinoId) async {
    try {
      final response = await post('${ApiConfig.participantesEndpoint}/transferir', {
        'estudianteId': estudianteId,
        'cursoOrigenId': cursoOrigenId,
        'cursoDestinoId': cursoDestinoId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al transferir estudiante: ${e.toString()}');
    }
  }

  /// Verifica si un estudiante está inscrito en un curso
  Future<bool> isEstudianteInCurso(String estudianteId, int cursoId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/verificar/$estudianteId/$cursoId');
      final data = processResponse(response, 'Error al verificar participación');
      return data['participando'] ?? false;
    } catch (e) {
      return false;
    }
  }

  /// Obtiene estadísticas de participación de un curso
  Future<Map<String, dynamic>> getEstadisticasParticipacion(int cursoId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/estadisticas/$cursoId');
      return processResponse(response, 'Error al obtener estadísticas de participación');
    } catch (e) {
      throw Exception('Error al cargar estadísticas: ${e.toString()}');
    }
  }

  /// Busca estudiantes no inscritos en un curso específico
  Future<List<Map<String, dynamic>>> getEstudiantesNoInscritos(int cursoId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/no-inscritos/$cursoId');
      return processListResponse(response, 'Error al obtener estudiantes no inscritos');
    } catch (e) {
      throw Exception('Error al cargar estudiantes no inscritos: ${e.toString()}');
    }
  }

  /// Exporta la lista de participantes de un curso
  Future<Map<String, dynamic>> exportarParticipantes(int cursoId, String formato) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/exportar/$cursoId?formato=$formato');
      return processResponse(response, 'Error al exportar participantes');
    } catch (e) {
      throw Exception('Error al exportar participantes: ${e.toString()}');
    }
  }

  /// Obtiene el historial de participación de un estudiante
  Future<List<Map<String, dynamic>>> getHistorialParticipacion(String estudianteId) async {
    try {
      final response = await get('${ApiConfig.participantesEndpoint}/historial/$estudianteId');
      return processListResponse(response, 'Error al obtener historial de participación');
    } catch (e) {
      throw Exception('Error al cargar historial: ${e.toString()}');
    }
  }
}
