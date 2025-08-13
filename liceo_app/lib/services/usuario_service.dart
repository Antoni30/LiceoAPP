import '../constants/api_config.dart';
import 'base_service.dart';

class UsuarioService extends BaseService {
  // Singleton pattern
  static final UsuarioService _instance = UsuarioService._internal();
  factory UsuarioService() => _instance;
  UsuarioService._internal();

  /// Obtiene todos los usuarios
  Future<List<Map<String, dynamic>>> getAllUsers() async {
    try {
      final response = await get(ApiConfig.usuariosEndpoint);
      return processListResponse(response, 'Error al obtener usuarios');
    } catch (e) {
      throw Exception('Error al cargar usuarios: ${e.toString()}');
    }
  }

  /// Obtiene un usuario por ID
  Future<Map<String, dynamic>> getUserById(String userId) async {
    try {
      final response = await get('${ApiConfig.usuariosEndpoint}/$userId');
      return processResponse(response, 'Error al obtener usuario');
    } catch (e) {
      throw Exception('Error al cargar usuario: ${e.toString()}');
    }
  }

  /// Crea un nuevo usuario
  Future<Map<String, dynamic>> createUser(Map<String, dynamic> userData) async {
    try {
      final response = await post(ApiConfig.usuariosEndpoint, userData);
      return processResponse(response, 'Error al crear usuario');
    } catch (e) {
      throw Exception('Error al crear usuario: ${e.toString()}');
    }
  }

  /// Actualiza un usuario existente
  Future<Map<String, dynamic>> updateUser(String userId, Map<String, dynamic> userData) async {
    try {
      final response = await put('${ApiConfig.usuariosEndpoint}/$userId', userData);
      return processResponse(response, 'Error al actualizar usuario');
    } catch (e) {
      throw Exception('Error al actualizar usuario: ${e.toString()}');
    }
  }

  /// Elimina un usuario
  Future<bool> deleteUser(String userId) async {
    try {
      final response = await delete('${ApiConfig.usuariosEndpoint}/$userId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar usuario: ${e.toString()}');
    }
  }

  /// Busca usuarios por criterio
  Future<List<Map<String, dynamic>>> searchUsers(String query) async {
    try {
      final response = await get('${ApiConfig.usuariosEndpoint}/buscar?q=$query');
      return processListResponse(response, 'Error al buscar usuarios');
    } catch (e) {
      throw Exception('Error al buscar usuarios: ${e.toString()}');
    }
  }

  /// Obtiene usuarios por rol
  Future<List<Map<String, dynamic>>> getUsersByRole(String roleName) async {
    try {
      final response = await get('${ApiConfig.usuariosEndpoint}/rol/$roleName');
      return processListResponse(response, 'Error al obtener usuarios por rol');
    } catch (e) {
      throw Exception('Error al cargar usuarios por rol: ${e.toString()}');
    }
  }
}
