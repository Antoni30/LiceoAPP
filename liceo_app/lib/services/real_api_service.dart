import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/api_config.dart';
import '../models/user_model.dart';
import '../models/academic_models.dart';

class RealApiService {
  static const Map<String, String> _defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  static Future<Map<String, String>> _getHeaders() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');
    
    final headers = Map<String, String>.from(_defaultHeaders);
    
    print('🔍 [RealApiService] Verificando token en SharedPreferences...');
    print('🔍 [RealApiService] Token disponible: ${token != null}');
    if (token != null) {
      print('🔍 [RealApiService] Token length: ${token.length}');
      print('🔍 [RealApiService] Token preview: ${token.length > 20 ? token.substring(0, 20) : token}...');
    }
    
    if (token != null && token.isNotEmpty) {
      // Intentar ambos formatos: Cookie y Authorization header
      headers['Cookie'] = 'token=$token';
      headers['Authorization'] = 'Bearer $token';
      print('🍪 [RealApiService] Enviando request con token como cookie Y Authorization header');
      print('🍪 [RealApiService] Cookie header: token=${token.length > 20 ? token.substring(0, 20) : token}...');
      print('🔑 [RealApiService] Authorization header: Bearer ${token.length > 20 ? token.substring(0, 20) : token}...');
    } else {
      print('⚠️ [RealApiService] No hay token de autenticación disponible');
      print('⚠️ [RealApiService] Todas las keys en SharedPreferences: ${prefs.getKeys()}');
    }
    
    return headers;
  }

  static Future<dynamic> _request(String endpoint, {
    String method = 'GET',
    Map<String, dynamic>? body,
    Map<String, String>? additionalHeaders,
  }) async {
    final url = Uri.parse('${ApiConfig.baseUrl}$endpoint');
    final headers = await _getHeaders();
    if (additionalHeaders != null) {
      headers.addAll(additionalHeaders);
    }

    http.Response response;
    
    try {
      switch (method.toUpperCase()) {
        case 'GET':
          response = await http.get(url, headers: headers);
          break;
        case 'POST':
          response = await http.post(
            url, 
            headers: headers, 
            body: body != null ? jsonEncode(body) : null,
          );
          break;
        case 'PUT':
          response = await http.put(
            url, 
            headers: headers, 
            body: body != null ? jsonEncode(body) : null,
          );
          break;
        case 'DELETE':
          response = await http.delete(url, headers: headers);
          break;
        default:
          throw Exception('Método HTTP no soportado: $method');
      }

      print('📥 [RealApiService] Response status: ${response.statusCode}');
      print('📥 [RealApiService] Response body: ${response.body}');
      
      if (response.statusCode >= 200 && response.statusCode < 300) {
        if (response.body.isNotEmpty) {
          return jsonDecode(response.body);
        }
        return null;
      } else {
        String errorMessage = 'HTTP ${response.statusCode}';
        
        if (response.statusCode == 401) {
          errorMessage = 'No autorizado - Token inválido o expirado';
          print('🚫 [RealApiService] Error 401: Token posiblemente expirado');
          print('🚫 [RealApiService] Endpoint que falló: $endpoint');
          print('🚫 [RealApiService] Headers enviados: $headers');
          
          // Limpiar token expirado
          final prefs = await SharedPreferences.getInstance();
          await prefs.remove('auth_token');
          print('🧹 [RealApiService] Token eliminado de SharedPreferences debido a 401');
        }
        
        try {
          final errorData = jsonDecode(response.body);
          errorMessage = errorData['message'] ?? errorMessage;
        } catch (e) {
          // Si no se puede parsear el error, usar el mensaje por defecto
        }
        
        print('❌ [RealApiService] Error: $errorMessage');
        throw Exception(errorMessage);
      }
    } catch (e) {
      if (e is Exception) {
        rethrow;
      }
      throw Exception('Error de conexión: $e');
    }
  }
}

class UserService {
  static Future<UserModel> getUser(String userId) async {
    try {
      print('📞 [UserService] Llamando getUser para userId: $userId');
      
      final userData = await RealApiService._request('/usuarios/$userId');
      return UserModel.fromJson(userData);
    } catch (e) {
      print('❌ [UserService] Error en getUser: $e');
      throw Exception('Error al obtener usuario: $e');
    }
  }

  static Future<List<UsuarioCurso>> getUserCursos(String userId) async {
    try {
      print('📞 [UserService] Llamando getUserCursos para userId: $userId');
      print('📞 [UserService] Endpoint: /usuarios-cursos/usuario/$userId');
      
      final cursosData = await RealApiService._request('/usuarios-cursos/usuario/$userId');
      print('✅ [UserService] getUserCursos exitoso, cursos encontrados: ${(cursosData as List).length}');
      
      return cursosData
          .map((curso) => UsuarioCurso.fromJson(curso))
          .toList();
    } catch (e) {
      print('❌ [UserService] Error en getUserCursos: $e');
      throw Exception('Error al obtener cursos del usuario: $e');
    }
  }

  static Future<List<Rol>> getUserRoles(String userId) async {
    try {
      print('📞 [UserService] Llamando getUserRoles para userId: $userId');
      
      final rolesData = await RealApiService._request('/usuarios-roles/usuario/$userId');
      return (rolesData as List)
          .map((rol) => Rol.fromJson(rol))
          .toList();
    } catch (e) {
      print('❌ [UserService] Error en getUserRoles: $e');
      throw Exception('Error al obtener roles del usuario: $e');
    }
  }
}

class CursoService {
  static Future<Curso> getCurso(String cursoId) async {
    try {
      print('📞 [CursoService] Llamando getCurso para cursoId: $cursoId');
      
      // Validar que cursoId sea convertible a int
      final cursoIdInt = int.tryParse(cursoId);
      if (cursoIdInt == null) {
        throw Exception('ID de curso inválido: $cursoId no es un número válido');
      }
      
      final cursoData = await RealApiService._request('/cursos/$cursoId');
      return Curso.fromJson(cursoData);
    } catch (e) {
      print('❌ [CursoService] Error en getCurso: $e');
      throw Exception('Error al obtener curso: $e');
    }
  }

  static Future<List<CursoMateria>> getCursoMaterias(String cursoId) async {
    try {
      print('📞 [CursoService] Llamando getCursoMaterias para cursoId: $cursoId');
      
      // Validar que cursoId sea convertible a int
      final cursoIdInt = int.tryParse(cursoId);
      if (cursoIdInt == null) {
        throw Exception('ID de curso inválido: $cursoId no es un número válido');
      }
      
      final materiasData = await RealApiService._request('/cursos-materias/curso/$cursoId');
      return (materiasData as List)
          .map((materia) => CursoMateria.fromJson(materia))
          .toList();
    } catch (e) {
      print('❌ [CursoService] Error en getCursoMaterias: $e');
      throw Exception('Error al obtener materias del curso: $e');
    }
  }

  static Future<List<UsuarioCurso>> getCursoUsuarios(String cursoId) async {
    try {
      print('📞 [CursoService] Llamando getCursoUsuarios para cursoId: $cursoId');
      
      // Validar que cursoId sea convertible a int
      final cursoIdInt = int.tryParse(cursoId);
      if (cursoIdInt == null) {
        throw Exception('ID de curso inválido: $cursoId no es un número válido');
      }
      
      final usuariosData = await RealApiService._request('/usuarios-cursos/curso/$cursoId');
      return (usuariosData as List)
          .map((usuario) => UsuarioCurso.fromJson(usuario))
          .toList();
    } catch (e) {
      print('❌ [CursoService] Error en getCursoUsuarios: $e');
      throw Exception('Error al obtener usuarios del curso: $e');
    }
  }
}

class MateriaService {
  static Future<Materia> getMateria(String materiaId) async {
    try {
      print('📞 [MateriaService] Llamando getMateria para materiaId: $materiaId');
      
      // Validar que materiaId sea convertible a int
      final materiaIdInt = int.tryParse(materiaId);
      if (materiaIdInt == null) {
        throw Exception('ID de materia inválido: $materiaId no es un número válido');
      }
      
      final materiaData = await RealApiService._request('/materias/$materiaId');
      return Materia.fromJson(materiaData);
    } catch (e) {
      print('❌ [MateriaService] Error en getMateria: $e');
      throw Exception('Error al obtener materia: $e');
    }
  }
}

class NotaService {
  static Future<List<Nota>> getNotasByUsuarioMateria(String userId, String materiaId) async {
    try {
      print('📞 [NotaService] Llamando getNotasByUsuarioMateria para userId: $userId, materiaId: $materiaId');
      
      // Validar que materiaId sea convertible a int (userId es String)
      final materiaIdInt = int.tryParse(materiaId);
      if (materiaIdInt == null) {
        throw Exception('ID de materia inválido: $materiaId no es un número válido');
      }
      
      final notasData = await RealApiService._request('/notas/usuario/$userId/materia/$materiaId');
      return (notasData as List)
          .map((nota) => Nota.fromJson(nota))
          .toList();
    } catch (e) {
      print('❌ [NotaService] Error en getNotasByUsuarioMateria: $e');
      // Retornar lista vacía si no hay notas
      return [];
    }
  }

  static Future<void> updateNota(String notaId, NotaRequest request) async {
    try {
      print('📞 [NotaService] Llamando updateNota para notaId: $notaId');
      
      // Validar que notaId sea convertible a int
      final notaIdInt = int.tryParse(notaId);
      if (notaIdInt == null) {
        throw Exception('ID de nota inválido: $notaId no es un número válido');
      }
      
      await RealApiService._request(
        '/notas/$notaId',
        method: 'PUT',
        body: request.toJson(),
      );
      print('✅ [NotaService] updateNota exitoso para notaId: $notaId');
    } catch (e) {
      print('❌ [NotaService] Error en updateNota: $e');
      throw Exception('Error al actualizar nota: $e');
    }
  }

  static Future<void> createNota(NotaRequest request) async {
    try {
      print('📞 [NotaService] Llamando createNota para userId: ${request.idUsuario}, materiaId: ${request.idMateria}');
      
      await RealApiService._request(
        '/notas',
        method: 'POST',
        body: request.toJson(),
      );
      print('✅ [NotaService] createNota exitoso');
    } catch (e) {
      print('❌ [NotaService] Error en createNota: $e');
      throw Exception('Error al crear nota: $e');
    }
  }
}