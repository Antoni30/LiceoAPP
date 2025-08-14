import 'package:flutter/material.dart';
import '../../constants/app_styles.dart';
import '../../services/curso_service.dart';
import '../../services/anio_academico_service.dart';
import '../../widgets/custom_button.dart';
import '../../widgets/loading_spinner.dart';

class AnioLectivoActualScreen extends StatefulWidget {
  final String? anioId;
  
  const AnioLectivoActualScreen({super.key, this.anioId});

  @override
  State<AnioLectivoActualScreen> createState() => _AnioLectivoActualScreenState();
}

class _AnioLectivoActualScreenState extends State<AnioLectivoActualScreen> {
  final CursoService _cursoService = CursoService();
  final AnioAcademicoService _anioService = AnioAcademicoService();
  
  Map<String, dynamic>? anioActivo;
  List<Map<String, dynamic>> cursos = [];
  bool isLoading = true;
  String? error;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    try {
      setState(() {
        isLoading = true;
        error = null;
      });

      // Obtener año activo o por ID
      Map<String, dynamic>? anio;
      if (widget.anioId != null) {
        final anioIdInt = int.tryParse(widget.anioId!);
        if (anioIdInt != null) {
          anio = await _anioService.getAnioAcademicoById(anioIdInt);
        } else {
          throw Exception('ID de año académico inválido');
        }
      } else {
        final anios = await _anioService.getAllAniosAcademicos();
        for (var a in anios) {
          if (a['activo'] == true) {
            anio = a;
            break;
          }
        }
      }

      if (anio == null) {
        throw Exception('No se encontró el año lectivo');
      }

      // Obtener cursos del año
      final cursosResponse = await _cursoService.getCursosByAnioAcademico(anio['id']);

      setState(() {
        anioActivo = anio;
        cursos = List<Map<String, dynamic>>.from(cursosResponse);
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        error = e.toString();
        isLoading = false;
      });
    }
  }

  Future<void> _showCreateCursoDialog() async {
    if (anioActivo == null) return;

    final result = await showDialog<Map<String, dynamic>>(
      context: context,
      builder: (context) => CreateCursoDialog(anioId: anioActivo!['id']),
    );

    if (result != null) {
      try {
        await _cursoService.createCurso(result);
        _loadData(); // Recargar la lista
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Curso creado exitosamente'),
              backgroundColor: AppColors.success,
            ),
          );
        }
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Error al crear curso: $e'),
              backgroundColor: AppColors.error,
            ),
          );
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Gestión Año Lectivo Actual'),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      body: Column(
        children: [
          // Header con información del año
          if (anioActivo != null) _buildAnioHeader(),
          
          // Lista de cursos
          Expanded(
            child: Container(
              margin: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: AppShadows.cardShadow,
              ),
              child: Column(
                children: [
                  // Header de la lista
                  Container(
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: AppColors.secondary.withValues(alpha: 0.1),
                      borderRadius: const BorderRadius.only(
                        topLeft: Radius.circular(12),
                        topRight: Radius.circular(12),
                      ),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          'Cursos del Año Lectivo',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            color: AppColors.textPrimary,
                          ),
                        ),
                        CustomButton(
                          text: 'Nuevo Curso',
                          iconData: Icons.add,
                          onPressed: _showCreateCursoDialog,
                          variant: ButtonVariant.secondary,
                          size: ButtonSize.small,
                        ),
                      ],
                    ),
                  ),
                  
                  // Lista
                  Expanded(child: _buildCursosList()),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAnioHeader() {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [AppColors.secondary, AppColors.secondary.withValues(alpha: 0.8)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(16),
        boxShadow: AppShadows.cardShadow,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(
                Icons.calendar_month,
                color: Colors.white,
                size: 28,
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Año Lectivo Actual',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    Text(
                      anioActivo!['nombre'] ?? 'Sin nombre',
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          Row(
            children: [
              Expanded(
                child: _buildStatCard(
                  'Cursos',
                  '${cursos.length}',
                  Icons.class_,
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: _buildStatCard(
                  'Fecha Inicio',
                  anioActivo!['fechaInicio'] ?? 'No definida',
                  Icons.event_available,
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: _buildStatCard(
                  'Fecha Fin',
                  anioActivo!['fechaFin'] ?? 'No definida',
                  Icons.event_busy,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildStatCard(String label, String value, IconData icon) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.white.withValues(alpha: 0.2),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        children: [
          Icon(icon, color: Colors.white, size: 20),
          const SizedBox(height: 4),
          Text(
            value,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
          ),
          Text(
            label,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 12,
              fontWeight: FontWeight.w500,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }

  Widget _buildCursosList() {
    if (isLoading) {
      return const Center(child: LoadingSpinner());
    }

    if (error != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.error_outline,
              size: 64,
              color: AppColors.error,
            ),
            const SizedBox(height: 16),
            Text(
              'Error al cargar cursos',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: AppColors.textPrimary,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              error!,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: AppColors.textSecondary,
              ),
            ),
            const SizedBox(height: 16),
            CustomButton(
              text: 'Reintentar',
              onPressed: _loadData,
              variant: ButtonVariant.primary,
            ),
          ],
        ),
      );
    }

    if (cursos.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.class_outlined,
              size: 64,
              color: AppColors.textSecondary,
            ),
            const SizedBox(height: 16),
            Text(
              'No hay cursos registrados',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: AppColors.textPrimary,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              'Crea el primer curso para este año lectivo',
              style: TextStyle(
                color: AppColors.textSecondary,
              ),
            ),
            const SizedBox(height: 16),
            CustomButton(
              text: 'Crear Primer Curso',
              iconData: Icons.add,
              onPressed: _showCreateCursoDialog,
              variant: ButtonVariant.secondary,
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: cursos.length,
      itemBuilder: (context, index) {
        final curso = cursos[index];
        return Card(
          margin: const EdgeInsets.only(bottom: 12),
          elevation: 2,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          child: ExpansionTile(
            leading: CircleAvatar(
              backgroundColor: AppColors.secondary,
              child: Text(
                curso['grado']?.toString() ?? '?',
                style: const TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            title: Text(
              '${curso['grado']}° ${curso['seccion'] ?? ''}',
              style: const TextStyle(
                fontWeight: FontWeight.bold,
              ),
            ),
            subtitle: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Turno: ${curso['turno'] ?? 'No especificado'}'),
                Text(
                  'ID: ${curso['id']}',
                  style: TextStyle(
                    fontSize: 12,
                    color: AppColors.textSecondary,
                  ),
                ),
              ],
            ),
            children: [
              Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Materias del Curso',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: AppColors.textPrimary,
                      ),
                    ),
                    const SizedBox(height: 8),
                    // TODO: Mostrar materias del curso
                    Text(
                      'Funcionalidad de materias disponible próximamente',
                      style: TextStyle(
                        color: AppColors.textSecondary,
                      ),
                    ),
                    const SizedBox(height: 12),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        CustomButton(
                          text: 'Ver Participantes',
                          onPressed: () {
                            // TODO: Navegar a participantes del curso
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                content: Text('Función disponible próximamente'),
                                backgroundColor: AppColors.secondary,
                              ),
                            );
                          },
                          variant: ButtonVariant.outline,
                          size: ButtonSize.small,
                        ),
                        const SizedBox(width: 8),
                        CustomButton(
                          text: 'Gestionar',
                          onPressed: () {
                            // TODO: Navegar a gestión del curso
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                content: Text('Función disponible próximamente'),
                                backgroundColor: AppColors.primary,
                              ),
                            );
                          },
                          variant: ButtonVariant.primary,
                          size: ButtonSize.small,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}

// Dialog para crear nuevo curso
class CreateCursoDialog extends StatefulWidget {
  final dynamic anioId;
  
  const CreateCursoDialog({super.key, required this.anioId});

  @override
  State<CreateCursoDialog> createState() => _CreateCursoDialogState();
}

class _CreateCursoDialogState extends State<CreateCursoDialog> {
  final _formKey = GlobalKey<FormState>();
  final _gradoController = TextEditingController();
  final _seccionController = TextEditingController();
  String _turno = 'Mañana';

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Nuevo Curso'),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _gradoController,
              decoration: const InputDecoration(
                labelText: 'Grado',
                hintText: 'Ej: 1, 2, 3...',
                border: OutlineInputBorder(),
              ),
              keyboardType: TextInputType.number,
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'El grado es requerido';
                }
                final grado = int.tryParse(value);
                if (grado == null || grado < 1 || grado > 12) {
                  return 'Ingresa un grado válido (1-12)';
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _seccionController,
              decoration: const InputDecoration(
                labelText: 'Sección',
                hintText: 'Ej: A, B, C...',
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'La sección es requerida';
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            DropdownButtonFormField<String>(
              value: _turno,
              decoration: const InputDecoration(
                labelText: 'Turno',
                border: OutlineInputBorder(),
              ),
              items: const [
                DropdownMenuItem(value: 'Mañana', child: Text('Mañana')),
                DropdownMenuItem(value: 'Tarde', child: Text('Tarde')),
                DropdownMenuItem(value: 'Noche', child: Text('Noche')),
              ],
              onChanged: (value) {
                if (value != null) {
                  setState(() => _turno = value);
                }
              },
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        ElevatedButton(
          onPressed: () {
            if (_formKey.currentState!.validate()) {
              Navigator.of(context).pop({
                'grado': int.parse(_gradoController.text.trim()),
                'seccion': _seccionController.text.trim().toUpperCase(),
                'turno': _turno,
                'idAnio': widget.anioId,
              });
            }
          },
          child: const Text('Crear'),
        ),
      ],
    );
  }

  @override
  void dispose() {
    _gradoController.dispose();
    _seccionController.dispose();
    super.dispose();
  }
}
