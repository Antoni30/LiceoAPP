import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../screens/auth/login_screen.dart';
import '../screens/home_screen.dart';
import '../screens/profile_screen.dart';
import '../screens/profile_edit_screen.dart';
import '../screens/profesor/asignar_notas_screen.dart';
import '../screens/profesor/generar_reporte_screen.dart';
import '../screens/profesor/estudiantes_screen.dart';
import '../screens/profesor/detalle_estudiante_screen.dart';
import '../screens/estudiante/estudiante_detalle_screen.dart';

class AppRouter {
  static GoRouter createRouter() {
    return GoRouter(
      initialLocation: '/',
      redirect: (context, state) {
        final authProvider = context.read<AuthProvider>();
        final isLoggedIn = authProvider.isLoggedIn;
        final isLoginRoute = state.matchedLocation == '/';

        // Si no está logueado y no está en login, redirigir a login
        if (!isLoggedIn && !isLoginRoute) {
          return '/';
        }

        // Si está logueado y está en login, redirigir según rol
        if (isLoggedIn && isLoginRoute) {
          if (authProvider.isEstudiante) return '/estudiante';
          return '/home';
        }

        return null; // No redirigir
      },
      routes: [
        // Ruta de login
        GoRoute(path: '/', name: 'login', builder: (context, state) => const LoginScreen()),

        // Ruta de home
        GoRoute(path: '/home', name: 'home', builder: (context, state) => const HomeScreen()),

        // Ruta de perfil
        GoRoute(
          path: '/perfil',
          name: 'perfil',
          builder: (context, state) => const ProfileScreen(),
        ),

        // Ruta de edición de perfil
        GoRoute(
          path: '/profile/edit',
          name: 'profile-edit',
          builder: (context, state) {
            final userData = state.extra as Map<String, dynamic>;
            return ProfileEditScreen(userData: userData);
          },
        ),

        // Rutas de profesor
        GoRoute(
          path: '/profesor/estudiantes',
          name: 'profesor-estudiantes',
          builder: (context, state) => const EstudiantesScreen(),
        ),

        // Rutas de estudiantes
        GoRoute(
          path: '/estudiante',
          name: 'estudiante-detalle',
          builder: (context, state) => const EstudianteDetalleScreen(),
        ),
        GoRoute(
          path: '/estudiante/cursos',
          name: 'estudiante-cursos',
          builder: (context, state) => const EstudianteCursosScreen(),
        ),

        GoRoute(
          path: '/estudiante/notas',
          name: 'estudiante-notas',
          builder: (context, state) => const EstudianteNotasScreen(),
        ),

        GoRoute(
          path: '/estudiante/horarios',
          name: 'estudiante-horarios',
          builder: (context, state) => const EstudianteHorariosScreen(),
        ),

        GoRoute(
          path: '/estudiante/tareas',
          name: 'estudiante-tareas',
          builder: (context, state) => const EstudianteTareasScreen(),
        ),

        // Ruta de asignar notas
        GoRoute(
          path: '/profesor/asignar-notas/:idUsuario/:idCurso',
          name: 'asignar-notas',
          builder: (context, state) {
            final idUsuario = state.pathParameters['idUsuario']!;
            final idCurso = state.pathParameters['idCurso']!;
            return AsignarNotasScreen(idUsuario: idUsuario, idCurso: idCurso);
          },
        ),

        // Ruta de generar reporte
        GoRoute(
          path: '/profesor/generar-reporte/:idProfesor',
          name: 'generar-reporte',
          builder: (context, state) {
            final idProfesor = state.pathParameters['idProfesor']!;
            return GenerarReporteScreen(idProfesor: idProfesor);
          },
        ),

        // Ruta de detalle de estudiante
        GoRoute(
          path: '/profesor/detalle-estudiante/:idUsuario/:idCurso',
          name: 'detalle-estudiante',
          builder: (context, state) {
            final idUsuario = state.pathParameters['idUsuario']!;
            final idCurso = state.pathParameters['idCurso']!;
            return DetalleEstudianteScreen(idUsuario: idUsuario, idCurso: idCurso);
          },
        ),

        // Ruta de no autorizado
        GoRoute(
          path: '/no-autorizado',
          name: 'no-autorizado',
          builder: (context, state) => const NoAutorizedScreen(),
        ),

        // Ruta 404
        GoRoute(
          path: '/not-found',
          name: 'not-found',
          builder: (context, state) => const NotFoundScreen(),
        ),
      ],
      errorBuilder: (context, state) => const NotFoundScreen(),
    );
  }
}

//
// Placeholder screens - estas pantallas las implementaremos después
//

// Widget helper para pantallas con navegación al home
class DashboardScreen extends StatelessWidget {
  final String title;
  final String content;

  const DashboardScreen({super.key, required this.title, required this.content});

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: true,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          context.pop();
        }
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text(title),
          leading: IconButton(icon: const Icon(Icons.arrow_back), onPressed: () => context.pop()),
        ),
        body: Center(child: Text(content)),
      ),
    );
  }
}

// Screens de Profesor
class ProfesorEstudiantesScreen extends StatelessWidget {
  const ProfesorEstudiantesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(title: 'Estudiantes', content: 'Gestionar de Estudiantes');
  }
}

class ProfesorReporteNotasScreen extends StatelessWidget {
  const ProfesorReporteNotasScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(
      title: 'Generar Reporte',
      content: 'Generación de Reporte de Notas de Estudiantes',
    );
  }
}

class ProfesorReporteHorarioScreen extends StatelessWidget {
  const ProfesorReporteHorarioScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(
      title: 'Generar Reporte Horario',
      content: 'Generación de Reporte del Horario',
    );
  }
}

// Screens de Estudiante
class EstudianteCursosScreen extends StatelessWidget {
  const EstudianteCursosScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(title: 'Mis Cursos', content: 'Mis Cursos Matriculados');
  }
}

class EstudianteNotasScreen extends StatelessWidget {
  const EstudianteNotasScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(title: 'Mis Notas', content: 'Mis Calificaciones');
  }
}

class EstudianteHorariosScreen extends StatelessWidget {
  const EstudianteHorariosScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(title: 'Horarios', content: 'Mi Horario de Clases');
  }
}

class EstudianteTareasScreen extends StatelessWidget {
  const EstudianteTareasScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const DashboardScreen(title: 'Tareas', content: 'Mis Tareas y Trabajos');
  }
}

class NoAutorizedScreen extends StatelessWidget {
  const NoAutorizedScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: true,
      child: Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.block, size: 64, color: Colors.red),
              const SizedBox(height: 16),
              const Text(
                'No Autorizado',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              const Text('No tienes permisos para acceder a esta página'),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () => context.go('/home'),
                child: const Text('Volver al Inicio'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class NotFoundScreen extends StatelessWidget {
  const NotFoundScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: true,
      child: Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.error_outline, size: 64, color: Colors.orange),
              const SizedBox(height: 16),
              const Text(
                'Página no encontrada',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              const Text('La página que buscas no existe'),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () => context.go('/home'),
                child: const Text('Volver al Inicio'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
