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
  final String? descripcion;

  Materia({
    this.idMateria,
    required this.nombre,
    this.descripcion,
  });

  factory Materia.fromJson(Map<String, dynamic> json) {
    return Materia(
      idMateria: json['idMateria'],
      nombre: json['nombre'] ?? '',
      descripcion: json['descripcion'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idMateria': idMateria,
      'nombre': nombre,
      'descripcion': descripcion,
    };
  }
}

class Curso {
  final int? idCurso;
  final String nombre;
  final String? descripcion;
  final int idAnio;
  final String? nombreAnio;

  Curso({
    this.idCurso,
    required this.nombre,
    this.descripcion,
    required this.idAnio,
    this.nombreAnio,
  });

  factory Curso.fromJson(Map<String, dynamic> json) {
    return Curso(
      idCurso: json['idCurso'],
      nombre: json['nombre'] ?? '',
      descripcion: json['descripcion'],
      idAnio: json['idAnio'] ?? 0,
      nombreAnio: json['nombreAnio'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idCurso': idCurso,
      'nombre': nombre,
      'descripcion': descripcion,
      'idAnio': idAnio,
      'nombreAnio': nombreAnio,
    };
  }
}
