import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../constants/app_styles.dart';
import '../../providers/auth_provider.dart';
import '../../widgets/custom_button.dart';
import '../../widgets/custom_text_field.dart';
import '../../models/user_model.dart';

class UsersManagementScreen extends StatefulWidget {
  const UsersManagementScreen({super.key});

  @override
  State<UsersManagementScreen> createState() => _UsersManagementScreenState();
}

class _UsersManagementScreenState extends State<UsersManagementScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<User> _users = [];
  List<User> _filteredUsers = [];
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _loadUsers();
    _searchController.addListener(_filterUsers);
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _loadUsers() async {
    setState(() {
      _isLoading = true;
    });

    // TODO: Implementar llamada real al API
    // Datos de ejemplo por ahora
    await Future.delayed(const Duration(seconds: 1));
    
    setState(() {
      _users = [
        User(
          idUsuario: 'admin001',
          nombre: 'Juan Carlos',
          apellido: 'Rodríguez',
          email: 'admin@liceo.edu',
          telefono: '555-0001',
          roles: ['administrador'],
        ),
        User(
          idUsuario: 'prof001',
          nombre: 'María Elena',
          apellido: 'González',
          email: 'maria.gonzalez@liceo.edu',
          telefono: '555-0002',
          roles: ['profesor'],
        ),
        User(
          idUsuario: 'prof002',
          nombre: 'Carlos Alberto',
          apellido: 'Martínez',
          email: 'carlos.martinez@liceo.edu',
          telefono: '555-0003',
          roles: ['profesor'],
        ),
      ];
      _filteredUsers = List.from(_users);
      _isLoading = false;
    });
  }

  void _filterUsers() {
    final query = _searchController.text.toLowerCase();
    setState(() {
      _filteredUsers = _users.where((user) {
        return user.nombre.toLowerCase().contains(query) ||
               user.apellido.toLowerCase().contains(query) ||
               user.email.toLowerCase().contains(query) ||
               user.idUsuario.toLowerCase().contains(query);
      }).toList();
    });
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Gestión de Usuarios'),
        actions: [
          IconButton(
            onPressed: () {
              // TODO: Implementar navegación a crear usuario
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Crear usuario - Por implementar'),
                  backgroundColor: AppColors.primary,
                ),
              );
            },
            icon: const Icon(Icons.person_add),
          ),
        ],
      ),
      body: Consumer<AuthProvider>(
        builder: (context, authProvider, child) {
          // Verificar permisos
          if (!authProvider.isAdmin) {
            return const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.block,
                    size: 64,
                    color: AppColors.error,
                  ),
                  SizedBox(height: AppStyles.spacing4),
                  Text(
                    'No tienes permisos para acceder a esta sección',
                    style: AppStyles.headingMedium,
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            );
          }

          return RefreshIndicator(
            onRefresh: _loadUsers,
            child: Column(
              children: [
                // Barra de búsqueda
                Container(
                  padding: const EdgeInsets.all(AppStyles.spacing3),
                  color: AppColors.surface,
                  child: LayoutBuilder(
                    builder: (context, constraints) {
                      return Row(
                        children: [
                          Expanded(
                            child: CustomTextField(
                              hintText: 'Buscar usuarios...',
                              controller: _searchController,
                              autofocus: false,
                          maxLength: 100,
                          prefixIcon: const Icon(
                            Icons.search,
                            color: AppColors.textLight,
                          ),
                        ),
                      ),
                      const SizedBox(width: AppStyles.spacing3),
                      Container(
                        decoration: BoxDecoration(
                          color: AppColors.primary,
                          borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                        ),
                        child: IconButton(
                          onPressed: _loadUsers,
                          icon: const Icon(
                            Icons.refresh,
                            color: Colors.white,
                          ),
                        ),
                      ),
                        ],
                      );
                    }
                  ),
                ),

                // Estadísticas
                Container(
                  padding: const EdgeInsets.all(AppStyles.spacing3),
                  color: AppColors.surface,
                  child: LayoutBuilder(
                    builder: (context, constraints) {
                      if (constraints.maxWidth < 500) {
                        return Column(
                          children: [
                            Row(
                              children: [
                                Expanded(
                                  child: _StatCard(
                                    title: 'Total',
                                    value: _users.length.toString(),
                                    color: AppColors.primary,
                                  ),
                                ),
                                const SizedBox(width: AppStyles.spacing2),
                                Expanded(
                                  child: _StatCard(
                                    title: 'Admins',
                                    value: _users.where((u) => u.isAdmin).length.toString(),
                                    color: AppColors.purple,
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: AppStyles.spacing2),
                            _StatCard(
                              title: 'Profesores',
                              value: _users.where((u) => u.isProfesor).length.toString(),
                              color: AppColors.success,
                            ),
                          ],
                        );
                      }
                      return Row(
                        children: [
                          Expanded(
                            child: _StatCard(
                              title: 'Total',
                              value: _users.length.toString(),
                              color: AppColors.primary,
                            ),
                          ),
                          const SizedBox(width: AppStyles.spacing3),
                          Expanded(
                            child: _StatCard(
                              title: 'Admins',
                              value: _users.where((u) => u.isAdmin).length.toString(),
                              color: AppColors.purple,
                            ),
                          ),
                          const SizedBox(width: AppStyles.spacing3),
                          Expanded(
                            child: _StatCard(
                              title: 'Profesores',
                              value: _users.where((u) => u.isProfesor).length.toString(),
                              color: AppColors.success,
                            ),
                          ),
                        ],
                      );
                    }
                  ),
                ),

                // Lista de usuarios
                Expanded(
                  child: _isLoading
                      ? const Center(
                          child: CircularProgressIndicator(),
                        )
                      : _filteredUsers.isEmpty
                          ? Center(
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Icon(
                                    _searchController.text.isNotEmpty
                                        ? Icons.search_off
                                        : Icons.people_outline,
                                    size: 64,
                                    color: AppColors.textLight,
                                  ),
                                  const SizedBox(height: AppStyles.spacing4),
                                  Text(
                                    _searchController.text.isNotEmpty
                                        ? 'No se encontraron usuarios'
                                        : 'No hay usuarios disponibles',
                                    style: AppStyles.headingSmall.copyWith(
                                      color: AppColors.textLight,
                                    ),
                                  ),
                                ],
                              ),
                            )
                          : ListView.builder(
                              padding: const EdgeInsets.all(AppStyles.spacing3),
                              itemCount: _filteredUsers.length,
                              itemBuilder: (context, index) {
                                final user = _filteredUsers[index];
                                return _UserCard(
                                  user: user,
                                  onTap: () => _showUserDetails(user),
                                  onEdit: () => _editUser(user),
                                  onDelete: () => _deleteUser(user),
                                );
                              },
                            ),
                ),
              ],
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // TODO: Implementar navegación a crear usuario
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Crear usuario - Por implementar'),
              backgroundColor: AppColors.primary,
            ),
          );
        },
        backgroundColor: AppColors.primary,
        child: const Icon(Icons.add, color: Colors.white),
      ),
    );
  }

  void _showUserDetails(User user) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(
          top: Radius.circular(AppStyles.radiusLg),
        ),
      ),
      builder: (context) => _UserDetailsSheet(user: user),
    );
  }

  void _editUser(User user) {
    // TODO: Implementar navegación a editar usuario
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Editar usuario ${user.fullName} - Por implementar'),
        backgroundColor: AppColors.primary,
      ),
    );
  }

  void _deleteUser(User user) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar eliminación'),
        content: Text('¿Estás seguro de que quieres eliminar a ${user.fullName}?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Cancelar'),
          ),
          CustomButton(
            text: 'Eliminar',
            type: ButtonType.error,
            onPressed: () {
              Navigator.of(context).pop();
              // TODO: Implementar eliminación
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Usuario eliminado - Por implementar'),
                  backgroundColor: AppColors.error,
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

class _StatCard extends StatelessWidget {
  final String title;
  final String value;
  final Color color;

  const _StatCard({
    required this.title,
    required this.value,
    required this.color,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(AppStyles.spacing3),
        decoration: BoxDecoration(
          color: color.withValues(alpha: 0.1),
          borderRadius: BorderRadius.circular(AppStyles.radiusMd),
        ),
        child: Column(
          children: [
            Text(
              value,
              style: AppStyles.headingSmall.copyWith(
                color: color,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              title,
              style: AppStyles.bodySmall.copyWith(
                color: color,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _UserCard extends StatelessWidget {
  final User user;
  final VoidCallback onTap;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

  const _UserCard({
    required this.user,
    required this.onTap,
    required this.onEdit,
    required this.onDelete,
  });

  Color _getRoleColor(String role) {
    switch (role.toLowerCase()) {
      case 'administrador':
        return AppColors.purple;
      case 'profesor':
        return AppColors.primary;
      default:
        return AppColors.textSecondary;
    }
  }

  String _getRoleDisplayName(String role) {
    switch (role.toLowerCase()) {
      case 'administrador':
        return 'Admin';
      case 'profesor':
        return 'Profesor';
      default:
        return role;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: AppStyles.spacing3),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(AppStyles.radiusLg),
        child: Padding(
          padding: const EdgeInsets.all(AppStyles.spacing4),
          child: Row(
            children: [
              // Avatar
              CircleAvatar(
                radius: 24,
                backgroundColor: AppColors.primary.withValues(alpha: 0.1),
                child: Text(
                  user.nombre.isNotEmpty ? user.nombre[0].toUpperCase() : 'U',
                  style: const TextStyle(
                    color: AppColors.primary,
                    fontWeight: FontWeight.bold,
                    fontSize: 18,
                  ),
                ),
              ),
              
              const SizedBox(width: AppStyles.spacing4),
              
              // Información del usuario
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      user.fullName,
                      style: AppStyles.bodyLarge.copyWith(
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    const SizedBox(height: AppStyles.spacing1),
                    Text(
                      user.email,
                      style: AppStyles.bodyMedium.copyWith(
                        color: AppColors.textSecondary,
                      ),
                    ),
                    const SizedBox(height: AppStyles.spacing1),
                    Text(
                      'ID: ${user.idUsuario}',
                      style: AppStyles.bodySmall.copyWith(
                        color: AppColors.textLight,
                      ),
                    ),
                  ],
                ),
              ),
              
              // Roles y acciones
              Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: user.roles.map((role) {
                      final color = _getRoleColor(role);
                      return Container(
                        margin: const EdgeInsets.only(left: AppStyles.spacing1),
                        padding: const EdgeInsets.symmetric(
                          horizontal: AppStyles.spacing2,
                          vertical: AppStyles.spacing1,
                        ),
                        decoration: BoxDecoration(
                          color: color.withValues(alpha: 0.1),
                          borderRadius: BorderRadius.circular(AppStyles.radiusSm),
                        ),
                        child: Text(
                          _getRoleDisplayName(role),
                          style: AppStyles.bodySmall.copyWith(
                            color: color,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      );
                    }).toList(),
                  ),
                  
                  const SizedBox(height: AppStyles.spacing2),
                  
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        onPressed: onEdit,
                        icon: const Icon(Icons.edit, size: 20),
                        color: AppColors.primary,
                        constraints: const BoxConstraints(
                          minWidth: 32,
                          minHeight: 32,
                        ),
                      ),
                      IconButton(
                        onPressed: onDelete,
                        icon: const Icon(Icons.delete, size: 20),
                        color: AppColors.error,
                        constraints: const BoxConstraints(
                          minWidth: 32,
                          minHeight: 32,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }


}

class _UserDetailsSheet extends StatelessWidget {
  final User user;

  const _UserDetailsSheet({required this.user});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(AppStyles.spacing6),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header
          Row(
            children: [
              CircleAvatar(
                radius: 32,
                backgroundColor: AppColors.primary.withValues(alpha: 0.1),
                child: Text(
                  user.nombre.isNotEmpty ? user.nombre[0].toUpperCase() : 'U',
                  style: const TextStyle(
                    color: AppColors.primary,
                    fontWeight: FontWeight.bold,
                    fontSize: 24,
                  ),
                ),
              ),
              const SizedBox(width: AppStyles.spacing4),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      user.fullName,
                      style: AppStyles.headingMedium,
                    ),
                    Text(
                      user.email,
                      style: AppStyles.bodyMedium.copyWith(
                        color: AppColors.textSecondary,
                      ),
                    ),
                  ],
                ),
              ),
              IconButton(
                onPressed: () => Navigator.of(context).pop(),
                icon: const Icon(Icons.close),
              ),
            ],
          ),
          
          const SizedBox(height: AppStyles.spacing6),
          
          // Información detallada
          _DetailRow(label: 'ID Usuario', value: user.idUsuario),
          _DetailRow(label: 'Nombre', value: user.nombre),
          _DetailRow(label: 'Apellido', value: user.apellido),
          _DetailRow(label: 'Email', value: user.email),
          _DetailRow(label: 'Teléfono', value: user.telefono ?? 'No especificado'),
          
          const SizedBox(height: AppStyles.spacing4),
          
          Text(
            'Roles',
            style: AppStyles.bodyMedium.copyWith(
              fontWeight: FontWeight.w600,
              color: AppColors.textPrimary,
            ),
          ),
          const SizedBox(height: AppStyles.spacing2),
          Wrap(
            spacing: AppStyles.spacing2,
            children: user.roles.map((role) {
              return Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: AppStyles.spacing3,
                  vertical: AppStyles.spacing2,
                ),
                decoration: BoxDecoration(
                  color: AppColors.primary.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                ),
                child: Text(
                  role,
                  style: AppStyles.bodyMedium.copyWith(
                    color: AppColors.primary,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              );
            }).toList(),
          ),
          
          const SizedBox(height: AppStyles.spacing6),
          
          // Botones de acción
          Row(
            children: [
              Expanded(
                child: CustomButton(
                  text: 'Editar',
                  onPressed: () {
                    Navigator.of(context).pop();
                    // TODO: Implementar edición
                  },
                  icon: const Icon(Icons.edit, size: 20),
                ),
              ),
              const SizedBox(width: AppStyles.spacing3),
              Expanded(
                child: CustomButton(
                  text: 'Eliminar',
                  type: ButtonType.error,
                  onPressed: () {
                    Navigator.of(context).pop();
                    // TODO: Implementar eliminación
                  },
                  icon: const Icon(Icons.delete, size: 20),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _DetailRow extends StatelessWidget {
  final String label;
  final String value;

  const _DetailRow({
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: AppStyles.spacing2),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 80,
            child: Text(
              label,
              style: AppStyles.bodyMedium.copyWith(
                color: AppColors.textSecondary,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
          const SizedBox(width: AppStyles.spacing3),
          Expanded(
            child: Text(
              value,
              style: AppStyles.bodyMedium.copyWith(
                color: AppColors.textPrimary,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
