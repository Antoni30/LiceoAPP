import 'dart:convert';
import 'dart:io';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/api_config.dart';

abstract class BaseService {
  
  static const String _baseTag = 'BaseService';
  
  /// Obtiene los headers de autenticación
  Future<Map<String, String>> getAuthHeaders() async {
    if (ApiConfig.isDebugMode) {
      developer.log('Obteniendo headers de autenticación', name: _baseTag);
    }
    
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString('user_id');
    final token = prefs.getString('auth_token'); // Para futuras implementaciones
    
    final headers = ApiConfig.getAuthHeaders(token);
    if (userId != null) {
      headers['X-User-ID'] = userId;
    }
    
    if (ApiConfig.isDebugMode) {
      developer.log('Headers generados: ${headers.map((k, v) => MapEntry(k, k.contains('token') || k.contains('auth') ? '***' : v))}', name: _baseTag);
    }
    return headers;
  }

  /// Maneja errores de conexión específicos
  Exception _handleConnectionError(dynamic error) {
    if (ApiConfig.isDebugMode) {
      developer.log('Error de conexión detectado: ${error.runtimeType} - ${error.toString()}', name: _baseTag, level: 1000);
    }
    
    if (error is SocketException) {
      final message = 'No se puede conectar al servidor. Verifica tu conexión a internet y que el servidor esté ejecutándose en ${ApiConfig.serverUrl}';
      if (ApiConfig.isDebugMode) {
        developer.log('Error de socket: $message', name: _baseTag, level: 1000);
      }
      return Exception(message);
    } else if (error is HttpException) {
      final message = 'Error HTTP: ${error.message}';
      if (ApiConfig.isDebugMode) {
        developer.log('Error HTTP: $message', name: _baseTag, level: 1000);
      }
      return Exception(message);
    } else if (error.toString().contains('TimeoutException')) {
      final message = 'Tiempo de espera agotado. El servidor puede estar sobrecargado o no responder.';
      if (ApiConfig.isDebugMode) {
        developer.log('Timeout: $message', name: _baseTag, level: 1000);
      }
      return Exception(message);
    } else {
      final message = 'Error de conexión: ${error.toString()}';
      if (ApiConfig.isDebugMode) {
        developer.log('Error genérico: $message', name: _baseTag, level: 1000);
      }
      return Exception(message);
    }
  }

  /// Realiza una petición GET
  Future<http.Response> get(String endpoint) async {
    developer.log('Realizando GET a: $endpoint', name: _baseTag);
    
    try {
      final headers = await getAuthHeaders();
      developer.log('Enviando petición GET...', name: _baseTag);
      
      final response = await http.get(
        Uri.parse(endpoint),
        headers: headers,
      ).timeout(ApiConfig.connectionTimeout);
      
      developer.log('GET completado - Status: ${response.statusCode}', name: _baseTag);
      developer.log('GET Response body: ${response.body.length > 200 ? '${response.body.substring(0, 200)}...' : response.body}', name: _baseTag);
      
      return response;
    } catch (e) {
      developer.log('Error en GET: ${e.toString()}', name: _baseTag, level: 1000);
      throw _handleConnectionError(e);
    }
  }

  /// Realiza una petición POST
  Future<http.Response> post(String endpoint, Map<String, dynamic> data) async {
    developer.log('Realizando POST a: $endpoint', name: _baseTag);
    developer.log('POST data: ${jsonEncode(data).length > 200 ? '${jsonEncode(data).substring(0, 200)}...' : jsonEncode(data)}', name: _baseTag);
    
    try {
      final headers = await getAuthHeaders();
      developer.log('Enviando petición POST...', name: _baseTag);
      
      final response = await http.post(
        Uri.parse(endpoint),
        headers: headers,
        body: jsonEncode(data),
      ).timeout(ApiConfig.connectionTimeout);
      
      developer.log('POST completado - Status: ${response.statusCode}', name: _baseTag);
      developer.log('POST Response body: ${response.body.length > 200 ? '${response.body.substring(0, 200)}...' : response.body}', name: _baseTag);
      
      return response;
    } catch (e) {
      developer.log('Error en POST: ${e.toString()}', name: _baseTag, level: 1000);
      throw _handleConnectionError(e);
    }
  }

  /// Realiza una petición PUT
  Future<http.Response> put(String endpoint, Map<String, dynamic> data) async {
    try {
      final response = await http.put(
        Uri.parse(endpoint),
        headers: await getAuthHeaders(),
        body: jsonEncode(data),
      ).timeout(ApiConfig.connectionTimeout);
      
      return response;
    } catch (e) {
      throw _handleConnectionError(e);
    }
  }

  /// Realiza una petición DELETE
  Future<http.Response> delete(String endpoint) async {
    try {
      final response = await http.delete(
        Uri.parse(endpoint),
        headers: await getAuthHeaders(),
      ).timeout(ApiConfig.connectionTimeout);
      
      return response;
    } catch (e) {
      throw _handleConnectionError(e);
    }
  }

  /// Verifica si la respuesta es exitosa
  bool isSuccessResponse(http.Response response) {
    return response.statusCode >= 200 && response.statusCode < 300;
  }

  /// Procesa la respuesta y maneja errores
  Map<String, dynamic> processResponse(http.Response response, String errorMessage) {
    if (!isSuccessResponse(response)) {
      final error = jsonDecode(response.body);
      throw Exception(error['message'] ?? errorMessage);
    }
    return jsonDecode(response.body);
  }

  /// Procesa la respuesta para listas
  List<Map<String, dynamic>> processListResponse(http.Response response, String errorMessage) {
    if (!isSuccessResponse(response)) {
      throw Exception(errorMessage);
    }
    return List<Map<String, dynamic>>.from(jsonDecode(response.body));
  }

  /// Verifica la conectividad con el servidor
  Future<bool> testConnection() async {
    developer.log('Probando conectividad con el servidor...', name: _baseTag);
    
    try {
      final testUrl = '${ApiConfig.baseUrl}/health';
      developer.log('URL de prueba: $testUrl', name: _baseTag);
      
      final response = await http.get(
        Uri.parse(testUrl),
        headers: ApiConfig.defaultHeaders,
      ).timeout(const Duration(seconds: 5));
      
      final isConnected = isSuccessResponse(response);
      developer.log('Resultado de conectividad: $isConnected (Status: ${response.statusCode})', name: _baseTag);
      
      return isConnected;
    } catch (e) {
      developer.log('Error de conectividad: ${e.toString()}', name: _baseTag, level: 1000);
      return false;
    }
  }

  /// Obtiene información del servidor
  Future<Map<String, dynamic>?> getServerInfo() async {
    try {
      final response = await http.get(
        Uri.parse('${ApiConfig.baseUrl}/info'),
        headers: ApiConfig.defaultHeaders,
      ).timeout(const Duration(seconds: 5));
      
      if (isSuccessResponse(response)) {
        return jsonDecode(response.body);
      }
      return null;
    } catch (e) {
      print('Error al obtener info del servidor: ${e.toString()}');
      return null;
    }
  }
}
