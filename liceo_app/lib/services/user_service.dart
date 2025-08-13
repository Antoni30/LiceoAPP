import 'dart:convert';
import '../constants/api_config.dart';
import 'base_service.dart';

class UserService extends BaseService {
  // Singleton pattern
  static final UserService _instance = UserService._internal();
  factory UserService() => _instance;
  UserService._internal();

  /// Obtiene los datos completos del usuario por ID
  Future<Map<String, dynamic>> getUserProfile(String userId) async {
    try {
      final url = '${ApiConfig.usuariosEndpoint}/$userId';
      print('🔍 [UserService] Obteniendo perfil de usuario: $url');
      
      final response = await get(url);

      if (!isSuccessResponse(response)) {
        throw Exception('No se pudo obtener la información del usuario');
      }

      final data = jsonDecode(response.body);
      print('✅ [UserService] Datos del usuario obtenidos correctamente');
      return data;
    } catch (e) {
      print('❌ [UserService] Error al obtener datos del usuario: $e');
      throw Exception('Error al obtener los datos del usuario: $e');
    }
  }

  /// Actualiza los datos del usuario
  Future<Map<String, dynamic>> updateUserProfile(String userId, Map<String, dynamic> userData) async {
    try {
      final url = '${ApiConfig.usuariosEndpoint}/$userId';
      print('🔄 [UserService] Actualizando perfil de usuario: $url');
      
      // Preparar el payload como lo espera el backend
      final payload = {
        'nombres': userData['nombres'],
        'apellidos': userData['apellidos'],
        'nickname': userData['nickname'],
        'estado': userData['estado'],
        'mfaHabilitado': userData['mfaHabilitado'] ?? false,
        'roles': null,
        'email': userData['email'],
        'emailVerificado': userData['emailVerificado'],
      };

      final response = await put(url, payload);

      if (!isSuccessResponse(response)) {
        final errorData = jsonDecode(response.body);
        String errorMessage = 'Error al actualizar el usuario';
        
        if (errorData is Map && errorData.containsKey('message')) {
          errorMessage = errorData['message'];
        }
        
        throw Exception(errorMessage);
      }

      print('✅ [UserService] Usuario actualizado correctamente');
      return jsonDecode(response.body);
    } catch (e) {
      print('❌ [UserService] Error al actualizar usuario: $e');
      throw Exception('Error al actualizar el usuario: $e');
    }
  }

  /// Verifica si un nickname está disponible
  Future<bool> isNicknameAvailable(String nickname, String currentUserId) async {
    try {
      // Primero obtener todos los usuarios para verificar duplicados
      final response = await get(ApiConfig.usuariosEndpoint);

      if (!isSuccessResponse(response)) {
        return true; // Si no podemos verificar, asumimos que está disponible
      }

      final List<dynamic> users = jsonDecode(response.body);
      
      // Verificar si algún otro usuario ya tiene ese nickname
      for (final user in users) {
        if (user['nickname'] == nickname && user['idUsuario'] != currentUserId) {
          return false;
        }
      }
      
      return true;
    } catch (e) {
      print('⚠️ [UserService] Error al verificar disponibilidad de nickname: $e');
      return true; // En caso de error, asumimos que está disponible
    }
  }
}
