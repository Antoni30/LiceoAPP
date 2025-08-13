import '../constants/api_config.dart';
import 'base_service.dart';

class MateriaService extends BaseService {
  // Singleton pattern
  static final MateriaService _instance = MateriaService._internal();
  factory MateriaService() => _instance;
  MateriaService._internal();

  /// Obtiene todas las materias
  Future<List<Map<String, dynamic>>> getAllMaterias() async {
    try {
      final response = await get(ApiConfig.materiasEndpoint);
      return processListResponse(response, 'Error al obtener materias');
    } catch (e) {
      throw Exception('Error al cargar materias: ${e.toString()}');
    }
  }

  /// Obtiene una materia por ID
  Future<Map<String, dynamic>> getMateriaById(int materiaId) async {
    try {
      final response = await get('${ApiConfig.materiasEndpoint}/$materiaId');
      return processResponse(response, 'Error al obtener materia');
    } catch (e) {
      throw Exception('Error al cargar materia: ${e.toString()}');
    }
  }

  /// Obtiene las materias de un curso específico
  Future<List<Map<String, dynamic>>> getMateriasByCurso(int cursoId) async {
    try {
      final response = await get('${ApiConfig.materiasEndpoint}/curso/$cursoId');
      return processListResponse(response, 'Error al obtener materias del curso');
    } catch (e) {
      throw Exception('Error al cargar materias del curso: ${e.toString()}');
    }
  }

  /// Obtiene las materias asignadas a un profesor
  Future<List<Map<String, dynamic>>> getMateriasByProfesor(String profesorId) async {
    try {
      final response = await get('${ApiConfig.materiasEndpoint}/profesor/$profesorId');
      return processListResponse(response, 'Error al obtener materias del profesor');
    } catch (e) {
      throw Exception('Error al cargar materias del profesor: ${e.toString()}');
    }
  }

  /// Crea una nueva materia
  Future<Map<String, dynamic>> createMateria(Map<String, dynamic> materiaData) async {
    try {
      final response = await post(ApiConfig.materiasEndpoint, materiaData);
      return processResponse(response, 'Error al crear materia');
    } catch (e) {
      throw Exception('Error al crear materia: ${e.toString()}');
    }
  }

  /// Actualiza una materia existente
  Future<Map<String, dynamic>> updateMateria(int materiaId, Map<String, dynamic> materiaData) async {
    try {
      final response = await put('${ApiConfig.materiasEndpoint}/$materiaId', materiaData);
      return processResponse(response, 'Error al actualizar materia');
    } catch (e) {
      throw Exception('Error al actualizar materia: ${e.toString()}');
    }
  }

  /// Elimina una materia
  Future<bool> deleteMateria(int materiaId) async {
    try {
      final response = await delete('${ApiConfig.materiasEndpoint}/$materiaId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar materia: ${e.toString()}');
    }
  }

  /// Asigna una materia a un curso
  Future<bool> assignMateriaToCurso(int materiaId, int cursoId) async {
    try {
      final response = await post('${ApiConfig.materiasEndpoint}/$materiaId/curso', {
        'cursoId': cursoId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al asignar materia al curso: ${e.toString()}');
    }
  }

  /// Remueve una materia de un curso
  Future<bool> removeMateriaFromCurso(int materiaId, int cursoId) async {
    try {
      final response = await delete('${ApiConfig.materiasEndpoint}/$materiaId/curso/$cursoId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al remover materia del curso: ${e.toString()}');
    }
  }

  /// Asigna un profesor a una materia
  Future<bool> assignProfesorToMateria(int materiaId, String profesorId) async {
    try {
      final response = await post('${ApiConfig.materiasEndpoint}/$materiaId/profesor', {
        'profesorId': profesorId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al asignar profesor a la materia: ${e.toString()}');
    }
  }

  /// Busca materias por nombre
  Future<List<Map<String, dynamic>>> searchMaterias(String query) async {
    try {
      final response = await get('${ApiConfig.materiasEndpoint}/buscar?q=$query');
      return processListResponse(response, 'Error al buscar materias');
    } catch (e) {
      throw Exception('Error al buscar materias: ${e.toString()}');
    }
  }
}
