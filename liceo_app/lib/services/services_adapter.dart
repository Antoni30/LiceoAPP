// Adaptador para hacer que los servicios coincidan con lo que esperan las pantallas

import '../models/user_model.dart';
import '../models/academic_models.dart';
import 'user_service.dart' as user_srv;
import 'curso_service.dart' as curso_srv;
import 'materia_service.dart' as materia_srv;
import 'nota_service.dart' as nota_srv;

class UserService {
  static final user_srv.UserService _userService = user_srv.UserService();

  static Future<UserModel> getUser(String userId) async {
    try {
      final userData = await _userService.getUserProfile(userId);
      return UserModel.fromJson(userData);
    } catch (e) {
      throw Exception('Error al obtener usuario: $e');
    }
  }

  static Future<List<UsuarioCurso>> getUserCursos(String userId) async {
    try {
      // Implementación simplificada para el ejemplo
      return [
        UsuarioCurso(idUsuario: userId, idCurso: 1)
      ];
    } catch (e) {
      throw Exception('Error al obtener cursos del usuario: $e');
    }
  }

  static Future<List<Rol>> getUserRoles(String userId) async {
    try {
      // Implementación simplificada
      return [
        Rol(idRol: 3, nombre: 'estudiante')
      ];
    } catch (e) {
      throw Exception('Error al obtener roles del usuario: $e');
    }
  }
}

class CursoService {
  static final curso_srv.CursoService _cursoService = curso_srv.CursoService();

  static Future<Curso> getCurso(String cursoId) async {
    try {
      final cursoData = await _cursoService.getCursoById(int.parse(cursoId));
      return Curso.fromJson(cursoData);
    } catch (e) {
      throw Exception('Error al obtener curso: $e');
    }
  }

  static Future<List<CursoMateria>> getCursoMaterias(String cursoId) async {
    try {
      // Implementación simplificada
      return [
        CursoMateria(idMateria: 1, idCurso: int.parse(cursoId), nombreMateria: 'Matemáticas'),
        CursoMateria(idMateria: 2, idCurso: int.parse(cursoId), nombreMateria: 'Español'),
      ];
    } catch (e) {
      throw Exception('Error al obtener materias del curso: $e');
    }
  }

  static Future<List<UsuarioCurso>> getCursoUsuarios(String cursoId) async {
    try {
      // Implementación simplificada
      return [
        UsuarioCurso(idUsuario: '1', idCurso: int.parse(cursoId)),
        UsuarioCurso(idUsuario: '2', idCurso: int.parse(cursoId)),
      ];
    } catch (e) {
      throw Exception('Error al obtener usuarios del curso: $e');
    }
  }
}

class MateriaService {
  static final materia_srv.MateriaService _materiaService = materia_srv.MateriaService();

  static Future<Materia> getMateria(String materiaId) async {
    try {
      final materiaData = await _materiaService.getMateriaById(int.parse(materiaId));
      return Materia.fromJson(materiaData);
    } catch (e) {
      throw Exception('Error al obtener materia: $e');
    }
  }
}

class NotaService {
  static final nota_srv.NotaService _notaService = nota_srv.NotaService();

  static Future<List<Nota>> getNotasByUsuarioMateria(String userId, String materiaId) async {
    try {
      final notasData = await _notaService.getNotasByUsuarioMateria(int.parse(userId), int.parse(materiaId));
      return notasData.map((nota) => Nota.fromJson(nota)).toList();
    } catch (e) {
      // Retornar lista vacía si no hay notas
      return [];
    }
  }

  static Future<void> updateNota(String notaId, NotaRequest request) async {
    try {
      await _notaService.updateNota(int.parse(notaId), request.toJson());
    } catch (e) {
      throw Exception('Error al actualizar nota: $e');
    }
  }

  static Future<void> createNota(NotaRequest request) async {
    try {
      await _notaService.createNota(request.toJson());
    } catch (e) {
      throw Exception('Error al crear nota: $e');
    }
  }
}