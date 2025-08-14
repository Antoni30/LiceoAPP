import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../services/real_api_service.dart';
import '../../widgets/loading_spinner.dart';
import '../../models/user_model.dart';
import '../../models/academic_models.dart';
import '../../constants/app_styles.dart';

class GenerarReporteScreen extends StatefulWidget {
  final String idProfesor;

  const GenerarReporteScreen({
    super.key,
    required this.idProfesor,
  });

  @override
  State<GenerarReporteScreen> createState() => _GenerarReporteScreenState();
}

class _GenerarReporteScreenState extends State<GenerarReporteScreen> {
  UserModel? profesor;
  List<EstudianteReporte> estudiantes = [];
  String? idCurso;
  String? nombreCurso;
  bool cargando = true;
  String? error;
  bool generandoPDF = false;
  Map<int, String> promediosParciales = {1: "S/N", 2: "S/N", 3: "S/N"};
  String promedioGeneral = "S/N";
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

      // Obtener datos del profesor
      final profData = await UserService.getUser(widget.idProfesor);
      profesor = profData;

      // Obtener cursos del profesor
      final cursos = await UserService.getUserCursos(widget.idProfesor);
      if (cursos.isEmpty) {
        throw Exception('Profesor disponible (sin cursos asignados) - Requiere acción administrativa');
      }
      
      final cursoId = cursos.first.idCurso.toString();
      idCurso = cursoId;

      // Obtener nombre del curso
      final nombreCursoResponse = await CursoService.getCurso(cursoId);
      nombreCurso = nombreCursoResponse.nombreCurso;

      // Obtener usuarios del curso
      final usuariosCurso = await CursoService.getCursoUsuarios(cursoId);

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

      final estudiantesFiltrados = <EstudianteReporte>[];
      final notasPorParcial = <int, List<double>>{1: [], 2: [], 3: []};

      for (final usuarioCurso in usuariosCurso) {
        try {
          // Verificar si es estudiante
          final roles = await UserService.getUserRoles(usuarioCurso.idUsuario);
          final tieneRolEstudiante = roles.any((r) => r.idRol == 3);
          if (!tieneRolEstudiante) continue;

          final usuario = await UserService.getUser(usuarioCurso.idUsuario);

          // Obtener todas las notas del estudiante por materia
          final notasEstudiante = <String, Map<int, String>>{};
          for (final materia in materiasDelCurso) {
            try {
              final notasMateria = await NotaService.getNotasByUsuarioMateria(
                usuarioCurso.idUsuario,
                materia.idMateria.toString(),
              );
              
              final parciales = <int, String>{1: "S/N", 2: "S/N", 3: "S/N"};
              
              for (final nota in notasMateria) {
                if (nota.parcial >= 1 && nota.parcial <= 3) {
                  parciales[nota.parcial] = nota.nota.toString();
                }
              }
              
              notasEstudiante[materia.idMateria.toString()] = parciales;
            } catch (err) {
              notasEstudiante[materia.idMateria.toString()] = {1: "S/N", 2: "S/N", 3: "S/N"};
            }
          }

          // Calcular promedios por parcial del estudiante
          final notasParciales = <int, String>{1: "S/N", 2: "S/N", 3: "S/N"};
          
          for (int parcial = 1; parcial <= 3; parcial++) {
            final notasDelParcial = <double>[];
            
            for (final materia in materiasDelCurso) {
              final nota = notasEstudiante[materia.idMateria.toString()]?[parcial] ?? "S/N";
              if (nota != "S/N") {
                final notaValue = double.tryParse(nota);
                if (notaValue != null) {
                  notasDelParcial.add(notaValue);
                }
              }
            }
            
            if (notasDelParcial.isNotEmpty) {
              final suma = notasDelParcial.reduce((a, b) => a + b);
              final promedio = suma / notasDelParcial.length;
              notasParciales[parcial] = promedio.toStringAsFixed(2);
              notasPorParcial[parcial]?.add(promedio);
            } else {
              notasPorParcial[parcial]?.add(0);
            }
          }

          // Calcular promedio general del estudiante
          final notasConvertidas = [
            notasParciales[1],
            notasParciales[2],
            notasParciales[3]
          ].where((n) => n != "S/N")
           .map((n) => double.tryParse(n ?? "S/N") ?? 0.0)
           .toList();

          String promedioEstudiante = "S/N";
          if (notasConvertidas.isNotEmpty) {
            final suma = notasConvertidas.reduce((a, b) => a + b);
            promedioEstudiante = (suma / notasConvertidas.length).toStringAsFixed(2);
          }

          estudiantesFiltrados.add(EstudianteReporte(
            idUsuario: usuarioCurso.idUsuario,
            nombres: usuario.nombres,
            apellidos: usuario.apellidos,
            nota1: notasParciales[1] ?? "S/N",
            nota2: notasParciales[2] ?? "S/N",
            nota3: notasParciales[3] ?? "S/N",
            promedio: promedioEstudiante,
            notasPorMateria: notasEstudiante,
          ));
        } catch (userErr) {
          continue;
        }
      }

      // Calcular promedios por parcial del curso
      final promediosParcialCalculados = <int, String>{};
      for (int parcial = 1; parcial <= 3; parcial++) {
        final notasDelParcial = notasPorParcial[parcial]?.where((n) => n > 0).toList() ?? [];
        if (notasDelParcial.isNotEmpty) {
          final suma = notasDelParcial.reduce((a, b) => a + b);
          promediosParcialCalculados[parcial] = (suma / notasDelParcial.length).toStringAsFixed(2);
        } else {
          promediosParcialCalculados[parcial] = "S/N";
        }
      }

      // Calcular promedio general del curso
      final promediosEstudiantes = estudiantesFiltrados
          .where((e) => e.promedio != "S/N")
          .map((e) => double.tryParse(e.promedio) ?? 0.0)
          .where((p) => p > 0)
          .toList();

      String promedioGeneralCalculado = "S/N";
      if (promediosEstudiantes.isNotEmpty) {
        final suma = promediosEstudiantes.reduce((a, b) => a + b);
        promedioGeneralCalculado = (suma / promediosEstudiantes.length).toStringAsFixed(2);
      }

      setState(() {
        estudiantes = estudiantesFiltrados;
        promediosParciales = promediosParcialCalculados;
        promedioGeneral = promedioGeneralCalculado;
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

  Future<void> _generarPDF() async {
    setState(() {
      generandoPDF = true;
    });

    try {
      // Aquí iría la lógica para generar el PDF
      // Por ahora mostramos un mensaje de éxito
      await Future.delayed(const Duration(seconds: 2));
      
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('PDF generado exitosamente (funcionalidad en desarrollo)'),
            backgroundColor: Colors.green,
          ),
        );
      }
    } catch (err) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error al generar PDF: $err'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          generandoPDF = false;
        });
      }
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
          title: const Text('Generar Reporte'),
        ),
        body: const LoadingSpinner(),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Generar Reporte'),
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
        title: Text('Generar Reporte - ${nombreCurso ?? ''}'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => context.pop(),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(AppStyles.spacing4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header del profesor
            Card(
              color: AppColors.primary,
              child: Padding(
                padding: const EdgeInsets.all(AppStyles.spacing4),
                child: Row(
                  children: [
                    CircleAvatar(
                      radius: 30,
                      backgroundColor: Colors.white,
                      child: Text(
                        '${profesor?.nombres[0] ?? ''}${profesor?.apellidos[0] ?? ''}',
                        style: TextStyle(
                          color: AppColors.primary,
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                    ),
                    const SizedBox(width: AppStyles.spacing4),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Generar Reporte - ${nombreCurso ?? ''}',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          Text(
                            'Profesor: ${profesor?.nombres ?? ''} ${profesor?.apellidos ?? ''}',
                            style: const TextStyle(
                              color: Colors.white70,
                              fontSize: 14,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: AppStyles.spacing4),

            // Resumen del curso
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
                    const SizedBox(height: AppStyles.spacing4),
                    
                    // Promedios por parcial
                    Row(
                      children: [
                        ...List.generate(3, (index) {
                          final parcial = index + 1;
                          final promedio = promediosParciales[parcial] ?? "S/N";
                          return Expanded(
                            child: Container(
                              margin: EdgeInsets.only(
                                right: index < 2 ? AppStyles.spacing2 : 0,
                              ),
                              padding: const EdgeInsets.all(AppStyles.spacing3),
                              decoration: BoxDecoration(
                                color: _getNotaColor(promedio).withOpacity(0.1),
                                borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                                border: Border.all(
                                  color: _getNotaColor(promedio).withOpacity(0.3),
                                ),
                              ),
                              child: Column(
                                children: [
                                  Text(
                                    'Parcial $parcial',
                                    style: const TextStyle(fontSize: 12),
                                  ),
                                  const SizedBox(height: AppStyles.spacing1),
                                  Text(
                                    promedio,
                                    style: TextStyle(
                                      fontSize: 18,
                                      fontWeight: FontWeight.bold,
                                      color: _getNotaColor(promedio),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          );
                        }),
                        const SizedBox(width: AppStyles.spacing2),
                        Expanded(
                          child: Container(
                            padding: const EdgeInsets.all(AppStyles.spacing3),
                            decoration: BoxDecoration(
                              color: _getNotaColor(promedioGeneral).withOpacity(0.1),
                              borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                              border: Border.all(
                                color: _getNotaColor(promedioGeneral).withOpacity(0.3),
                              ),
                            ),
                            child: Column(
                              children: [
                                const Text(
                                  'Promedio General',
                                  style: TextStyle(fontSize: 12),
                                ),
                                const SizedBox(height: AppStyles.spacing1),
                                Text(
                                  promedioGeneral,
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                    color: _getNotaColor(promedioGeneral),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                    
                    const SizedBox(height: AppStyles.spacing4),
                    
                    // Estadísticas
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          'Total de estudiantes: ${estudiantes.length}',
                          style: Theme.of(context).textTheme.bodyLarge,
                        ),
                        ElevatedButton.icon(
                          onPressed: generandoPDF ? null : _generarPDF,
                          icon: generandoPDF
                              ? const SizedBox(
                                  width: 16,
                                  height: 16,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                    color: Colors.white,
                                  ),
                                )
                              : const Icon(Icons.picture_as_pdf),
                          label: Text(generandoPDF ? 'Generando...' : 'Descargar PDF'),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.green,
                            foregroundColor: Colors.white,
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
              Card(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(AppStyles.spacing4),
                      child: Text(
                        'Detalle por Estudiante',
                        style: Theme.of(context).textTheme.titleLarge?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    SingleChildScrollView(
                      scrollDirection: Axis.horizontal,
                      child: DataTable(
                        columns: const [
                          DataColumn(label: Text('Estudiante')),
                          DataColumn(label: Text('P1')),
                          DataColumn(label: Text('P2')),
                          DataColumn(label: Text('P3')),
                          DataColumn(label: Text('Promedio')),
                        ],
                        rows: estudiantes.map((estudiante) {
                          return DataRow(
                            cells: [
                              DataCell(
                                SizedBox(
                                  width: 150,
                                  child: Text(
                                    '${estudiante.nombres} ${estudiante.apellidos}',
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                ),
                              ),
                              DataCell(
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: AppStyles.spacing2,
                                    vertical: AppStyles.spacing1,
                                  ),
                                  decoration: BoxDecoration(
                                    color: _getNotaColor(estudiante.nota1).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                                  ),
                                  child: Text(
                                    estudiante.nota1,
                                    style: TextStyle(
                                      color: _getNotaColor(estudiante.nota1),
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                              DataCell(
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: AppStyles.spacing2,
                                    vertical: AppStyles.spacing1,
                                  ),
                                  decoration: BoxDecoration(
                                    color: _getNotaColor(estudiante.nota2).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                                  ),
                                  child: Text(
                                    estudiante.nota2,
                                    style: TextStyle(
                                      color: _getNotaColor(estudiante.nota2),
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                              DataCell(
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: AppStyles.spacing2,
                                    vertical: AppStyles.spacing1,
                                  ),
                                  decoration: BoxDecoration(
                                    color: _getNotaColor(estudiante.nota3).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                                  ),
                                  child: Text(
                                    estudiante.nota3,
                                    style: TextStyle(
                                      color: _getNotaColor(estudiante.nota3),
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                              DataCell(
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: AppStyles.spacing2,
                                    vertical: AppStyles.spacing1,
                                  ),
                                  decoration: BoxDecoration(
                                    color: _getNotaColor(estudiante.promedio).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                                    border: Border.all(
                                      color: _getNotaColor(estudiante.promedio),
                                    ),
                                  ),
                                  child: Text(
                                    estudiante.promedio,
                                    style: TextStyle(
                                      color: _getNotaColor(estudiante.promedio),
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          );
                        }).toList(),
                      ),
                    ),
                  ],
                ),
              ),
          ],
        ),
      ),
    );
  }
}

class EstudianteReporte {
  final String idUsuario; // String según el DTO del backend
  final String nombres;
  final String apellidos;
  final String nota1;
  final String nota2;
  final String nota3;
  final String promedio;
  final Map<String, Map<int, String>> notasPorMateria;

  EstudianteReporte({
    required this.idUsuario,
    required this.nombres,
    required this.apellidos,
    required this.nota1,
    required this.nota2,
    required this.nota3,
    required this.promedio,
    required this.notasPorMateria,
  });
}