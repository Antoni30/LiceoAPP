import 'dart:convert';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user_model.dart';
import '../constants/api_config.dart';
import 'base_service.dart';

class AuthService extends BaseService {
  // Singleton pattern
  static final AuthService _instance = AuthService._internal();
  factory AuthService() => _instance;
  AuthService._internal();

  static const String _tag = 'AuthService';

  Future<Map<String, dynamic>> login(String idUsuario, String contrasena) async {
    print('🔐 [AuthService] Iniciando login para usuario: $idUsuario');
    developer.log('Iniciando login para usuario: $idUsuario', name: _tag);
    
    try {
      // 1. Intento de autenticación inicial
      final loginUrl = '${ApiConfig.authEndpoint}/login';
      print('🔗 [AuthService] URL de login: $loginUrl');
      developer.log('URL de login: $loginUrl', name: _tag);
      
      print('📤 [AuthService] Enviando datos de login para usuario: $idUsuario');
      developer.log('Enviando datos de login para usuario: $idUsuario', name: _tag);
      
      final response = await http.post(
        Uri.parse(loginUrl),
        headers: ApiConfig.defaultHeaders,
        body: jsonEncode({
          'idUsuario': idUsuario,
          'contrasena': contrasena,
        }),
      );

      print('📥 [AuthService] Status code recibido: ${response.statusCode}');
      print('📥 [AuthService] Body de respuesta: ${response.body}');
      developer.log('Status code recibido: ${response.statusCode}', name: _tag);
      developer.log('Headers de respuesta: ${response.headers}', name: _tag);
      developer.log('Body de respuesta: ${response.body}', name: _tag);

      final data = jsonDecode(response.body);
      print('🔍 [AuthService] Analizando respuesta del backend:');
      print('   - success: ${data['success']}');
      print('   - requiresVerification: ${data['requiresVerification']}');
      print('   - mfaRequired: ${data['mfaRequired']}');
      print('   - token: ${data['token'] != null ? 'presente' : 'ausente'}');

      // 2. PRIMERO verificar MFA antes que otras validaciones
      // Basado en el cliente web: if (data["success"] === false)
      if (data['success'] == false) {
        print('🔒 [AuthService] Login requiere verificación de código 2FA');
        developer.log('Login requiere verificación de código 2FA', name: _tag);
        return {
          'requiresVerification': true,
          'idUsuario': idUsuario,
        };
      }

      // 3. Verificar errores DESPUÉS de validar MFA
      if (!isSuccessResponse(response)) {
        developer.log('Error en la respuesta: ${data['message'] ?? 'Error desconocido'}', name: _tag, level: 1000);
        throw Exception(data['message'] ?? 'Error en la autenticación');
      }

      print('✅ [AuthService] Login exitoso, obteniendo información de roles del usuario');
      developer.log('Login exitoso, obteniendo información de roles del usuario', name: _tag);
      
      // Extraer el token de la respuesta
      final token = data['token'];
      print('🔑 [AuthService] Token recibido: ${token?.substring(0, 20)}...'); // Solo mostrar primeros 20 chars
      
      // Guardar el token en SharedPreferences para uso futuro
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('auth_token', token);
      print('💾 [AuthService] Token guardado en SharedPreferences');
      
      // 3. Obtener información del usuario (roles)
      final usuarioRolesUrl = '${ApiConfig.usuariosRolesEndpoint}/usuario/$idUsuario';
      print('🔗 [AuthService] URL de usuario-roles: $usuarioRolesUrl');
      developer.log('URL de usuario-roles: $usuarioRolesUrl', name: _tag);
      
      // Crear headers con el token JWT como cookie (el backend espera cookies)
      final headersWithAuth = {
        ...ApiConfig.defaultHeaders,
        'Cookie': 'token=$token',
      };
      print('🍪 [AuthService] Enviando headers con Cookie: token=${token?.substring(0, 20)}...');
      print('🔍 [AuthService] Headers completos: $headersWithAuth');
      
      final rolesResponse = await http.get(
        Uri.parse(usuarioRolesUrl),
        headers: headersWithAuth,
      );

      print('📥 [AuthService] Status code de usuario-roles: ${rolesResponse.statusCode}');
      print('📥 [AuthService] Body de usuario-roles: ${rolesResponse.body}');
      developer.log('Status code de usuario-roles: ${rolesResponse.statusCode}', name: _tag);
      developer.log('Body de usuario-roles: ${rolesResponse.body}', name: _tag);

      if (!isSuccessResponse(rolesResponse)) {
        print('❌ [AuthService] Error al obtener roles del usuario - Status: ${rolesResponse.statusCode}');
        developer.log('Error al obtener roles del usuario', name: _tag, level: 1000);
        throw Exception('El rol de usuario aún no ha sido asignado por el administrador del sistema.');
      }

      final rolesData = jsonDecode(rolesResponse.body);
      print('📊 [AuthService] Datos de roles recibidos: $rolesData');
      
      if (rolesData.isEmpty) {
        print('❌ [AuthService] Usuario no tiene roles asignados');
        developer.log('Usuario no tiene roles asignados', name: _tag, level: 1000);
        throw Exception('Estado: Pendiente de asignación de rol por parte del administrador.');
      }

      // 4. Obtener nombre del rol desde el primer rol asignado
      // En el cliente web: roles[0].idRol
      final firstRoleId = rolesData[0]['idRol'];
      print('🎯 [AuthService] ID del primer rol: $firstRoleId');
      final rolUrl = '${ApiConfig.rolesEndpoint}/$firstRoleId';
      print('🔗 [AuthService] URL de rol: $rolUrl');
      developer.log('URL de rol: $rolUrl', name: _tag);
      
      final rolResponse = await http.get(
        Uri.parse(rolUrl),
        headers: headersWithAuth, // Usar los mismos headers con token
      );

      print('📥 [AuthService] Status code de rol: ${rolResponse.statusCode}');
      print('📥 [AuthService] Body de rol: ${rolResponse.body}');
      developer.log('Status code de rol: ${rolResponse.statusCode}', name: _tag);
      developer.log('Body de rol: ${rolResponse.body}', name: _tag);

      if (!isSuccessResponse(rolResponse)) {
        developer.log('Error al obtener nombre del rol', name: _tag, level: 1000);
        throw Exception('No se pudo obtener el nombre del rol');
      }

      final rolData = jsonDecode(rolResponse.body);
      final userRole = (rolData['nombre'] as String).toLowerCase();
      developer.log('Rol del usuario: $userRole', name: _tag);

      // 5. Verificar que el rol tiene acceso al sistema móvil (solo profesores y estudiantes)
      if (userRole != 'profesor' && userRole != 'estudiante') {
        if (userRole == 'administrador') {
          developer.log('Administrador intenta acceder al sistema móvil: $userRole', name: _tag, level: 1000);
          throw Exception('Este tipo de usuario no es admitido en la aplicación móvil. Los administradores deben usar el sistema web.');
        } else {
          developer.log('Usuario no tiene rol válido para acceder al sistema móvil: $userRole', name: _tag, level: 1000);
          throw Exception('Tu rol no tiene acceso al sistema móvil');
        }
      }

      developer.log('Guardando sesión de usuario con rol: $userRole', name: _tag);
      // Guardar información de sesión
      final userObject = User(
        idUsuario: idUsuario,
        nombre: data['nombre'] ?? '',
        apellido: data['apellido'] ?? '',
        email: data['email'] ?? '',
        roles: [userRole],
      );
      await _saveUserSession(idUsuario, [userRole], userObject);

      developer.log('Login completado exitosamente. Rol: $userRole', name: _tag);

      return {
        'success': true,
        'userRole': userRole,
        'idUsuario': idUsuario,
        'user': userObject,
      };
    } catch (e) {
      developer.log('Error en login: ${e.toString()}', name: _tag, level: 1000);
      throw Exception(e.toString());
    }
  }

  Future<bool> verifyCode(String idUsuario, String code) async {
    developer.log('Verificando código para usuario: $idUsuario', name: _tag);
    
    try {
      final verifyUrl = '${ApiConfig.authEndpoint}/verify-mfa';
      developer.log('URL de verificación: $verifyUrl', name: _tag);
      
      final response = await http.post(
        Uri.parse(verifyUrl),
        headers: ApiConfig.defaultHeaders,
        body: jsonEncode({
          'idUsuario': idUsuario,
          'code': code,
        }),
      );

      print('📥 [AuthService] Status verificación: ${response.statusCode}');
      print('📥 [AuthService] Body verificación: ${response.body}');
      developer.log('Status code de verificación: ${response.statusCode}', name: _tag);
      developer.log('Body de verificación: ${response.body}', name: _tag);

      final data = jsonDecode(response.body);
      final success = isSuccessResponse(response) && data['success'] == true;
      
      if (success) {
        print('✅ [AuthService] Verificación exitosa, completando login...');
        
        // Después de verificación exitosa, completar el proceso de login
        await _completeLoginAfterVerification(idUsuario);
      }
      
      developer.log('Verificación exitosa: $success', name: _tag);
      return success;
    } catch (e) {
      developer.log('Error en verificación: ${e.toString()}', name: _tag, level: 1000);
      return false;
    }
  }

  Future<void> _completeLoginAfterVerification(String idUsuario) async {
    print('🔄 [AuthService] Completando login después de verificación MFA');
    
    try {
      // Obtener roles del usuario (las cookies ya están establecidas)
      final usuarioRolesUrl = '${ApiConfig.usuariosRolesEndpoint}/usuario/$idUsuario';
      final headersWithCredentials = {
        ...ApiConfig.defaultHeaders,
      };
      
      final rolesResponse = await http.get(
        Uri.parse(usuarioRolesUrl),
        headers: headersWithCredentials,
      );

      if (!isSuccessResponse(rolesResponse)) {
        throw Exception('Error al obtener roles después de verificación');
      }

      final rolesData = jsonDecode(rolesResponse.body);
      if (rolesData.isEmpty) {
        throw Exception('Usuario no tiene roles asignados');
      }

      // Obtener nombre del rol
      final firstRoleId = rolesData[0]['idRol'];
      final rolUrl = '${ApiConfig.rolesEndpoint}/$firstRoleId';
      
      final rolResponse = await http.get(
        Uri.parse(rolUrl),
        headers: headersWithCredentials,
      );

      if (!isSuccessResponse(rolResponse)) {
        throw Exception('Error al obtener información del rol');
      }

      final rolData = jsonDecode(rolResponse.body);
      final userRole = (rolData['nombre'] as String).toLowerCase();

      // Verificar acceso móvil
      if (userRole != 'profesor' && userRole != 'estudiante') {
        if (userRole == 'administrador') {
          throw Exception('Este tipo de usuario no es admitido en la aplicación móvil.');
        } else {
          throw Exception('Tu rol no tiene acceso al sistema móvil');
        }
      }

      // Guardar sesión
      final userObject = User(
        idUsuario: idUsuario,
        nombre: '', // El backend no devuelve estos datos en la verificación
        apellido: '',
        email: '',
        roles: [userRole],
      );
      await _saveUserSession(idUsuario, [userRole], userObject);

      print('✅ [AuthService] Login completado después de verificación MFA');
    } catch (e) {
      print('❌ [AuthService] Error completando login: $e');
      throw e;
    }
  }

  Future<void> logout() async {
    developer.log('Iniciando logout', name: _tag);
    
    try {
      await http.post(Uri.parse('${ApiConfig.authEndpoint}/logout'));
      developer.log('Logout del servidor completado', name: _tag);
    } catch (e) {
      developer.log('Error en logout del servidor: ${e.toString()}', name: _tag, level: 1000);
    } finally {
      await _clearUserSession();
      developer.log('Sesión local limpiada', name: _tag);
    }
  }

  Future<User?> getCurrentUser() async {
    developer.log('Obteniendo usuario actual', name: _tag);
    
    final prefs = await SharedPreferences.getInstance();
    final userJson = prefs.getString('current_user');
    
    if (userJson != null) {
      developer.log('Usuario encontrado en preferencias', name: _tag);
      return User.fromJson(jsonDecode(userJson));
    }
    
    developer.log('No hay usuario en preferencias', name: _tag);
    return null;
  }

  Future<String?> getCurrentUserRole() async {
    developer.log('Obteniendo rol del usuario actual', name: _tag);
    
    final prefs = await SharedPreferences.getInstance();
    final role = prefs.getString('user_role');
    
    developer.log('Rol actual: $role', name: _tag);
    return role;
  }

  Future<bool> isLoggedIn() async {
    developer.log('Verificando si el usuario está logueado', name: _tag);
    
    final user = await getCurrentUser();
    final isLogged = user != null;
    
    developer.log('Usuario logueado: $isLogged', name: _tag);
    return isLogged;
  }

  Future<void> _saveUserSession(String idUsuario, List<String> roles, User user) async {
    developer.log('Guardando sesión para usuario: $idUsuario con roles: $roles', name: _tag);
    
    final prefs = await SharedPreferences.getInstance();
    // Para app móvil: priorizar profesor sobre estudiante
    final primaryRole = roles.contains('profesor') ? 'profesor' : 'estudiante';
    
    await prefs.setString('user_id', idUsuario);
    await prefs.setString('user_role', primaryRole);
    await prefs.setStringList('user_roles', roles);
    await prefs.setString('current_user', jsonEncode(user.toJson()));
    
    developer.log('Sesión guardada exitosamente', name: _tag);
  }

  Future<void> _clearUserSession() async {
    developer.log('Limpiando sesión de usuario', name: _tag);
    
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('user_id');
    await prefs.remove('user_role');
    await prefs.remove('user_roles');
    await prefs.remove('current_user');
    await prefs.remove('auth_token'); // Limpiar también el token JWT
    
    developer.log('Sesión limpiada exitosamente', name: _tag);
  }

  // ========== MÉTODOS AUXILIARES PARA AUTENTICACIÓN MÓVIL ==========
  
  /// Verifica permisos basado en roles (solo para app móvil)
  bool hasPermission(List<String> userRoles, String requiredRole) {
    if (requiredRole == 'profesor') {
      return userRoles.contains('profesor');
    } else if (requiredRole == 'estudiante') {
      return userRoles.contains('estudiante');
    }
    return false;
  }

  /// Obtiene funciones disponibles según el rol (solo para app móvil)
  List<String> getAvailableFunctions(List<String> userRoles) {
    List<String> functions = [];
    
    if (userRoles.contains('profesor')) {
      functions.addAll([
        'dashboard_profesor',
        'mis_cursos',
        'mis_materias',
        'notas_gestion',
        'participantes_visualizacion',
        'reportes_mis_cursos',
        'perfil_usuario',
      ]);
    }
    
    if (userRoles.contains('estudiante')) {
      functions.addAll([
        'dashboard_estudiante',
        'mis_notas',
        'mis_cursos_estudiante',
        'horarios',
        'perfil_usuario',
      ]);
    }
    
    return functions;
  }
}
