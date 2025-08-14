class UserModel {
  final String idUsuario; // String según el DTO del backend
  final String nombres;
  final String apellidos;
  final String? email;
  final String? telefono;
  final List<String> roles;

  UserModel({
    required this.idUsuario,
    required this.nombres,
    required this.apellidos,
    this.email,
    this.telefono,
    required this.roles,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      idUsuario: json['idUsuario'] ?? '',
      nombres: json['nombres'] ?? '',
      apellidos: json['apellidos'] ?? '',
      email: json['email'],
      telefono: json['telefono'],
      roles: List<String>.from(json['roles'] ?? []),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idUsuario': idUsuario,
      'nombres': nombres,
      'apellidos': apellidos,
      'email': email,
      'telefono': telefono,
      'roles': roles,
    };
  }

  String get fullName => '$nombres $apellidos';
  
  bool hasRole(String role) => roles.contains(role);
  
  bool get isAdmin => hasRole('administrador');
  bool get isProfesor => hasRole('profesor');
}

class User {
  final String idUsuario;
  final String nombre;
  final String apellido;
  final String email;
  final String? telefono;
  final List<String> roles;

  User({
    required this.idUsuario,
    required this.nombre,
    required this.apellido,
    required this.email,
    this.telefono,
    required this.roles,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      idUsuario: json['idUsuario'] ?? '',
      nombre: json['nombre'] ?? '',
      apellido: json['apellido'] ?? '',
      email: json['email'] ?? '',
      telefono: json['telefono'],
      roles: List<String>.from(json['roles'] ?? []),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idUsuario': idUsuario,
      'nombre': nombre,
      'apellido': apellido,
      'email': email,
      'telefono': telefono,
      'roles': roles,
    };
  }

  String get fullName => '$nombre $apellido';
  
  bool hasRole(String role) => roles.contains(role);
  
  bool get isAdmin => hasRole('administrador');
  bool get isProfesor => hasRole('profesor');
}
