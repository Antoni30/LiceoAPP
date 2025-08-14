class Anio {
  final int? idAnio;
  final String nombre;
  final DateTime fechaInicio;
  final DateTime fechaFin;
  final bool activo;

  Anio({
    this.idAnio,
    required this.nombre,
    required this.fechaInicio,
    required this.fechaFin,
    required this.activo,
  });

  factory Anio.fromJson(Map<String, dynamic> json) {
    return Anio(
      idAnio: json['idAnio'],
      nombre: json['nombre'] ?? '',
      fechaInicio: DateTime.parse(json['fechaInicio'] ?? DateTime.now().toString()),
      fechaFin: DateTime.parse(json['fechaFin'] ?? DateTime.now().toString()),
      activo: json['activo'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idAnio': idAnio,
      'nombre': nombre,
      'fechaInicio': fechaInicio.toIso8601String(),
      'fechaFin': fechaFin.toIso8601String(),
      'activo': activo,
    };
  }
}

class Materia {
  final int? idMateria;
  final String nombre;
  final String nombreMateria;
  final String? descripcion;

  Materia({
    this.idMateria,
    required this.nombre,
    String? nombreMateria,
    this.descripcion,
  }) : nombreMateria = nombreMateria ?? nombre;

  factory Materia.fromJson(Map<String, dynamic> json) {
    return Materia(
      idMateria: json['idMateria'],
      nombre: json['nombre'] ?? '',
      nombreMateria: json['nombreMateria'] ?? json['nombre'] ?? '',
      descripcion: json['descripcion'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idMateria': idMateria,
      'nombre': nombre,
      'nombreMateria': nombreMateria,
      'descripcion': descripcion,
    };
  }
}

class Curso {
  final int? idCurso;
  final String nombre;
  final String nombreCurso;
  final String? descripcion;
  final int idAnio;
  final String? nombreAnio;

  Curso({
    this.idCurso,
    required this.nombre,
    String? nombreCurso,
    this.descripcion,
    required this.idAnio,
    this.nombreAnio,
  }) : nombreCurso = nombreCurso ?? nombre;

  factory Curso.fromJson(Map<String, dynamic> json) {
    return Curso(
      idCurso: json['idCurso'],
      nombre: json['nombre'] ?? '',
      nombreCurso: json['nombreCurso'] ?? json['nombre'] ?? '',
      descripcion: json['descripcion'],
      idAnio: json['idAnio'] ?? 0,
      nombreAnio: json['nombreAnio'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idCurso': idCurso,
      'nombre': nombre,
      'nombreCurso': nombreCurso,
      'descripcion': descripcion,
      'idAnio': idAnio,
      'nombreAnio': nombreAnio,
    };
  }
}

class CursoMateria {
  final int idMateria;
  final int idCurso;
  final String? nombreMateria;

  CursoMateria({
    required this.idMateria,
    required this.idCurso,
    this.nombreMateria,
  });

  factory CursoMateria.fromJson(Map<String, dynamic> json) {
    return CursoMateria(
      idMateria: json['idMateria'] ?? 0,
      idCurso: json['idCurso'] ?? 0,
      nombreMateria: json['nombreMateria'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idMateria': idMateria,
      'idCurso': idCurso,
      'nombreMateria': nombreMateria,
    };
  }
}

class UsuarioCurso {
  final String idUsuario;  // String según el DTO del backend
  final int idCurso;       // Integer según el DTO del backend

  UsuarioCurso({
    required this.idUsuario,
    required this.idCurso,
  });

  factory UsuarioCurso.fromJson(Map<String, dynamic> json) {
    return UsuarioCurso(
      idUsuario: json['idUsuario'] ?? '',
      idCurso: json['idCurso'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idUsuario': idUsuario,
      'idCurso': idCurso,
    };
  }
}

class Nota {
  final int? id;          // Integer según el DTO del backend
  final String idUsuario; // String según el DTO del backend
  final int idMateria;    // Integer según el DTO del backend
  final double nota;      // Double según el DTO del backend
  final int parcial;      // Integer según el DTO del backend
  final DateTime? fechaCreacion;

  Nota({
    this.id,
    required this.idUsuario,
    required this.idMateria,
    required this.nota,
    required this.parcial,
    this.fechaCreacion,
  });

  factory Nota.fromJson(Map<String, dynamic> json) {
    return Nota(
      id: json['id'],
      idUsuario: json['idUsuario'] ?? '',
      idMateria: json['idMateria'] ?? 0,
      nota: (json['nota'] ?? 0).toDouble(),
      parcial: json['parcial'] ?? 1,
      fechaCreacion: json['fechaCreacion'] != null 
          ? DateTime.parse(json['fechaCreacion'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'idUsuario': idUsuario,
      'idMateria': idMateria,
      'nota': nota,
      'parcial': parcial,
      'fechaCreacion': fechaCreacion?.toIso8601String(),
    };
  }
}

class NotaRequest {
  final String idUsuario; // String según el DTO del backend
  final int idMateria;    // Integer según el DTO del backend
  final double nota;      // Double según el DTO del backend
  final int parcial;      // Integer según el DTO del backend

  NotaRequest({
    required this.idUsuario,
    required this.idMateria,
    required this.nota,
    required this.parcial,
  });

  Map<String, dynamic> toJson() {
    return {
      'idUsuario': idUsuario,
      'idMateria': idMateria,
      'nota': nota,
      'parcial': parcial,
    };
  }
}

class Rol {
  final int idRol;
  final String nombre;

  Rol({
    required this.idRol,
    required this.nombre,
  });

  factory Rol.fromJson(Map<String, dynamic> json) {
    return Rol(
      idRol: json['idRol'] ?? 0,
      nombre: json['nombre'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idRol': idRol,
      'nombre': nombre,
    };
  }
}
