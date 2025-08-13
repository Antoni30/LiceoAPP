import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../constants/app_styles.dart';
import '../../services/anio_academico_service.dart';
import '../../widgets/custom_button.dart';
import '../../widgets/loading_spinner.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final AnioAcademicoService _anioService = AnioAcademicoService();
  List<Map<String, dynamic>> aniosLectivos = [];
  Map<String, dynamic>? anioActivo;
  bool isLoading = true;
  String? error;

  @override
  void initState() {
    super.initState();
    _loadAniosLectivos();
  }

  Future<void> _loadAniosLectivos() async {
    try {
      setState(() {
        isLoading = true;
        error = null;
      });

      final response = await _anioService.getAllAniosAcademicos();
      final List<Map<String, dynamic>> anios = List<Map<String, dynamic>>.from(response);
      
      // Buscar el año activo
      Map<String, dynamic>? activo;
      for (var anio in anios) {
        if (anio['activo'] == true) {
          activo = anio;
          break;
        }
      }

      setState(() {
        aniosLectivos = anios;
        anioActivo = activo;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        error = e.toString();
        isLoading = false;
      });
    }
  }

  Future<void> _showCreateAnioDialog() async {
    final result = await showDialog<Map<String, dynamic>>(
      context: context,
      builder: (context) => const CreateAnioLectivoDialog(),
    );

    if (result != null) {
      try {
        await _anioService.createAnioAcademico(result);
        _loadAniosLectivos(); // Recargar la lista
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Año lectivo creado exitosamente'),
              backgroundColor: AppColors.success,
            ),
          );
        }
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Error al crear año lectivo: $e'),
              backgroundColor: AppColors.error,
            ),
          );
        }
      }
    }
  }

  Future<void> _activarAnio(Map<String, dynamic> anio) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar activación'),
        content: Text('¿Estás seguro de que quieres activar el año lectivo "${anio['nombre']}"?\n\nEsto desactivará el año actual.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () => Navigator.of(context).pop(true),
            child: const Text('Activar', style: TextStyle(color: AppColors.success)),
          ),
        ],
      ),
    );

    if (confirm == true) {
      try {
        await _anioService.activarAnioAcademico(anio['id']);
        _loadAniosLectivos(); // Recargar la lista
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Año lectivo activado exitosamente'),
              backgroundColor: AppColors.success,
            ),
          );
        }
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Error al activar año lectivo: $e'),
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
        title: const Text('Gestión de Años Lectivos'),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      body: Column(
        children: [
          // Header con año activo
          if (anioActivo != null) _buildAnioActivoCard(),
          
          // Lista de años lectivos
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
                      color: AppColors.primary.withOpacity(0.1),
                      borderRadius: const BorderRadius.only(
                        topLeft: Radius.circular(12),
                        topRight: Radius.circular(12),
                      ),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          'Todos los Años Lectivos',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            color: AppColors.textPrimary,
                          ),
                        ),
                        CustomButton(
                          text: 'Nuevo Año',
                          iconData: Icons.add,
                          onPressed: _showCreateAnioDialog,
                          variant: ButtonVariant.primary,
                          size: ButtonSize.small,
                        ),
                      ],
                    ),
                  ),
                  
                  // Lista
                  Expanded(child: _buildAniosLectivosList()),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAnioActivoCard() {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [AppColors.success, AppColors.success.withOpacity(0.8)],
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
                Icons.school,
                color: Colors.white,
                size: 28,
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Año Lectivo Activo',
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
                child: _buildInfoItem(
                  'Fecha Inicio',
                  anioActivo!['fechaInicio'] ?? 'No definida',
                  Icons.calendar_today,
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: _buildInfoItem(
                  'Fecha Fin',
                  anioActivo!['fechaFin'] ?? 'No definida',
                  Icons.event,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildInfoItem(String label, String value, IconData icon) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.2),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icon, color: Colors.white, size: 16),
              const SizedBox(width: 4),
              Text(
                label,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 12,
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
          const SizedBox(height: 4),
          Text(
            value,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 14,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAniosLectivosList() {
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
              'Error al cargar años lectivos',
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
              onPressed: _loadAniosLectivos,
              variant: ButtonVariant.primary,
            ),
          ],
        ),
      );
    }

    if (aniosLectivos.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.calendar_today_outlined,
              size: 64,
              color: AppColors.textSecondary,
            ),
            const SizedBox(height: 16),
            Text(
              'No hay años lectivos registrados',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: AppColors.textPrimary,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              'Crea el primer año lectivo del sistema',
              style: TextStyle(
                color: AppColors.textSecondary,
              ),
            ),
            const SizedBox(height: 16),
            CustomButton(
              text: 'Crear Primer Año',
              iconData: Icons.add,
              onPressed: _showCreateAnioDialog,
              variant: ButtonVariant.primary,
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: aniosLectivos.length,
      itemBuilder: (context, index) {
        final anio = aniosLectivos[index];
        final isActive = anio['activo'] == true;
        
        return Card(
          margin: const EdgeInsets.only(bottom: 12),
          elevation: isActive ? 4 : 2,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
            side: isActive 
              ? BorderSide(color: AppColors.success, width: 2)
              : BorderSide.none,
          ),
          child: ListTile(
            leading: CircleAvatar(
              backgroundColor: isActive ? AppColors.success : AppColors.primary,
              child: Icon(
                isActive ? Icons.school : Icons.calendar_today,
                color: Colors.white,
              ),
            ),
            title: Row(
              children: [
                Expanded(
                  child: Text(
                    anio['nombre'] ?? 'Sin nombre',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      color: isActive ? AppColors.success : AppColors.textPrimary,
                    ),
                  ),
                ),
                if (isActive)
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                    decoration: BoxDecoration(
                      color: AppColors.success,
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: const Text(
                      'ACTIVO',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 10,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
              ],
            ),
            subtitle: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 4),
                Text('${anio['fechaInicio']} - ${anio['fechaFin']}'),
                const SizedBox(height: 4),
                Text(
                  'ID: ${anio['id']}',
                  style: TextStyle(
                    fontSize: 12,
                    color: AppColors.textSecondary,
                  ),
                ),
              ],
            ),
            trailing: !isActive 
              ? IconButton(
                  icon: const Icon(Icons.play_arrow, color: AppColors.success),
                  tooltip: 'Activar año lectivo',
                  onPressed: () => _activarAnio(anio),
                )
              : const Icon(Icons.check_circle, color: AppColors.success),
          ),
        );
      },
    );
  }
}

// Dialog para crear nuevo año lectivo
class CreateAnioLectivoDialog extends StatefulWidget {
  const CreateAnioLectivoDialog({super.key});

  @override
  State<CreateAnioLectivoDialog> createState() => _CreateAnioLectivoDialogState();
}

class _CreateAnioLectivoDialogState extends State<CreateAnioLectivoDialog> {
  final _formKey = GlobalKey<FormState>();
  final _nombreController = TextEditingController();
  DateTime? _fechaInicio;
  DateTime? _fechaFin;

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Nuevo Año Lectivo'),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _nombreController,
              decoration: const InputDecoration(
                labelText: 'Nombre del año lectivo',
                hintText: 'Ej: 2024-2025',
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'El nombre es requerido';
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: InkWell(
                    onTap: () async {
                      final date = await showDatePicker(
                        context: context,
                        initialDate: DateTime.now(),
                        firstDate: DateTime(2020),
                        lastDate: DateTime(2030),
                      );
                      if (date != null) {
                        setState(() => _fechaInicio = date);
                      }
                    },
                    child: InputDecorator(
                      decoration: const InputDecoration(
                        labelText: 'Fecha de inicio',
                        border: OutlineInputBorder(),
                      ),
                      child: Text(
                        _fechaInicio != null 
                          ? '${_fechaInicio!.day}/${_fechaInicio!.month}/${_fechaInicio!.year}'
                          : 'Seleccionar fecha',
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: InkWell(
                    onTap: () async {
                      final date = await showDatePicker(
                        context: context,
                        initialDate: _fechaInicio ?? DateTime.now(),
                        firstDate: _fechaInicio ?? DateTime(2020),
                        lastDate: DateTime(2030),
                      );
                      if (date != null) {
                        setState(() => _fechaFin = date);
                      }
                    },
                    child: InputDecorator(
                      decoration: const InputDecoration(
                        labelText: 'Fecha de fin',
                        border: OutlineInputBorder(),
                      ),
                      child: Text(
                        _fechaFin != null 
                          ? '${_fechaFin!.day}/${_fechaFin!.month}/${_fechaFin!.year}'
                          : 'Seleccionar fecha',
                      ),
                    ),
                  ),
                ),
              ],
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
              if (_fechaInicio == null || _fechaFin == null) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Debes seleccionar ambas fechas'),
                    backgroundColor: AppColors.error,
                  ),
                );
                return;
              }
              
              Navigator.of(context).pop({
                'nombre': _nombreController.text.trim(),
                'fechaInicio': '${_fechaInicio!.year}-${_fechaInicio!.month.toString().padLeft(2, '0')}-${_fechaInicio!.day.toString().padLeft(2, '0')}',
                'fechaFin': '${_fechaFin!.year}-${_fechaFin!.month.toString().padLeft(2, '0')}-${_fechaFin!.day.toString().padLeft(2, '0')}',
                'activo': false, // Por defecto no activo
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
    _nombreController.dispose();
    super.dispose();
  }
}
