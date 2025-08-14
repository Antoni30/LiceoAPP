import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../../services/real_api_service.dart';
import '../../widgets/loading_spinner.dart';
import '../../models/user_model.dart';
import '../../models/academic_models.dart';
import '../../constants/app_styles.dart';
import '../../providers/auth_provider.dart';

class EstudiantesScreen extends StatefulWidget {
  const EstudiantesScreen({super.key});

  @override
  State<EstudiantesScreen> createState() => _EstudiantesScreenState();
}

class _EstudiantesScreenState extends State<EstudiantesScreen> {
  List<EstudianteConNotas> estudiantes = [];
  String? idCurso;
  String? nombreCurso;
  bool cargando = true;
  String? error;
  Map<String, String> materiasNombres = {};

  @override
  void initState() {
    super.initState();
    _fetchData();
  }

  Future<void> _fetchData() async {
    try {
      setState(() {
        cargando = true;
        error = null;
      });

      final authProvider = context.read<AuthProvider>();
      final profesorId = authProvider.userId;
      
      print('🔍 [EstudiantesScreen] AuthProvider userId: $profesorId');
      print('🔍 [EstudiantesScreen] AuthProvider userRole: ${authProvider.userRole}');
      print('🔍 [EstudiantesScreen] AuthProvider isLoggedIn: ${authProvider.isLoggedIn}');
      
      if (profesorId == null) {
        throw Exception('No se pudo obtener el ID del profesor. Por favor, inicie sesión nuevamente.');
      }

      // Obtener cursos del profesor
      print('🔍 [EstudiantesScreen] Llamando UserService.getUserCursos con ID: $profesorId');
      final cursos = await UserService.getUserCursos(profesorId);
      if (cursos.isEmpty) {
        throw Exception('No tienes cursos asignados');
      }

      final cursoId = cursos.first.idCurso.toString();
      idCurso = cursoId;

      // Obtener información del curso
      final curso = await CursoService.getCurso(cursoId);
      nombreCurso = curso.nombreCurso;

      // Obtener materias del curso
      final materiasDelCurso = await CursoService.getCursoMaterias(cursoId);

      // Obtener nombres de materias
      final nombresMaterias = <String, String>{};
      for (final materia in materiasDelCurso) {
        try {
          final materiaDetalle = await MateriaService.getMateria(materia.idMateria.toString());
          nombresMaterias[materia.idMateria.toString()] = materiaDetalle.nombreMateria;
        } catch (err) {
          nombresMaterias[materia.idMateria.toString()] = 'Materia ID: ${materia.idMateria}';
        }
      }
      materiasNombres = nombresMaterias;

      // Obtener usuarios del curso
      final usuariosCurso = await CursoService.getCursoUsuarios(cursoId);

      final estudiantesList = <EstudianteConNotas>[];

      for (final usuarioCurso in usuariosCurso) {
        try {
          // Verificar si es estudiante
          final roles = await UserService.getUserRoles(usuarioCurso.idUsuario);
          final tieneRolEstudiante = roles.any((r) => r.idRol == 3);
          if (!tieneRolEstudiante) continue;

          final usuario = await UserService.getUser(usuarioCurso.idUsuario);

          // Calcular promedios por parcial del estudiante
          final notasParciales = <int, String>{1: "S/N", 2: "S/N", 3: "S/N"};
          
          for (int parcial = 1; parcial <= 3; parcial++) {
            final notasDelParcial = <double>[];
            
            for (final materia in materiasDelCurso) {
              try {
                final notasMateria = await NotaService.getNotasByUsuarioMateria(
                  usuarioCurso.idUsuario,
                  materia.idMateria.toString(),
                );
                
                final notaParcial = notasMateria
                    .where((n) => n.parcial == parcial)
                    .firstOrNull;
                    
                if (notaParcial != null) {
                  notasDelParcial.add(notaParcial.nota);
                }
              } catch (err) {
                // Si no hay nota, no la agregamos al cálculo
              }
            }
            
            if (notasDelParcial.isNotEmpty) {
              final suma = notasDelParcial.reduce((a, b) => a + b);
              final promedio = suma / notasDelParcial.length;
              notasParciales[parcial] = promedio.toStringAsFixed(2);
            }
          }

          // Calcular promedio general del estudiante
          final notasConvertidas = [
            notasParciales[1],
            notasParciales[2],
            notasParciales[3]
          ].where((n) => n != "S/N")
           .map((n) => double.tryParse(n ?? "S/N") ?? 0.0)
           .where((n) => n > 0)
           .toList();

          String promedioEstudiante = "S/N";
          if (notasConvertidas.isNotEmpty) {
            final suma = notasConvertidas.reduce((a, b) => a + b);
            promedioEstudiante = (suma / notasConvertidas.length).toStringAsFixed(2);
          }

          estudiantesList.add(EstudianteConNotas(
            usuario: usuario,
            nota1: notasParciales[1] ?? "S/N",
            nota2: notasParciales[2] ?? "S/N",
            nota3: notasParciales[3] ?? "S/N",
            promedio: promedioEstudiante,
          ));
        } catch (userErr) {
          continue;
        }
      }

      setState(() {
        estudiantes = estudiantesList;
      });
    } catch (err) {
      setState(() {
        error = err.toString();
      });
    } finally {
      setState(() {
        cargando = false;
      });
    }
  }

  Color _getNotaColor(String nota) {
    if (nota == "S/N") return Colors.grey;
    final notaValue = double.tryParse(nota);
    if (notaValue == null) return Colors.grey;
    return notaValue >= 14 ? Colors.green : Colors.red;
  }

  @override
  Widget build(BuildContext context) {
    if (cargando) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Gestión de Estudiantes'),
        ),
        body: const LoadingSpinner(),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Gestión de Estudiantes'),
        ),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(AppStyles.spacing4),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.error_outline,
                  size: 64,
                  color: Colors.red[400],
                ),
                const SizedBox(height: AppStyles.spacing4),
                Text(
                  'Error',
                  style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    color: Colors.red[700],
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: AppStyles.spacing2),
                Text(
                  error!,
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: AppStyles.spacing4),
                ElevatedButton(
                  onPressed: _fetchData,
                  child: const Text('Reintentar'),
                ),
              ],
            ),
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Text('Estudiantes - ${nombreCurso ?? ''}'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => context.pop(),
        ),
      ),
      body: RefreshIndicator(
        onRefresh: _fetchData,
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.all(AppStyles.spacing4),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Header con estadísticas
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(AppStyles.spacing4),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Resumen del Curso',
                        style: Theme.of(context).textTheme.titleLarge?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: AppStyles.spacing3),
                      Row(
                        children: [
                          Expanded(
                            child: Container(
                              padding: const EdgeInsets.all(AppStyles.spacing3),
                              decoration: BoxDecoration(
                                color: AppColors.primary.withOpacity(0.1),
                                borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                              ),
                              child: Column(
                                children: [
                                  Text(
                                    'Total Estudiantes',
                                    style: TextStyle(
                                      fontSize: 12,
                                      color: AppColors.primary,
                                    ),
                                  ),
                                  const SizedBox(height: AppStyles.spacing1),
                                  Text(
                                    '${estudiantes.length}',
                                    style: TextStyle(
                                      fontSize: 24,
                                      fontWeight: FontWeight.bold,
                                      color: AppColors.primary,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                          const SizedBox(width: AppStyles.spacing3),
                          Expanded(
                            child: Container(
                              padding: const EdgeInsets.all(AppStyles.spacing3),
                              decoration: BoxDecoration(
                                color: Colors.green.withOpacity(0.1),
                                borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                              ),
                              child: Column(
                                children: [
                                  const Text(
                                    'Aprobados',
                                    style: TextStyle(
                                      fontSize: 12,
                                      color: Colors.green,
                                    ),
                                  ),
                                  const SizedBox(height: AppStyles.spacing1),
                                  Text(
                                    '${estudiantes.where((e) => e.promedio != "S/N" && double.parse(e.promedio) >= 14).length}',
                                    style: const TextStyle(
                                      fontSize: 24,
                                      fontWeight: FontWeight.bold,
                                      color: Colors.green,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: AppStyles.spacing4),

              // Lista de estudiantes
              if (estudiantes.isEmpty)
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(AppStyles.spacing6),
                    child: Center(
                      child: Column(
                        children: [
                          Icon(
                            Icons.people_outline,
                            size: 64,
                            color: Colors.grey[400],
                          ),
                          const SizedBox(height: AppStyles.spacing4),
                          Text(
                            'No hay estudiantes',
                            style: Theme.of(context).textTheme.titleLarge,
                          ),
                          const SizedBox(height: AppStyles.spacing2),
                          Text(
                            'Este curso no tiene estudiantes matriculados',
                            style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                              color: Colors.grey[600],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                )
              else
                ListView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemCount: estudiantes.length,
                  itemBuilder: (context, index) {
                    final estudiante = estudiantes[index];
                    return Card(
                      margin: const EdgeInsets.only(bottom: AppStyles.spacing3),
                      child: Padding(
                          padding: const EdgeInsets.all(AppStyles.spacing4),
                          child: Column(
                            children: [
                              Row(
                                children: [
                                  CircleAvatar(
                                    radius: 25,
                                    backgroundColor: AppColors.primary,
                                    child: Text(
                                      '${estudiante.usuario.nombres[0]}${estudiante.usuario.apellidos[0]}',
                                      style: const TextStyle(
                                        color: Colors.white,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                  const SizedBox(width: AppStyles.spacing3),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          '${estudiante.usuario.nombres} ${estudiante.usuario.apellidos}',
                                          style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                            fontWeight: FontWeight.bold,
                                          ),
                                        ),
                                        Text(
                                          'ID: ${estudiante.usuario.idUsuario}',
                                          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                            color: Colors.grey[600],
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: AppStyles.spacing3,
                                      vertical: AppStyles.spacing1,
                                    ),
                                    decoration: BoxDecoration(
                                      color: _getNotaColor(estudiante.promedio).withOpacity(0.1),
                                      borderRadius: BorderRadius.circular(AppStyles.radiusLg),
                                      border: Border.all(
                                        color: _getNotaColor(estudiante.promedio),
                                      ),
                                    ),
                                    child: Column(
                                      children: [
                                        const Text(
                                          'Promedio',
                                          style: TextStyle(fontSize: 10),
                                        ),
                                        Text(
                                          estudiante.promedio,
                                          style: TextStyle(
                                            fontSize: 16,
                                            fontWeight: FontWeight.bold,
                                            color: _getNotaColor(estudiante.promedio),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: AppStyles.spacing3),
                              const Divider(),
                              const SizedBox(height: AppStyles.spacing2),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceAround,
                                children: [
                                  _buildNotaParcial('P1', estudiante.nota1),
                                  _buildNotaParcial('P2', estudiante.nota2),
                                  _buildNotaParcial('P3', estudiante.nota3),
                                ],
                              ),
                              const SizedBox(height: AppStyles.spacing3),
                              Row(
                                children: [
                                  Expanded(
                                    child: OutlinedButton.icon(
                                      onPressed: () {
                                        context.push(
                                          '/profesor/detalle-estudiante/${estudiante.usuario.idUsuario}/$idCurso',
                                        );
                                      },
                                      icon: const Icon(Icons.visibility, size: 16),
                                      label: const Text('Ver Detalle'),
                                      style: OutlinedButton.styleFrom(
                                        foregroundColor: AppColors.primary,
                                      ),
                                    ),
                                  ),
                                  const SizedBox(width: AppStyles.spacing2),
                                  Expanded(
                                    child: ElevatedButton.icon(
                                      onPressed: () {
                                        context.push(
                                          '/profesor/asignar-notas/${estudiante.usuario.idUsuario}/$idCurso',
                                        );
                                      },
                                      icon: const Icon(Icons.edit, size: 16),
                                      label: const Text('Asignar Notas'),
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: AppColors.primary,
                                        foregroundColor: Colors.white,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                    );
                  },
                ),
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          if (idCurso != null) {
            final authProvider = context.read<AuthProvider>();
            final profesorId = authProvider.userId ?? '1';
            context.push('/profesor/generar-reporte/$profesorId');
          }
        },
        icon: const Icon(Icons.assessment),
        label: const Text('Generar Reporte'),
      ),
    );
  }

  Widget _buildNotaParcial(String label, String nota) {
    return Column(
      children: [
        Text(
          label,
          style: const TextStyle(
            fontSize: 12,
            fontWeight: FontWeight.w500,
          ),
        ),
        const SizedBox(height: AppStyles.spacing1),
        Container(
          padding: const EdgeInsets.symmetric(
            horizontal: AppStyles.spacing2,
            vertical: AppStyles.spacing1,
          ),
          decoration: BoxDecoration(
            color: _getNotaColor(nota).withOpacity(0.1),
            borderRadius: BorderRadius.circular(AppStyles.radiusSm),
            border: Border.all(
              color: _getNotaColor(nota).withOpacity(0.3),
            ),
          ),
          child: Text(
            nota,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: _getNotaColor(nota),
            ),
          ),
        ),
      ],
    );
  }
}

class EstudianteConNotas {
  final UserModel usuario;
  final String nota1;
  final String nota2;
  final String nota3;
  final String promedio;

  EstudianteConNotas({
    required this.usuario,
    required this.nota1,
    required this.nota2,
    required this.nota3,
    required this.promedio,
  });
}