import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:go_router/go_router.dart';
import '../constants/app_styles.dart';
import '../providers/auth_provider.dart';
import '../widgets/custom_cards.dart';
import '../widgets/custom_button.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  Map<String, dynamic>? anioActivo;

  @override
  void initState() {
    super.initState();
    _loadAnioActivo();
  }

  Future<void> _loadAnioActivo() async {
    // TODO: Implementar la carga del año activo
    // Por ahora usamos datos de ejemplo
    setState(() {
      anioActivo = {
        'nombre': '2024-2025',
        'fechaInicio': '2024-02-01',
        'fechaFin': '2024-12-15',
      };
    });
  }

  List<Map<String, dynamic>> _getActionCards(AuthProvider authProvider) {
    final commonActions = [
      {
        'title': 'Mi Perfil',
        'description': 'Ver y editar tu información personal',
        'icon': Icons.person,
        'color': AppColors.primary,
        'path': '/perfil',
      },
    ];

    final profesorActions = [
      {
        'title': 'Estudiantes',
        'description': 'Gestionar de Estudiantes',
        'icon': Icons.group,
        'color': AppColors.primary,
        'path': '/profesor/estudiantes',
      },
      {
        'title': 'Generar Reporte',
        'description': 'Generación de Reporte de Notas de Estudiantes',
        'icon': Icons.assignment,
        'color': AppColors.success,
        'path': '/profesor/reporte-notas',
      },
      {
        'title': 'Generar Reporte Horario',
        'description': 'Generación de Reporte del Horario',
        'icon': Icons.schedule,
        'color': AppColors.secondary,
        'path': '/profesor/reporte-horario',
      },
    ];

    final estudianteActions = [
      {
        'title': 'Mis Cursos',
        'description': 'Ver mis cursos matriculados',
        'icon': Icons.school,
        'color': AppColors.primary,
        'path': '/estudiante/cursos',
      },
      {
        'title': 'Mis Notas',
        'description': 'Ver mis calificaciones',
        'icon': Icons.assessment,
        'color': AppColors.success,
        'path': '/estudiante/notas',
      },
      {
        'title': 'Horarios',
        'description': 'Ver mi horario de clases',
        'icon': Icons.schedule,
        'color': AppColors.secondary,
        'path': '/estudiante/horarios',
      },
      {
        'title': 'Tareas',
        'description': 'Ver tareas y trabajos pendientes',
        'icon': Icons.assignment,
        'color': AppColors.purple,
        'path': '/estudiante/tareas',
      },
    ];

    if (authProvider.isProfesor) {
      return [...commonActions, ...profesorActions];
    } else if (authProvider.isEstudiante) {
      return [...commonActions, ...estudianteActions];
    }

    // Retornar solo acciones comunes si no tiene rol específico
    return commonActions;
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        // Mostrar diálogo de confirmación para salir de la app
        final shouldExit = await showDialog<bool>(
          context: context,
          builder: (context) => AlertDialog(
            title: const Text('Salir de la aplicación'),
            content: const Text('¿Estás seguro de que quieres salir?'),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(false),
                child: const Text('Cancelar'),
              ),
              TextButton(
                onPressed: () => Navigator.of(context).pop(true),
                child: const Text('Salir'),
              ),
            ],
          ),
        );
        
        if (shouldExit == true) {
          SystemNavigator.pop();
        }
        return false;
      },
      child: Scaffold(
        backgroundColor: AppColors.background,
        appBar: AppBar(
          backgroundColor: AppColors.surface,
          elevation: 1,
          shadowColor: AppColors.shadow,
          title: Row(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: AppColors.primary.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                ),
                child: const Icon(
                  Icons.school,
                  color: AppColors.primary,
                  size: 24,
                ),
              ),
              const SizedBox(width: AppStyles.spacing3),
              Text(
                'Portal Académico',
                style: AppStyles.headingMedium.copyWith(
                  fontSize: AppStyles.textXl,
                  color: AppColors.primary,
                ),
              ),
            ],
          ),
          actions: [
            Consumer<AuthProvider>(
              builder: (context, authProvider, child) {
                return PopupMenuButton<String>(
                  icon: CircleAvatar(
                    backgroundColor: AppColors.primary.withOpacity(0.1),
                    child: Text(
                      authProvider.displayName.isNotEmpty
                          ? authProvider.displayName[0].toUpperCase()
                          : 'U',
                      style: const TextStyle(
                        color: AppColors.primary,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  onSelected: (value) async {
                    switch (value) {
                      case 'perfil':
                        context.go('/perfil');
                        break;
                      case 'logout':
                        await authProvider.logout();
                        if (context.mounted) {
                          context.go('/');
                        }
                        break;
                    }
                  },
                  itemBuilder: (context) => [
                    PopupMenuItem(
                      value: 'perfil',
                      child: Row(
                        children: [
                          const Icon(Icons.person, size: 20),
                          const SizedBox(width: AppStyles.spacing2),
                          Text('Mi Perfil', style: AppStyles.bodyMedium),
                        ],
                      ),
                    ),
                    const PopupMenuDivider(),
                    PopupMenuItem(
                      value: 'logout',
                      child: Row(
                        children: [
                          const Icon(Icons.logout, size: 20, color: AppColors.error),
                          const SizedBox(width: AppStyles.spacing2),
                          Text(
                            'Cerrar Sesión',
                            style: AppStyles.bodyMedium.copyWith(color: AppColors.error),
                          ),
                        ],
                      ),
                    ),
                  ],
                );
              },
            ),
            const SizedBox(width: AppStyles.spacing3),
          ],
        ),
        body: Consumer<AuthProvider>(
          builder: (context, authProvider, child) {
            final actionCards = _getActionCards(authProvider);

            return SingleChildScrollView(
              padding: const EdgeInsets.all(AppStyles.spacing5),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Header de bienvenida
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(AppStyles.spacing6),
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          AppColors.primary,
                          AppColors.primary.withOpacity(0.8),
                        ],
                      ),
                      borderRadius: BorderRadius.circular(AppStyles.radiusLg),
                      boxShadow: [
                        BoxShadow(
                          color: AppColors.primary.withOpacity(0.3),
                          blurRadius: AppStyles.elevationMd,
                          offset: const Offset(0, 4),
                        ),
                      ],
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          '¡Bienvenido, ${authProvider.roleName}!',
                          style: AppStyles.headingLarge.copyWith(
                            color: Colors.white,
                            fontSize: AppStyles.text2Xl,
                          ),
                        ),
                        const SizedBox(height: AppStyles.spacing2),
                        Text(
                          authProvider.displayName,
                          style: AppStyles.bodyLarge.copyWith(
                            color: Colors.white.withOpacity(0.9),
                            fontSize: AppStyles.textLg,
                          ),
                        ),
                        if (anioActivo != null) ...[
                          const SizedBox(height: AppStyles.spacing4),
                          Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: AppStyles.spacing3,
                              vertical: AppStyles.spacing2,
                            ),
                            decoration: BoxDecoration(
                              color: Colors.white.withOpacity(0.2),
                              borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                            ),
                            child: Text(
                              'Año Lectivo: ${anioActivo!['nombre']}',
                              style: AppStyles.bodyMedium.copyWith(
                                color: Colors.white,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ),
                        ],
                      ],
                    ),
                  ),
                  const SizedBox(height: AppStyles.spacing6),
                  // Estadísticas rápidas (solo para admin)
                  if (authProvider.isAdmin) ...[
                    Text(
                      'Resumen del Sistema',
                      style: AppStyles.headingMedium,
                    ),
                    const SizedBox(height: AppStyles.spacing4),
                    Row(
                      children: [
                        Expanded(
                          child: InfoCard(
                            title: 'Usuarios',
                            value: '125',
                            icon: Icons.people,
                            color: AppColors.primary,
                          ),
                        ),
                        const SizedBox(width: AppStyles.spacing3),
                        Expanded(
                          child: InfoCard(
                            title: 'Materias',
                            value: '18',
                            icon: Icons.book,
                            color: AppColors.success,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: AppStyles.spacing3),
                    Row(
                      children: [
                        Expanded(
                          child: InfoCard(
                            title: 'Cursos',
                            value: '12',
                            icon: Icons.class_,
                            color: AppColors.purple,
                          ),
                        ),
                        const SizedBox(width: AppStyles.spacing3),
                        Expanded(
                          child: InfoCard(
                            title: 'Profesores',
                            value: '24',
                            icon: Icons.school,
                            color: AppColors.secondary,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: AppStyles.spacing6),
                  ],
                  // Acciones disponibles
                  Text(
                    'Acciones Disponibles',
                    style: AppStyles.headingMedium,
                  ),
                  const SizedBox(height: AppStyles.spacing4),
                  GridView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 1.2,
                      crossAxisSpacing: AppStyles.spacing3,
                      mainAxisSpacing: AppStyles.spacing3,
                    ),
                    itemCount: actionCards.length,
                    itemBuilder: (context, index) {
                      final card = actionCards[index];
                      return ActionCard(
                        title: card['title'],
                        description: card['description'],
                        icon: card['icon'],
                        color: card['color'],
                        onTap: () {
                          context.push(card['path']);
                        },
                      );
                    },
                  ),
                  const SizedBox(height: AppStyles.spacing6),
                ],
              ),
            );
          },
        ),
      ), // Cierre del Scaffold
    );
  }
}