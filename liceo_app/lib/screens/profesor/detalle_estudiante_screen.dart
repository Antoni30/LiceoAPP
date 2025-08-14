import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../services/real_api_service.dart';
import '../../widgets/loading_spinner.dart';
import '../../models/user_model.dart';
import '../../models/academic_models.dart';
import '../../constants/app_styles.dart';

class DetalleEstudianteScreen extends StatefulWidget {
  final String idUsuario;
  final String idCurso;

  const DetalleEstudianteScreen({super.key, required this.idUsuario, required this.idCurso});

  @override
  State<DetalleEstudianteScreen> createState() => _DetalleEstudianteScreenState();
}

class _DetalleEstudianteScreenState extends State<DetalleEstudianteScreen> {
  UserModel? estudiante;
  List<MateriaConNotas> materiasConNotas = [];
  Curso? curso;
  bool cargando = true;
  String? error;

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

      final estudianteData = await UserService.getUser(widget.idUsuario);
      estudiante = estudianteData;

      final cursoData = await CursoService.getCurso(widget.idCurso);
      curso = cursoData;

      final materiasDelCurso = await CursoService.getCursoMaterias(widget.idCurso);
      final materiasConNotasList = <MateriaConNotas>[];

      for (final cursoMateria in materiasDelCurso) {
        try {
          final materia = await MateriaService.getMateria(cursoMateria.idMateria.toString());
          final notasMateria = await NotaService.getNotasByUsuarioMateria(
            widget.idUsuario,
            cursoMateria.idMateria.toString(),
          );

          final notasPorParcial = <int, Nota?>{1: null, 2: null, 3: null};
          for (final nota in notasMateria) {
            if (nota.parcial >= 1 && nota.parcial <= 3) {
              notasPorParcial[nota.parcial] = nota;
            }
          }

          final notasValidas = notasPorParcial.values
              .where((nota) => nota != null)
              .map((nota) => nota!.nota)
              .toList();

          String promedio = "S/N";
          if (notasValidas.isNotEmpty) {
            final suma = notasValidas.reduce((a, b) => a + b);
            promedio = (suma / notasValidas.length).toStringAsFixed(2);
          }

          materiasConNotasList.add(
            MateriaConNotas(
              materia: materia,
              notaParcial1: notasPorParcial[1],
              notaParcial2: notasPorParcial[2],
              notaParcial3: notasPorParcial[3],
              promedio: promedio,
            ),
          );
        } catch (_) {
          materiasConNotasList.add(
            MateriaConNotas(
              materia: Materia(
                idMateria: cursoMateria.idMateria,
                nombre: 'Materia ID: ${cursoMateria.idMateria}',
                nombreMateria: 'Materia ID: ${cursoMateria.idMateria}',
              ),
              notaParcial1: null,
              notaParcial2: null,
              notaParcial3: null,
              promedio: "S/N",
            ),
          );
        }
      }

      setState(() {
        materiasConNotas = materiasConNotasList;
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

  String _calcularPromedioGeneral() {
    final promedios = materiasConNotas
        .where((m) => m.promedio != "S/N")
        .map((m) => double.tryParse(m.promedio) ?? 0.0)
        .where((p) => p > 0)
        .toList();

    if (promedios.isEmpty) return "S/N";
    final suma = promedios.reduce((a, b) => a + b);
    return (suma / promedios.length).toStringAsFixed(2);
  }

  Color _getNotaColor(String? nota) {
    if (nota == null || nota == "S/N") return Colors.grey;
    final notaValue = double.tryParse(nota);
    if (notaValue == null) return Colors.grey;
    return notaValue >= 14 ? Colors.green : Colors.red;
  }

  String _getNotaDisplay(Nota? nota) {
    return nota?.nota.toString() ?? "S/N";
  }

  @override
  Widget build(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    final isMobile = width < 600; // breakpoint simple
    final isTablet = width >= 600 && width < 900;

    if (cargando) {
      return Scaffold(
        appBar: AppBar(title: const Text('Detalle del Estudiante')),
        body: const LoadingSpinner(),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Detalle del Estudiante')),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(AppStyles.spacing4),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.error_outline, size: 64, color: Colors.red[400]),
                const SizedBox(height: AppStyles.spacing4),
                Text(
                  'Error',
                  style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    color: Colors.red[700],
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: AppStyles.spacing2),
                Text(error!, textAlign: TextAlign.center),
                const SizedBox(height: AppStyles.spacing4),
                ElevatedButton(onPressed: _fetchData, child: const Text('Reintentar')),
              ],
            ),
          ),
        ),
      );
    }

    final promedioGeneral = _calcularPromedioGeneral();
    final promedioColor = _getNotaColor(promedioGeneral);

    return Scaffold(
      appBar: AppBar(
        title: Text('${estudiante?.nombres ?? ''} ${estudiante?.apellidos ?? ''}'),
        leading: IconButton(icon: const Icon(Icons.arrow_back), onPressed: () => context.pop()),
        actions: [
          IconButton(
            onPressed: () {
              context.push('/profesor/asignar-notas/${widget.idUsuario}/${widget.idCurso}');
            },
            icon: const Icon(Icons.edit),
            tooltip: 'Asignar Notas',
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(
          horizontal: isMobile ? AppStyles.spacing3 : AppStyles.spacing4,
          vertical: AppStyles.spacing4,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header/Info del estudiante (responsivo)
            if (estudiante != null) ...[
              Card(
                elevation: 0.5,
                child: Padding(
                  padding: const EdgeInsets.all(AppStyles.spacing4),
                  child: isMobile
                      ? Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                CircleAvatar(
                                  radius: 30,
                                  backgroundColor: AppColors.primary,
                                  child: Text(
                                    '${(estudiante!.nombres.isNotEmpty ? estudiante!.nombres[0] : '?')}${(estudiante!.apellidos.isNotEmpty ? estudiante!.apellidos[0] : '?')}',
                                    style: const TextStyle(
                                      color: Colors.white,
                                      fontWeight: FontWeight.bold,
                                      fontSize: 18,
                                    ),
                                  ),
                                ),
                                const SizedBox(width: AppStyles.spacing3),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        '${estudiante!.nombres} ${estudiante!.apellidos}',
                                        style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                          fontWeight: FontWeight.bold,
                                        ),
                                        maxLines: 2,
                                        overflow: TextOverflow.ellipsis,
                                      ),
                                      const SizedBox(height: AppStyles.spacing1),
                                      Text('ID: ${estudiante!.idUsuario}'),
                                      Text('Curso: ${curso?.nombreCurso ?? 'N/A'}'),
                                      if (estudiante!.email != null)
                                        Text('Email: ${estudiante!.email}'),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: AppStyles.spacing3),
                            Align(
                              alignment: Alignment.centerLeft,
                              child: _PromedioBadge(
                                titulo: 'Promedio General',
                                promedio: promedioGeneral,
                                color: promedioColor,
                              ),
                            ),
                          ],
                        )
                      : Row(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            CircleAvatar(
                              radius: 35,
                              backgroundColor: AppColors.primary,
                              child: Text(
                                '${(estudiante!.nombres.isNotEmpty ? estudiante!.nombres[0] : '?')}${(estudiante!.apellidos.isNotEmpty ? estudiante!.apellidos[0] : '?')}',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 20,
                                ),
                              ),
                            ),
                            const SizedBox(width: AppStyles.spacing4),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    '${estudiante!.nombres} ${estudiante!.apellidos}',
                                    style: Theme.of(
                                      context,
                                    ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(height: AppStyles.spacing1),
                                  Text('ID: ${estudiante!.idUsuario}'),
                                  Text('Curso: ${curso?.nombreCurso ?? 'N/A'}'),
                                  if (estudiante!.email != null)
                                    Text('Email: ${estudiante!.email}'),
                                ],
                              ),
                            ),
                            _PromedioBadge(
                              titulo: 'Promedio General',
                              promedio: promedioGeneral,
                              color: promedioColor,
                            ),
                          ],
                        ),
                ),
              ),
              const SizedBox(height: AppStyles.spacing4),
            ],

            // Banner informativo (responsivo)
            Container(
              padding: const EdgeInsets.all(AppStyles.spacing4),
              decoration: BoxDecoration(
                color: Colors.blue.withValues(alpha: 0.06),
                borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                border: Border.all(color: Colors.blue.withValues(alpha: 0.25)),
              ),
              child: isMobile
                  ? Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Icon(Icons.info_outline, color: Colors.blue[700]),
                        const SizedBox(height: AppStyles.spacing2),
                        Text(
                          'Detalle completo de las calificaciones por materia. Toca "Editar" para modificar las notas.',
                          style: TextStyle(color: Colors.blue[700]),
                        ),
                      ],
                    )
                  : Row(
                      children: [
                        Icon(Icons.info_outline, color: Colors.blue[700]),
                        const SizedBox(width: AppStyles.spacing2),
                        Expanded(
                          child: Text(
                            'Detalle completo de las calificaciones por materia. Toca "Editar" para modificar las notas.',
                            style: TextStyle(color: Colors.blue[700]),
                          ),
                        ),
                      ],
                    ),
            ),
            const SizedBox(height: AppStyles.spacing4),

            // Materias y notas (responsivo: tarjetas en mobile, DataTable en tablet/desktop)
            if (materiasConNotas.isEmpty)
              Card(
                elevation: 0.5,
                child: Padding(
                  padding: const EdgeInsets.all(AppStyles.spacing6),
                  child: Center(
                    child: Column(
                      children: [
                        Icon(Icons.school_outlined, size: 64, color: Colors.grey[400]),
                        const SizedBox(height: AppStyles.spacing4),
                        Text(
                          'No hay materias asignadas',
                          style: Theme.of(context).textTheme.titleLarge,
                        ),
                        const SizedBox(height: AppStyles.spacing2),
                        Text(
                          'Este curso no tiene materias configuradas',
                          style: Theme.of(
                            context,
                          ).textTheme.bodyMedium?.copyWith(color: Colors.grey[600]),
                        ),
                      ],
                    ),
                  ),
                ),
              )
            else
              Card(
                elevation: 0.5,
                child: Padding(
                  padding: const EdgeInsets.only(bottom: AppStyles.spacing3),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(AppStyles.spacing4),
                        child: Text(
                          'Calificaciones por Materia',
                          style: Theme.of(
                            context,
                          ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
                        ),
                      ),
                      if (isMobile)
                        ListView.separated(
                          shrinkWrap: true,
                          physics: const NeverScrollableScrollPhysics(),
                          itemCount: materiasConNotas.length,
                          separatorBuilder: (_, __) => const Divider(height: 1),
                          itemBuilder: (context, index) {
                            final m = materiasConNotas[index];
                            final c1 = _getNotaColor(_getNotaDisplay(m.notaParcial1));
                            final c2 = _getNotaColor(_getNotaDisplay(m.notaParcial2));
                            final c3 = _getNotaColor(_getNotaDisplay(m.notaParcial3));
                            final cp = _getNotaColor(m.promedio);

                            return Padding(
                              padding: const EdgeInsets.symmetric(
                                horizontal: AppStyles.spacing4,
                                vertical: AppStyles.spacing3,
                              ),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    m.materia.nombreMateria,
                                    style: Theme.of(
                                      context,
                                    ).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w600),
                                  ),
                                  const SizedBox(height: AppStyles.spacing2),
                                  Wrap(
                                    spacing: AppStyles.spacing2,
                                    runSpacing: AppStyles.spacing2,
                                    children: [
                                      _NotaChip(
                                        label: 'P1',
                                        valor: _getNotaDisplay(m.notaParcial1),
                                        color: c1,
                                      ),
                                      _NotaChip(
                                        label: 'P2',
                                        valor: _getNotaDisplay(m.notaParcial2),
                                        color: c2,
                                      ),
                                      _NotaChip(
                                        label: 'P3',
                                        valor: _getNotaDisplay(m.notaParcial3),
                                        color: c3,
                                      ),
                                      _NotaChip(
                                        label: 'Promedio',
                                        valor: m.promedio,
                                        color: cp,
                                        enfatizar: true,
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            );
                          },
                        )
                      else
                        SingleChildScrollView(
                          scrollDirection: Axis.horizontal,
                          child: DataTable(
                            columns: const [
                              DataColumn(label: Text('Materia')),
                              DataColumn(label: Text('Parcial 1')),
                              DataColumn(label: Text('Parcial 2')),
                              DataColumn(label: Text('Parcial 3')),
                              DataColumn(label: Text('Promedio')),
                            ],
                            rows: materiasConNotas.map<DataRow>((m) {
                              return DataRow(
                                cells: [
                                  DataCell(
                                    SizedBox(
                                      width: isTablet ? 180 : 220,
                                      child: Text(
                                        m.materia.nombreMateria,
                                        overflow: TextOverflow.ellipsis,
                                        style: const TextStyle(fontWeight: FontWeight.w500),
                                      ),
                                    ),
                                  ),
                                  DataCell(
                                    _CeldaNota(
                                      valor: _getNotaDisplay(m.notaParcial1),
                                      color: _getNotaColor(_getNotaDisplay(m.notaParcial1)),
                                    ),
                                  ),
                                  DataCell(
                                    _CeldaNota(
                                      valor: _getNotaDisplay(m.notaParcial2),
                                      color: _getNotaColor(_getNotaDisplay(m.notaParcial2)),
                                    ),
                                  ),
                                  DataCell(
                                    _CeldaNota(
                                      valor: _getNotaDisplay(m.notaParcial3),
                                      color: _getNotaColor(_getNotaDisplay(m.notaParcial3)),
                                    ),
                                  ),
                                  DataCell(
                                    _CeldaNota(
                                      valor: m.promedio,
                                      color: _getNotaColor(m.promedio),
                                      enfatizar: true,
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
              ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          context.push('/profesor/asignar-notas/${widget.idUsuario}/${widget.idCurso}');
        },
        icon: const Icon(Icons.edit),
        label: const Text('Asignar Notas'),
      ),
    );
  }
}

// ------- Widgets de apoyo responsivos -------

class _PromedioBadge extends StatelessWidget {
  final String titulo;
  final String promedio;
  final Color color;

  const _PromedioBadge({required this.titulo, required this.promedio, required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: AppStyles.spacing3,
        vertical: AppStyles.spacing2,
      ),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(AppStyles.radiusLg),
        border: Border.all(color: color),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(titulo, style: const TextStyle(fontSize: 12)),
          Text(
            promedio,
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: color),
          ),
        ],
      ),
    );
  }
}

class _CeldaNota extends StatelessWidget {
  final String valor;
  final Color color;
  final bool enfatizar;

  const _CeldaNota({required this.valor, required this.color, this.enfatizar = false});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: AppStyles.spacing2,
        vertical: AppStyles.spacing1,
      ),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(AppStyles.radiusSm),
        border: Border.all(color: enfatizar ? color : color.withValues(alpha: 0.35)),
      ),
      child: Text(
        valor,
        style: TextStyle(color: color, fontWeight: FontWeight.bold),
      ),
    );
  }
}

class _NotaChip extends StatelessWidget {
  final String label;
  final String valor;
  final Color color;
  final bool enfatizar;

  const _NotaChip({
    required this.label,
    required this.valor,
    required this.color,
    this.enfatizar = false,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: AppStyles.spacing3,
        vertical: AppStyles.spacing2,
      ),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(AppStyles.radiusMd),
        border: Border.all(color: enfatizar ? color : color.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text('$label: ', style: const TextStyle(fontWeight: FontWeight.w500)),
          Text(
            valor,
            style: TextStyle(fontWeight: FontWeight.bold, color: color),
          ),
        ],
      ),
    );
  }
}

class MateriaConNotas {
  final Materia materia;
  final Nota? notaParcial1;
  final Nota? notaParcial2;
  final Nota? notaParcial3;
  final String promedio;

  MateriaConNotas({
    required this.materia,
    this.notaParcial1,
    this.notaParcial2,
    this.notaParcial3,
    required this.promedio,
  });
}
