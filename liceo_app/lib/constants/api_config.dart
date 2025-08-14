/// Configuración centralizada de la API
/// 
/// Este archivo contiene toda la configuración relacionada con la API
/// Para cambiar el entorno, modifica la variable [environment]
class ApiConfig {
  // ========================================
  // CONFIGURACIÓN DE ENTORNO
  // ========================================
  
  /// Entorno actual de la aplicación
  /// Cambiar este valor para usar diferentes configuraciones
  /// Valores válidos: 'development', 'production', 'local'
  static const Environment environment = Environment.local;
  
  // ========================================
  // CONFIGURACIÓN DE URLs BASE
  // ========================================
  
  /// URL base según el entorno actual
  static String get baseUrl {
    switch (environment) {
      case Environment.development:
        return 'http://192.168.1.90:8080/api';
      case Environment.production:
        return 'https://liceo-api.dominio.com/api';
      case Environment.local:
        return 'http://192.168.1.90:8080/api';
    }
  }
  
  /// URL del servidor base (sin /api)
  static String get serverUrl {
    switch (environment) {
      case Environment.development:
        return 'http://192.168.1.90:8080';
      case Environment.production:
        return 'https://liceo-api.dominio.com';
      case Environment.local:
        return 'http://192.168.1.90:8080';
    }
  }
  
  // ========================================
  // ENDPOINTS ESPECÍFICOS
  // ========================================
  
  static String get authEndpoint => '$baseUrl/auth';
  static String get usuariosEndpoint => '$baseUrl/usuarios';
  static String get rolesEndpoint => '$baseUrl/roles';
  static String get usuariosRolesEndpoint => '$baseUrl/usuarios-roles';
  static String get cursosEndpoint => '$baseUrl/cursos';
  static String get materiasEndpoint => '$baseUrl/materias';
  static String get notasEndpoint => '$baseUrl/notas';
  static String get participantesEndpoint => '$baseUrl/participantes';
  static String get aniosAcademicosEndpoint => '$baseUrl/anios-academicos';
  static String get dashboardEndpoint => '$baseUrl/dashboard';
  
  // ========================================
  // CONFIGURACIÓN DE HEADERS
  // ========================================
  
  /// Headers comunes para todas las peticiones
  static const Map<String, String> defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };
  
  /// Headers para peticiones con autenticación (usando cookies como espera el backend)
  static Map<String, String> getAuthHeaders(String? token) {
    final headers = Map<String, String>.from(defaultHeaders);
    if (token != null && token.isNotEmpty) {
      headers['Cookie'] = 'token=$token';
    }
    return headers;
  }
  
  // ========================================
  // CONFIGURACIÓN DE TIMEOUTS
  // ========================================
  
  static const Duration connectionTimeout = Duration(seconds: 30);
  static const Duration receiveTimeout = Duration(seconds: 30);
  static const Duration sendTimeout = Duration(seconds: 30);
  
  // ========================================
  // CONFIGURACIÓN DE RETRY
  // ========================================
  
  static const int maxRetries = 3;
  static const Duration retryDelay = Duration(seconds: 2);
  
  // ========================================
  // CONFIGURACIÓN DE DEBUG
  // ========================================
  
  /// Habilitar logs detallados en desarrollo
  static bool get isDebugMode {
    switch (environment) {
      case Environment.development:
      case Environment.local:
        return true;
      case Environment.production:
        return false;
    }
  }
  
  // ========================================
  // UTILIDADES
  // ========================================
  
  /// Construye una URL completa para un endpoint específico
  static String buildEndpoint(String path) {
    if (path.startsWith('http')) {
      return path; // Ya es una URL completa
    }
    
    if (path.startsWith('/')) {
      return '$baseUrl$path';
    }
    
    return '$baseUrl/$path';
  }
  
  /// Información del entorno actual
  static Map<String, dynamic> get environmentInfo => {
    'environment': environment.name,
    'baseUrl': baseUrl,
    'serverUrl': serverUrl,
    'isDebugMode': isDebugMode,
  };
}

/// Enumeración de entornos disponibles
enum Environment {
  development,
  production,
  local,
}
