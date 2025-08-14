import 'package:flutter/material.dart';
import '../models/user_model.dart';
import '../services/auth_service.dart';

class AuthProvider with ChangeNotifier {
  final AuthService _authService = AuthService();
  
  User? _currentUser;
  String? _userRole;
  bool _isLoading = false;
  String? _errorMessage;

  User? get currentUser => _currentUser;
  String? get userRole => _userRole;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;
  bool get isLoggedIn => _currentUser != null;
  bool get isAdmin => _userRole == 'administrador';
  bool get isProfesor => _userRole == 'profesor';
  bool get isEstudiante => _userRole == 'estudiante';

  AuthProvider() {
    _loadUserFromStorage();
  }

  Future<void> _loadUserFromStorage() async {
    _setLoading(true);
    try {
      _currentUser = await _authService.getCurrentUser();
      _userRole = await _authService.getCurrentUserRole();
      notifyListeners();
    } finally {
      _setLoading(false);
    }
  }

  Future<Map<String, dynamic>> login(String idUsuario, String contrasena) async {
    _setLoading(true);
    _clearError();

    try {
      final result = await _authService.login(idUsuario, contrasena);
      
      print('🔍 [AuthProvider] Resultado de AuthService: $result');
      
      if (result['requiresVerification'] == true) {
        print('✅ [AuthProvider] Retornando resultado de MFA');
        return result;
      }

      if (result['success'] == true) {
        _currentUser = result['user'];
        _userRole = result['userRole'];
        notifyListeners();
      }

      return result;
    } catch (e) {
      print('❌ [AuthProvider] Error en login: $e');
      _setError(e.toString());
      rethrow;
    } finally {
      _setLoading(false);
    }
  }

  Future<bool> verifyCode(String idUsuario, String code) async {
    _setLoading(true);
    _clearError();

    try {
      final success = await _authService.verifyCode(idUsuario, code);
      if (success) {
        // Después de verificar, hacer login automáticamente
        await _loadUserFromStorage();
      }
      return success;
    } catch (e) {
      _setError(e.toString());
      return false;
    } finally {
      _setLoading(false);
    }
  }

  Future<void> logout() async {
    _setLoading(true);
    try {
      await _authService.logout();
      _currentUser = null;
      _userRole = null;
      notifyListeners();
    } finally {
      _setLoading(false);
    }
  }

  void _setLoading(bool loading) {
    _isLoading = loading;
    notifyListeners();
  }

  void _setError(String error) {
    _errorMessage = error;
    notifyListeners();
  }

  void _clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  bool hasRole(String role) {
    return _currentUser?.hasRole(role) ?? false;
  }

  String get displayName {
    return _currentUser?.fullName ?? 'Usuario';
  }

  String? get userId {
    print('🔍 [AuthProvider] Obteniendo userId...');
    print('🔍 [AuthProvider] _currentUser disponible: ${_currentUser != null}');
    if (_currentUser != null) {
      print('🔍 [AuthProvider] _currentUser.idUsuario: ${_currentUser!.idUsuario}');
    }
    return _currentUser?.idUsuario;
  }

  String get roleName {
    switch (_userRole?.toLowerCase()) {
      case 'administrador':
        return 'Administrador';
      case 'profesor':
        return 'Profesor';
      case 'estudiante':
        return 'Estudiante';
      default:
        return 'Usuario';
    }
  }
}
