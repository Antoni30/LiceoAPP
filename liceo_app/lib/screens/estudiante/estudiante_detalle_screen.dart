import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:go_router/go_router.dart';
import '../../constants/app_styles.dart';
import '../../providers/auth_provider.dart';
import '../../widgets/loading_spinner.dart';
import '../../models/user_model.dart';
import '../../models/academic_models.dart';
import '../../services/real_api_service.dart';

class EstudianteDetalleScreen extends StatefulWidget {
  const EstudianteDetalleScreen({super.key});

  @override
  State<EstudianteDetalleScreen> createState() => _EstudianteDetalleScreenState();
}

class _EstudianteDetalleScreenState extends State<EstudianteDetalleScreen> {
  UserModel? _estudiante;
  Curso? _cursoSeleccionado;
  List<_MateriaConNotas> _materiasConNotas = [];
  bool _cargando = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _cargarDatosIniciales();
  }

  Future<void> _cargarDatosIniciales() async {
    final auth = context.read<AuthProvider>();
    final userId = auth.userId;

    if (userId == null || userId.isEmpty) {
      setState(() {
        _error = 'No se pudo identificar al usuario actual.';
        _cargando = false;
      });
      return;
    }

    setState(() {
      _cargando = true;
      _error = null;
    });

    try {
      final estudiante = await UserService.getUser(userId);
      final cursos = await UserService.getUserCursos(userId);

      int? cursoIdSeleccionado;
      Curso? cursoSeleccionado;

      if (cursos.isNotEmpty) {
        cursoIdSeleccionado = cursos.first.idCurso;
        cursoSeleccionado = await CursoService.getCurso(cursoIdSeleccionado.toString());
      }

      List<_MateriaConNotas> materiasConNotas = [];
      if (cursoIdSeleccionado != null) {
        materiasConNotas = await _cargarMateriasConNotas(userId, cursoIdSeleccionado);
      }

      if (!mounted) return;
      setState(() {
        _estudiante = estudiante;
        _cursoSeleccionado = cursoSeleccionado;
        _materiasConNotas = materiasConNotas;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() {
        _error = e.toString();
      });
    } finally {
      if (!mounted) return;
      setState(() {
        _cargando = false;
      });
    }
  }

  Future<List<_MateriaConNotas>> _cargarMateriasConNotas(String userId, int idCurso) async {
    final materiasDelCurso = await CursoService.getCursoMaterias(idCurso.toString());
    final List<_MateriaConNotas> resultado = [];

    for (final cm in materiasDelCurso) {
      try {
        final materia = await MateriaService.getMateria(cm.idMateria.toString());
        final notas = await NotaService.getNotasByUsuarioMateria(userId, cm.idMateria.toString());

        final mapa = <int, Nota?>{1: null, 2: null, 3: null};
        for (final n in notas) {
          if (n.parcial >= 1 && n.parcial <= 3) {
            mapa[n.parcial] = n;
          }
        }

        final validas = mapa.values.where((n) => n != null).map((n) => n!.nota).toList();
        String promedio = 'S/N';
        if (validas.isNotEmpty) {
          final suma = validas.reduce((a, b) => a + b);
          promedio = (suma / validas.length).toStringAsFixed(2);
        }

        resultado.add(
          _MateriaConNotas(
            materia: materia,
            notaParcial1: mapa[1],
            notaParcial2: mapa[2],
            notaParcial3: mapa[3],
            promedio: promedio,
          ),
        );
      } catch (_) {
        resultado.add(
          _MateriaConNotas(
            materia: Materia(
              idMateria: cm.idMateria,
              nombre: 'Materia ID: ${cm.idMateria}',
              nombreMateria: 'Materia ID: ${cm.idMateria}',
            ),
            notaParcial1: null,
            notaParcial2: null,
            notaParcial3: null,
            promedio: 'S/N',
          ),
        );
      }
    }
    return resultado;
  }

  String _promedioGeneral() {
    final promedios = _materiasConNotas
        .where((m) => m.promedio != 'S/N')
        .map((m) => double.tryParse(m.promedio) ?? 0.0)
        .where((p) => p > 0)
        .toList();
    if (promedios.isEmpty) return 'S/N';
    final suma = promedios.reduce((a, b) => a + b);
    return (suma / promedios.length).toStringAsFixed(2);
  }

  int _aprobadas() {
    return _materiasConNotas
        .where((m) => m.promedio != 'S/N' && (double.tryParse(m.promedio) ?? 0) >= 14)
        .length;
  }

  Color _notaColor(String? nota) {
    if (nota == null || nota == 'S/N') return Colors.grey;
    final v = double.tryParse(nota);
    if (v == null) return Colors.grey;
    return v >= 14 ? Colors.green : Colors.red;
  }

  String _notaDisplay(Nota? n) => n?.nota.toString() ?? 'S/N';

  void _irAlPerfil() {
    context.push('/perfil');
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final width = size.width;
    final isMobile = width < 600;
    final isTablet = width >= 600 && width < 900;

    if (_cargando) {
      return Scaffold(
        appBar: AppBar(title: const Text('Mi Detalle Académico')),
        body: const LoadingSpinner(),
      );
    }

    if (_error != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Mi Detalle Académico')),
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
                Text(_error!, textAlign: TextAlign.center),
                const SizedBox(height: AppStyles.spacing4),
                ElevatedButton(onPressed: _cargarDatosIniciales, child: const Text('Reintentar')),
              ],
            ),
          ),
        ),
      );
    }

    final promedioGeneral = _promedioGeneral();
    final promedioColor = _notaColor(promedioGeneral);
    final horizontalPad = isMobile ? AppStyles.spacing3 : AppStyles.spacing4;

    return Scaffold(
      appBar: AppBar(
        title: Text('${_estudiante?.nombres ?? ''} ${_estudiante?.apellidos ?? ''}'),
        // Se quitaron los íconos del AppBar (especialmente el lápiz de editar)
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
                    // Header del estudiante (responsivo) + acceso visible al perfil
                    if (_estudiante != null) ...[
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
                                        InkWell(
                                          borderRadius: BorderRadius.circular(999),
                                          onTap: _irAlPerfil,
                                          child: CircleAvatar(
                                            radius: 30,
                                            backgroundColor: AppColors.primary,
                                            child: Text(
                                              '${(_estudiante!.nombres.isNotEmpty ? _estudiante!.nombres[0] : '?')}${(_estudiante!.apellidos.isNotEmpty ? _estudiante!.apellidos[0] : '?')}',
                                              style: const TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold,
                                                fontSize: 18,
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: AppStyles.spacing3),
                                        Expanded(
                                          child: Column(
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                '${_estudiante!.nombres} ${_estudiante!.apellidos}',
                                                style: Theme.of(context).textTheme.titleMedium
                                                    ?.copyWith(fontWeight: FontWeight.bold),
                                                maxLines: 2,
                                                overflow: TextOverflow.ellipsis,
                                              ),
                                              const SizedBox(height: AppStyles.spacing1),
                                              Text('ID: ${_estudiante!.idUsuario}'),
                                              Text(
                                                'Curso: ${_cursoSeleccionado?.nombreCurso ?? 'N/A'}',
                                              ),
                                              if (_estudiante!.email != null)
                                                Text('Email: ${_estudiante!.email}'),
                                            ],
                                          ),
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: AppStyles.spacing3),
                                    // Botón grande y visible para ir al Perfil
                                    SizedBox(
                                      width: double.infinity,
                                      child: FilledButton.icon(
                                        onPressed: _irAlPerfil,
                                        icon: const Icon(Icons.person),
                                        label: const Text('Ver mi perfil'),
                                      ),
                                    ),
                                    const SizedBox(height: AppStyles.spacing3),
                                    Wrap(
                                      spacing: AppStyles.spacing2,
                                      runSpacing: AppStyles.spacing2,
                                      children: [
                                        _KpiChip(
                                          icon: Icons.menu_book,
                                          label: 'Materias',
                                          value: _materiasConNotas.length.toString(),
                                        ),
                                        _KpiChip(
                                          icon: Icons.check_circle,
                                          label: 'Aprobadas',
                                          value: _aprobadas().toString(),
                                        ),
                                        _KpiChip(
                                          icon: Icons.star_rate,
                                          label: 'Promedio',
                                          value: promedioGeneral,
                                          color: promedioColor,
                                        ),
                                      ],
                                    ),
                                  ],
                                )
                              : Row(
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    InkWell(
                                      borderRadius: BorderRadius.circular(999),
                                      onTap: _irAlPerfil,
                                      child: CircleAvatar(
                                        radius: 35,
                                        backgroundColor: AppColors.primary,
                                        child: Text(
                                          '${(_estudiante!.nombres.isNotEmpty ? _estudiante!.nombres[0] : '?')}${(_estudiante!.apellidos.isNotEmpty ? _estudiante!.apellidos[0] : '?')}',
                                          style: const TextStyle(
                                            color: Colors.white,
                                            fontWeight: FontWeight.bold,
                                            fontSize: 20,
                                          ),
                                        ),
                                      ),
                                    ),
                                    const SizedBox(width: AppStyles.spacing4),
                                    Expanded(
                                      child: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          Text(
                                            '${_estudiante!.nombres} ${_estudiante!.apellidos}',
                                            style: Theme.of(context).textTheme.titleLarge?.copyWith(
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                          const SizedBox(height: AppStyles.spacing1),
                                          Text('ID: ${_estudiante!.idUsuario}'),
                                          Text(
                                            'Curso: ${_cursoSeleccionado?.nombreCurso ?? 'N/A'}',
                                          ),
                                          if (_estudiante!.email != null)
                                            Text('Email: ${_estudiante!.email}'),
                                        ],
                                      ),
                                    ),
                                    // Columna con KPI + botón visible de Perfil
                                    Column(
                                      mainAxisSize: MainAxisSize.min,
                                      crossAxisAlignment: CrossAxisAlignment.end,
                                      children: [
                                        _KpiChip(
                                          icon: Icons.star_rate,
                                          label: 'Promedio',
                                          value: promedioGeneral,
                                          color: promedioColor,
                                        ),
                                        const SizedBox(height: AppStyles.spacing3),
                                        FilledButton.icon(
                                          onPressed: _irAlPerfil,
                                          icon: const Icon(Icons.person),
                                          label: const Text('Ver mi perfil'),
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                        ),
                      ),
                      const SizedBox(height: AppStyles.spacing4),
                    ],

                    // Banner informativo sutil
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
                                  'Revisa tus calificaciones por materia y tu promedio general.',
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
                                    'Revisa tus calificaciones por materia y tu promedio general.',
                                    style: TextStyle(color: Colors.blue[700]),
                                  ),
                                ),
                              ],
                            ),
                    ),
                    const SizedBox(height: AppStyles.spacing4),

                    // Materias y notas (responsivo)
                    if (_materiasConNotas.isEmpty)
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
                                  'Mis Calificaciones',
                                  style: Theme.of(
                                    context,
                                  ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
                                ),
                              ),
                              if (isMobile)
                                ListView.separated(
                                  shrinkWrap: true,
                                  physics: const NeverScrollableScrollPhysics(),
                                  itemCount: _materiasConNotas.length,
                                  separatorBuilder: (_, __) => const Divider(height: 1),
                                  itemBuilder: (context, index) {
                                    final m = _materiasConNotas[index];
                                    final c1 = _notaColor(_notaDisplay(m.notaParcial1));
                                    final c2 = _notaColor(_notaDisplay(m.notaParcial2));
                                    final c3 = _notaColor(_notaDisplay(m.notaParcial3));
                                    final cp = _notaColor(m.promedio);

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
                                            style: Theme.of(context).textTheme.titleMedium
                                                ?.copyWith(fontWeight: FontWeight.w600),
                                          ),
                                          const SizedBox(height: AppStyles.spacing2),
                                          Wrap(
                                            spacing: AppStyles.spacing2,
                                            runSpacing: AppStyles.spacing2,
                                            children: [
                                              _NotaChip(
                                                label: 'P1',
                                                valor: _notaDisplay(m.notaParcial1),
                                                color: c1,
                                              ),
                                              _NotaChip(
                                                label: 'P2',
                                                valor: _notaDisplay(m.notaParcial2),
                                                color: c2,
                                              ),
                                              _NotaChip(
                                                label: 'P3',
                                                valor: _notaDisplay(m.notaParcial3),
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
                                DataTableTheme(
                                  data: DataTableThemeData(
                                    columnSpacing: isMobile ? 12 : 24,
                                    horizontalMargin: isMobile ? 8 : 24,
                                    dividerThickness: 0.6,
                                    headingTextStyle: Theme.of(context).textTheme.labelLarge,
                                    dataTextStyle: Theme.of(context).textTheme.bodyMedium,
                                  ),
                                  child: SingleChildScrollView(
                                    scrollDirection: Axis.horizontal,
                                    child: DataTable(
                                      columns: const [
                                        DataColumn(label: Text('Materia')),
                                        DataColumn(label: Text('Parcial 1')),
                                        DataColumn(label: Text('Parcial 2')),
                                        DataColumn(label: Text('Parcial 3')),
                                        DataColumn(label: Text('Promedio')),
                                      ],
                                      rows: _materiasConNotas.map<DataRow>((m) {
                                        return DataRow(
                                          cells: [
                                            DataCell(
                                              SizedBox(
                                                width: isTablet ? 180 : 220,
                                                child: Text(
                                                  m.materia.nombreMateria,
                                                  overflow: TextOverflow.ellipsis,
                                                  style: const TextStyle(
                                                    fontWeight: FontWeight.w500,
                                                  ),
                                                ),
                                              ),
                                            ),
                                            DataCell(
                                              _CeldaNota(
                                                valor: _notaDisplay(m.notaParcial1),
                                                color: _notaColor(_notaDisplay(m.notaParcial1)),
                                              ),
                                            ),
                                            DataCell(
                                              _CeldaNota(
                                                valor: _notaDisplay(m.notaParcial2),
                                                color: _notaColor(_notaDisplay(m.notaParcial2)),
                                              ),
                                            ),
                                            DataCell(
                                              _CeldaNota(
                                                valor: _notaDisplay(m.notaParcial3),
                                                color: _notaColor(_notaDisplay(m.notaParcial3)),
                                              ),
                                            ),
                                            DataCell(
                                              _CeldaNota(
                                                valor: m.promedio,
                                                color: _notaColor(m.promedio),
                                                enfatizar: true,
                                              ),
                                            ),
                                          ],
                                        );
                                      }).toList(),
                                    ),
                                  ),
                                ),
                            ],
                          ),
                        ),
                      ),

                    const SizedBox(height: AppStyles.spacing6), // empuja para ocupar altura
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

// ------- Widgets y tipos de apoyo -------

class _MateriaConNotas {
  final Materia materia;
  final Nota? notaParcial1;
  final Nota? notaParcial2;
  final Nota? notaParcial3;
  final String promedio;

  _MateriaConNotas({
    required this.materia,
    this.notaParcial1,
    this.notaParcial2,
    this.notaParcial3,
    required this.promedio,
  });
}

class _KpiChip extends StatelessWidget {
  final IconData icon;
  final String label;
  final String value;
  final Color? color;

  const _KpiChip({required this.icon, required this.label, required this.value, this.color});

  @override
  Widget build(BuildContext context) {
    final c = color ?? AppColors.primary;
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: AppStyles.spacing3,
        vertical: AppStyles.spacing2,
      ),
      decoration: BoxDecoration(
        color: c.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(AppStyles.radiusMd),
        border: Border.all(color: c.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 16, color: c),
          const SizedBox(width: AppStyles.spacing2),
          Text('$label: ', style: const TextStyle(fontWeight: FontWeight.w500)),
          Text(
            value,
            style: TextStyle(fontWeight: FontWeight.bold, color: c),
          ),
        ],
      ),
    );
  }
}

// (El badge de promedio no se usa en esta versión)

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
