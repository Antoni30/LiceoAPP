import '../constants/api_config.dart';
import 'base_service.dart';

class CursoService extends BaseService {
  // Singleton pattern
  static final CursoService _instance = CursoService._internal();
  factory CursoService() => _instance;
  CursoService._internal();

  /// Obtiene todos los cursos
  Future<List<Map<String, dynamic>>> getAllCursos() async {
    try {
      final response = await get(ApiConfig.cursosEndpoint);
      return processListResponse(response, 'Error al obtener cursos');
    } catch (e) {
      throw Exception('Error al cargar cursos: ${e.toString()}');
    }
  }

  /// Obtiene un curso por ID
  Future<Map<String, dynamic>> getCursoById(int cursoId) async {
    try {
      final response = await get('${ApiConfig.cursosEndpoint}/$cursoId');
      return processResponse(response, 'Error al obtener curso');
    } catch (e) {
      throw Exception('Error al cargar curso: ${e.toString()}');
    }
  }

  /// Obtiene los cursos asignados a un profesor
  Future<List<Map<String, dynamic>>> getCursosByProfesor(String profesorId) async {
    try {
      final response = await get('${ApiConfig.cursosEndpoint}/profesor/$profesorId');
      return processListResponse(response, 'Error al obtener cursos del profesor');
    } catch (e) {
      throw Exception('Error al cargar cursos del profesor: ${e.toString()}');
    }
  }

  /// Obtiene cursos por año académico
  Future<List<Map<String, dynamic>>> getCursosByAnioAcademico(int anioId) async {
    try {
      final response = await get('${ApiConfig.cursosEndpoint}/anio/$anioId');
      return processListResponse(response, 'Error al obtener cursos del año académico');
    } catch (e) {
      throw Exception('Error al cargar cursos del año académico: ${e.toString()}');
    }
  }

  /// Crea un nuevo curso
  Future<Map<String, dynamic>> createCurso(Map<String, dynamic> cursoData) async {
    try {
      final response = await post(ApiConfig.cursosEndpoint, cursoData);
      return processResponse(response, 'Error al crear curso');
    } catch (e) {
      throw Exception('Error al crear curso: ${e.toString()}');
    }
  }

  /// Actualiza un curso existente
  Future<Map<String, dynamic>> updateCurso(int cursoId, Map<String, dynamic> cursoData) async {
    try {
      final response = await put('${ApiConfig.cursosEndpoint}/$cursoId', cursoData);
      return processResponse(response, 'Error al actualizar curso');
    } catch (e) {
      throw Exception('Error al actualizar curso: ${e.toString()}');
    }
  }

  /// Elimina un curso
  Future<bool> deleteCurso(int cursoId) async {
    try {
      final response = await delete('${ApiConfig.cursosEndpoint}/$cursoId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar curso: ${e.toString()}');
    }
  }

  /// Asigna un profesor a un curso
  Future<bool> assignProfesorToCurso(int cursoId, String profesorId) async {
    try {
      final response = await post('${ApiConfig.cursosEndpoint}/$cursoId/profesor', {
        'profesorId': profesorId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al asignar profesor al curso: ${e.toString()}');
    }
  }

  /// Remueve un profesor de un curso
  Future<bool> removeProfesorFromCurso(int cursoId, String profesorId) async {
    try {
      final response = await delete('${ApiConfig.cursosEndpoint}/$cursoId/profesor/$profesorId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al remover profesor del curso: ${e.toString()}');
    }
  }

  /// Busca cursos por nombre
  Future<List<Map<String, dynamic>>> searchCursos(String query) async {
    try {
      final response = await get('${ApiConfig.cursosEndpoint}/buscar?q=$query');
      return processListResponse(response, 'Error al buscar cursos');
    } catch (e) {
      throw Exception('Error al buscar cursos: ${e.toString()}');
    }
  }
}
