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
