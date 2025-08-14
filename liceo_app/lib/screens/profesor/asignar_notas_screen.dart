import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../services/real_api_service.dart';
import '../../widgets/loading_spinner.dart';
import '../../models/user_model.dart';
import '../../models/academic_models.dart';
import '../../constants/app_styles.dart';

class AsignarNotasScreen extends StatefulWidget {
  final String idUsuario;
  final String idCurso;

  const AsignarNotasScreen({super.key, required this.idUsuario, required this.idCurso});

  @override
  State<AsignarNotasScreen> createState() => _AsignarNotasScreenState();
}

class _AsignarNotasScreenState extends State<AsignarNotasScreen> {
  UserModel? estudiante;
  List<CursoMateria> materias = [];
  Map<String, String> materiasNombres = {};
  Map<String, Map<int, String>> notas = {};
  bool cargando = true;
  String? error;
  Curso? curso;
  String? editando; // '<idMateria>_<parcial>'
  final TextEditingController _notaController = TextEditingController();
  bool guardando = false;

  @override
  void initState() {
    super.initState();
    _fetchData();
  }

  @override
  void dispose() {
    _notaController.dispose();
    super.dispose();
  }

  String _calcularPromedio(Map<int, String> notasMateria) {
    final valores = [notasMateria[1] ?? "S/N", notasMateria[2] ?? "S/N", notasMateria[3] ?? "S/N"];
    final nums = valores.where((n) => n != "S/N").map((n) => double.tryParse(n) ?? 0.0).toList();
    if (nums.isEmpty) return "S/N";
    final suma = nums.reduce((a, b) => a + b);
    return (suma / nums.length).toStringAsFixed(2);
  }

  String _calcularPromedioGeneral() {
    final promedios = materias
        .map((m) => _calcularPromedio(notas[m.idMateria.toString()] ?? {}))
        .where((p) => p != "S/N")
        .map((p) => double.tryParse(p) ?? 0.0)
        .toList();
    if (promedios.isEmpty) return "S/N";
    final suma = promedios.reduce((a, b) => a + b);
    return (suma / promedios.length).toStringAsFixed(2);
  }

  Future<void> _fetchData() async {
    try {
      setState(() {
        cargando = true;
        error = null;
      });

      curso = await CursoService.getCurso(widget.idCurso);
      estudiante = await UserService.getUser(widget.idUsuario);
      materias = await CursoService.getCursoMaterias(widget.idCurso);

      final nombres = <String, String>{};
      for (final m in materias) {
        try {
          final det = await MateriaService.getMateria(m.idMateria.toString());
          nombres[m.idMateria.toString()] = det.nombreMateria;
        } catch (_) {
          nombres[m.idMateria.toString()] = 'Materia ID: ${m.idMateria}';
        }
      }
      materiasNombres = nombres;

      final notasMap = <String, Map<int, String>>{};
      for (final m in materias) {
        try {
          final ns = await NotaService.getNotasByUsuarioMateria(
            widget.idUsuario,
            m.idMateria.toString(),
          );
          final parciales = <int, String>{1: "S/N", 2: "S/N", 3: "S/N"};
          for (final n in ns) {
            if (n.parcial >= 1 && n.parcial <= 3) parciales[n.parcial] = n.nota.toString();
          }
          notasMap[m.idMateria.toString()] = parciales;
        } catch (_) {
          notasMap[m.idMateria.toString()] = {1: "S/N", 2: "S/N", 3: "S/N"};
        }
      }

      setState(() {
        notas = notasMap;
      });
    } catch (err) {
      setState(() {
        error = err.toString();
      });
    } finally {
      if (mounted) setState(() => cargando = false);
    }
  }

  void _iniciarEdicion(String idMateria, int parcial, String valorActual) {
    setState(() {
      editando = '${idMateria}_$parcial';
      _notaController.text = valorActual == "S/N" ? "" : valorActual;
    });
  }

  void _cancelarEdicion() {
    setState(() {
      editando = null;
      _notaController.clear();
    });
  }

  Future<void> _guardarNota(String idMateria, int parcial) async {
    final valor = _notaController.text.trim();

    if (valor.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Por favor ingrese una nota')));
      return;
    }
    final notaValue = double.tryParse(valor);
    if (notaValue == null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('La nota debe ser un número válido')));
      return;
    }
    if (notaValue < 0 || notaValue > 20) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('La nota debe estar entre 0 y 20')));
      return;
    }

    try {
      setState(() => guardando = true);

      final existentes = await NotaService.getNotasByUsuarioMateria(widget.idUsuario, idMateria);
      Nota? existente;
      for (final n in existentes) {
        if (n.parcial == parcial) {
          existente = n;
          break;
        }
      }

      if (existente != null) {
        await NotaService.updateNota(
          existente.id.toString(),
          NotaRequest(
            idUsuario: widget.idUsuario,
            idMateria: int.parse(idMateria),
            nota: notaValue,
            parcial: parcial,
          ),
        );
      } else {
        await NotaService.createNota(
          NotaRequest(
            idUsuario: widget.idUsuario,
            idMateria: int.parse(idMateria),
            nota: notaValue,
            parcial: parcial,
          ),
        );
      }

      setState(() {
        final current = {
          ...(notas[idMateria] ?? {1: "S/N", 2: "S/N", 3: "S/N"}),
        };
        current[parcial] = valor;
        notas[idMateria] = current;
      });

      _cancelarEdicion();
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('Nota guardada exitosamente')));
      }
    } catch (err) {
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Error al guardar la nota: $err')));
      }
    } finally {
      if (mounted) setState(() => guardando = false);
    }
  }

  Color _getNotaColor(String nota) {
    if (nota == "S/N") return Colors.grey;
    final v = double.tryParse(nota);
    if (v == null) return Colors.grey;
    return v >= 14 ? Colors.green : Colors.red;
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final width = size.width;
    final isMobile = width < 600;
    final isTablet = width >= 600 && width < 900;

    if (cargando) {
      return Scaffold(
        appBar: AppBar(title: const Text('Asignar Notas')),
        body: const LoadingSpinner(),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Asignar Notas')),
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

    final horizontalPad = isMobile ? AppStyles.spacing3 : AppStyles.spacing4;
    final availableWidth = size.width - (horizontalPad * 2);

    return Scaffold(
      appBar: AppBar(
        title: Text('Asignar Notas - ${curso?.nombreCurso ?? ''}'),
        leading: IconButton(icon: const Icon(Icons.arrow_back), onPressed: () => context.pop()),
      ),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            return SingleChildScrollView(
              padding: EdgeInsets.symmetric(
                horizontal: horizontalPad,
                vertical: AppStyles.spacing4,
              ),
              child: ConstrainedBox(
                constraints: BoxConstraints(minHeight: constraints.maxHeight),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    // Header estudiante (responsivo)
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
                                                style: Theme.of(context).textTheme.titleMedium
                                                    ?.copyWith(fontWeight: FontWeight.bold),
                                                maxLines: 2,
                                                overflow: TextOverflow.ellipsis,
                                              ),
                                              Text('ID: ${estudiante!.idUsuario}'),
                                              Text(
                                                'Email: ${estudiante!.email ?? 'No disponible'}',
                                              ),
                                            ],
                                          ),
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: AppStyles.spacing3),
                                    _PromedioBadge(
                                      titulo: 'Promedio General',
                                      promedio: promedioGeneral,
                                      color: promedioColor,
                                    ),
                                  ],
                                )
                              : Row(
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
                                            style: Theme.of(context).textTheme.titleLarge?.copyWith(
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                          Text('ID: ${estudiante!.idUsuario}'),
                                          Text('Email: ${estudiante!.email ?? 'No disponible'}'),
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

                    // Banner informativo
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
                                  'Toca una nota para editarla. Las notas deben estar entre 0 y 20.',
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
                                    'Toca una nota para editarla. Las notas deben estar entre 0 y 20.',
                                    style: TextStyle(color: Colors.blue[700]),
                                  ),
                                ),
                              ],
                            ),
                    ),
                    const SizedBox(height: AppStyles.spacing4),

                    // Parciales en FILAS (misma edición inline)
                    if (materias.isEmpty)
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
                          child: DataTableTheme(
                            data: DataTableThemeData(
                              columnSpacing: isMobile ? 12 : 24,
                              horizontalMargin: isMobile ? 8 : 24,
                              dividerThickness: 0.6,
                              headingTextStyle: Theme.of(context).textTheme.labelLarge,
                              dataTextStyle: Theme.of(context).textTheme.bodyMedium,
                            ),
                            child: SingleChildScrollView(
                              scrollDirection: Axis.horizontal,
                              child: ConstrainedBox(
                                constraints: BoxConstraints(minWidth: availableWidth),
                                child: DataTable(
                                  columns: const [
                                    DataColumn(label: Text('Materia')),
                                    DataColumn(label: Text('Parcial')),
                                    DataColumn(label: Text('Nota')),
                                  ],
                                  rows: [
                                    for (final m in materias)
                                      ..._rowsPorMateria(m, isMobile, isTablet),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),

                    // empuja el contenido para llenar el alto si es corto
                    const SizedBox(height: AppStyles.spacing6),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }

  List<DataRow> _rowsPorMateria(CursoMateria m, bool isMobile, bool isTablet) {
    final idMateria = m.idMateria.toString();
    final nombreMateria = materiasNombres[idMateria] ?? 'Materia ID: ${m.idMateria}';
    final notasMateria = notas[idMateria] ?? {1: "S/N", 2: "S/N", 3: "S/N"};
    final promedio = _calcularPromedio(notasMateria);

    final filas = <DataRow>[];

    for (var parcial = 1; parcial <= 3; parcial++) {
      final nota = notasMateria[parcial] ?? "S/N";
      final isEditing = editando == '${idMateria}_$parcial';
      final color = _getNotaColor(nota);

      filas.add(
        DataRow(
          cells: [
            DataCell(
              SizedBox(
                width: isMobile ? 140 : (isTablet ? 180 : 220),
                child: Text(
                  parcial == 1 ? nombreMateria : '',
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(fontWeight: FontWeight.w500),
                ),
              ),
            ),
            DataCell(Text('Parcial $parcial')),
            if (isEditing)
              DataCell(
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    SizedBox(
                      width: 60,
                      child: TextField(
                        controller: _notaController,
                        keyboardType: const TextInputType.numberWithOptions(decimal: true),
                        decoration: const InputDecoration(
                          isDense: true,
                          contentPadding: EdgeInsets.all(8),
                        ),
                        enabled: !guardando,
                      ),
                    ),
                    IconButton(
                      icon: Icon(
                        guardando ? Icons.hourglass_empty : Icons.check,
                        color: Colors.green,
                        size: 20,
                      ),
                      onPressed: guardando ? null : () => _guardarNota(idMateria, parcial),
                    ),
                    IconButton(
                      icon: const Icon(Icons.close, color: Colors.red, size: 20),
                      onPressed: guardando ? null : _cancelarEdicion,
                    ),
                  ],
                ),
              )
            else
              DataCell(
                GestureDetector(
                  onTap: () => _iniciarEdicion(idMateria, parcial, nota),
                  child: Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: AppStyles.spacing2,
                      vertical: AppStyles.spacing1,
                    ),
                    decoration: BoxDecoration(
                      color: color.withValues(alpha: 0.08),
                      borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                      border: Border.all(color: color.withValues(alpha: 0.35)),
                    ),
                    child: Text(
                      nota,
                      style: TextStyle(color: color, fontWeight: FontWeight.bold),
                    ),
                  ),
                ),
              ),
          ],
        ),
      );
    }

    final colorProm = _getNotaColor(promedio);
    filas.add(
      DataRow(
        cells: [
          const DataCell(Text('')),
          const DataCell(Text('Promedio', style: TextStyle(fontWeight: FontWeight.w600))),
          DataCell(
            Container(
              padding: const EdgeInsets.symmetric(
                horizontal: AppStyles.spacing2,
                vertical: AppStyles.spacing1,
              ),
              decoration: BoxDecoration(
                color: colorProm.withValues(alpha: 0.08),
                borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                border: Border.all(color: colorProm),
              ),
              child: Text(
                promedio,
                style: TextStyle(color: colorProm, fontWeight: FontWeight.bold),
              ),
            ),
          ),
        ],
      ),
    );

    return filas;
  }
}

// ---- Widgets de apoyo (solo estilos) ----

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
