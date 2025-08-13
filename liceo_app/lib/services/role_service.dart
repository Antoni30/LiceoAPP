import '../constants/api_config.dart';
import 'base_service.dart';

class RoleService extends BaseService {
  // Singleton pattern
  static final RoleService _instance = RoleService._internal();
  factory RoleService() => _instance;
  RoleService._internal();

  /// Obtiene todos los roles
  Future<List<Map<String, dynamic>>> getAllRoles() async {
    try {
      final response = await get(ApiConfig.rolesEndpoint);
      return processListResponse(response, 'Error al obtener roles');
    } catch (e) {
      throw Exception('Error al cargar roles: ${e.toString()}');
    }
  }

  /// Obtiene un rol por ID
  Future<Map<String, dynamic>> getRoleById(int roleId) async {
    try {
      final response = await get('${ApiConfig.rolesEndpoint}/$roleId');
      return processResponse(response, 'Error al obtener rol');
    } catch (e) {
      throw Exception('Error al cargar rol: ${e.toString()}');
    }
  }

  /// Obtiene los roles de un usuario específico
  Future<List<Map<String, dynamic>>> getUserRoles(String userId) async {
    try {
      final response = await get('${ApiConfig.usuariosRolesEndpoint}/usuario/$userId');
      return processListResponse(response, 'Error al obtener roles del usuario');
    } catch (e) {
      throw Exception('Error al cargar roles del usuario: ${e.toString()}');
    }
  }

  /// Asigna un rol a un usuario
  Future<bool> assignRoleToUser(String userId, int roleId) async {
    try {
      final response = await post(ApiConfig.usuariosRolesEndpoint, {
        'idUsuario': userId,
        'idRol': roleId,
      });
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al asignar rol: ${e.toString()}');
    }
  }

  /// Remueve un rol de un usuario
  Future<bool> removeRoleFromUser(String userId, int roleId) async {
    try {
      final response = await delete('${ApiConfig.usuariosRolesEndpoint}/$userId/$roleId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al remover rol: ${e.toString()}');
    }
  }

  /// Crea un nuevo rol
  Future<Map<String, dynamic>> createRole(Map<String, dynamic> roleData) async {
    try {
      final response = await post(ApiConfig.rolesEndpoint, roleData);
      return processResponse(response, 'Error al crear rol');
    } catch (e) {
      throw Exception('Error al crear rol: ${e.toString()}');
    }
  }

  /// Actualiza un rol existente
  Future<Map<String, dynamic>> updateRole(int roleId, Map<String, dynamic> roleData) async {
    try {
      final response = await put('${ApiConfig.rolesEndpoint}/$roleId', roleData);
      return processResponse(response, 'Error al actualizar rol');
    } catch (e) {
      throw Exception('Error al actualizar rol: ${e.toString()}');
    }
  }

  /// Elimina un rol
  Future<bool> deleteRole(int roleId) async {
    try {
      final response = await delete('${ApiConfig.rolesEndpoint}/$roleId');
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al eliminar rol: ${e.toString()}');
    }
  }

  /// Verifica si un usuario tiene un rol específico
  Future<bool> userHasRole(String userId, String roleName) async {
    try {
      final userRoles = await getUserRoles(userId);
      return userRoles.any((role) => role['rol']['nombre'] == roleName);
    } catch (e) {
      return false;
    }
  }
}
