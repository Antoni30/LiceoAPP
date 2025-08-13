import '../constants/api_config.dart';
import 'base_service.dart';

class DashboardService extends BaseService {
  // Singleton pattern
  static final DashboardService _instance = DashboardService._internal();
  factory DashboardService() => _instance;
  DashboardService._internal();

  /// Obtiene estadísticas generales del dashboard (para administradores)
  Future<Map<String, dynamic>> getDashboardStats() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/stats');
      return processResponse(response, 'Error al obtener estadísticas del dashboard');
    } catch (e) {
      throw Exception('Error al cargar estadísticas: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas del dashboard específicas para un profesor
  Future<Map<String, dynamic>> getProfesorDashboardStats(String profesorId) async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/profesor/$profesorId');
      return processResponse(response, 'Error al obtener estadísticas del profesor');
    } catch (e) {
      throw Exception('Error al cargar estadísticas del profesor: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas de estudiantes
  Future<Map<String, dynamic>> getEstudiantesStats() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/estudiantes');
      return processResponse(response, 'Error al obtener estadísticas de estudiantes');
    } catch (e) {
      throw Exception('Error al cargar estadísticas de estudiantes: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas de cursos
  Future<Map<String, dynamic>> getCursosStats() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/cursos');
      return processResponse(response, 'Error al obtener estadísticas de cursos');
    } catch (e) {
      throw Exception('Error al cargar estadísticas de cursos: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas de notas y rendimiento académico
  Future<Map<String, dynamic>> getRendimientoAcademico() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/rendimiento');
      return processResponse(response, 'Error al obtener estadísticas de rendimiento');
    } catch (e) {
      throw Exception('Error al cargar estadísticas de rendimiento: ${e.toString()}');
    }
  }

  /// Obtiene actividad reciente del sistema
  Future<List<Map<String, dynamic>>> getActividadReciente(int limit) async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/actividad-reciente?limit=$limit');
      return processListResponse(response, 'Error al obtener actividad reciente');
    } catch (e) {
      throw Exception('Error al cargar actividad reciente: ${e.toString()}');
    }
  }

  /// Obtiene notificaciones para el usuario actual
  Future<List<Map<String, dynamic>>> getNotificaciones() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/notificaciones');
      return processListResponse(response, 'Error al obtener notificaciones');
    } catch (e) {
      throw Exception('Error al cargar notificaciones: ${e.toString()}');
    }
  }

  /// Marca una notificación como leída
  Future<bool> marcarNotificacionLeida(int notificacionId) async {
    try {
      final response = await post('${ApiConfig.dashboardEndpoint}/notificaciones/$notificacionId/leer', {});
      return isSuccessResponse(response);
    } catch (e) {
      throw Exception('Error al marcar notificación como leída: ${e.toString()}');
    }
  }

  /// Obtiene estadísticas por período de tiempo
  Future<Map<String, dynamic>> getEstadisticasPorPeriodo(String periodo, String fechaInicio, String fechaFin) async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/estadisticas-periodo?periodo=$periodo&fechaInicio=$fechaInicio&fechaFin=$fechaFin');
      return processResponse(response, 'Error al obtener estadísticas por período');
    } catch (e) {
      throw Exception('Error al cargar estadísticas por período: ${e.toString()}');
    }
  }

  /// Obtiene el ranking de estudiantes por rendimiento
  Future<List<Map<String, dynamic>>> getRankingEstudiantes(int limit) async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/ranking-estudiantes?limit=$limit');
      return processListResponse(response, 'Error al obtener ranking de estudiantes');
    } catch (e) {
      throw Exception('Error al cargar ranking: ${e.toString()}');
    }
  }

  /// Obtiene alertas del sistema (estudiantes con bajo rendimiento, etc.)
  Future<List<Map<String, dynamic>>> getAlertas() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/alertas');
      return processListResponse(response, 'Error al obtener alertas');
    } catch (e) {
      throw Exception('Error al cargar alertas: ${e.toString()}');
    }
  }

  /// Obtiene resumen de asistencia
  Future<Map<String, dynamic>> getResumenAsistencia() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/asistencia');
      return processResponse(response, 'Error al obtener resumen de asistencia');
    } catch (e) {
      throw Exception('Error al cargar resumen de asistencia: ${e.toString()}');
    }
  }

  /// Genera reporte personalizado
  Future<Map<String, dynamic>> generarReportePersonalizado(Map<String, dynamic> parametros) async {
    try {
      final response = await post('${ApiConfig.dashboardEndpoint}/reporte-personalizado', parametros);
      return processResponse(response, 'Error al generar reporte personalizado');
    } catch (e) {
      throw Exception('Error al generar reporte: ${e.toString()}');
    }
  }

  /// Obtiene métricas de uso del sistema
  Future<Map<String, dynamic>> getMetricasUso() async {
    try {
      final response = await get('${ApiConfig.dashboardEndpoint}/metricas-uso');
      return processResponse(response, 'Error al obtener métricas de uso');
    } catch (e) {
      throw Exception('Error al cargar métricas: ${e.toString()}');
    }
  }
}
