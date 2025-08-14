import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:go_router/go_router.dart';
import '../../constants/app_styles.dart';
import '../../providers/auth_provider.dart';
import '../../widgets/custom_button.dart';
import '../../widgets/custom_text_field.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _idUsuarioController = TextEditingController();
  final _contrasenaController = TextEditingController();

  @override
  void dispose() {
    _idUsuarioController.dispose();
    _contrasenaController.dispose();
    super.dispose();
  }

  Future<void> _handleSubmit() async {
    if (!_formKey.currentState!.validate()) return;

    final authProvider = context.read<AuthProvider>();

    try {
      final result = await authProvider.login(
        _idUsuarioController.text.trim(),
        _contrasenaController.text,
      );

      print('🔍 [LoginScreen] Resultado del login: $result');

      if (!mounted) return;

      print(
        '🔍 [LoginScreen] Evaluando resultado: requiresVerification = ${result['requiresVerification']}',
      );
      print(
        '🔍 [LoginScreen] Tipo de requiresVerification: ${result['requiresVerification'].runtimeType}',
      );
      print('🔍 [LoginScreen] Comparación == true: ${result['requiresVerification'] == true}');
      print('🔍 [LoginScreen] Valor truthiness: ${result['requiresVerification']}');

      if (result['requiresVerification'] != null && result['requiresVerification']) {
        print('✅ [LoginScreen] Mostrando modal de verificación');
        print(
          '🔍 [LoginScreen] idUsuario recibido: ${result['idUsuario']} (tipo: ${result['idUsuario'].runtimeType})',
        );
        final idUsuarioStr = result['idUsuario'].toString();
        print('🔍 [LoginScreen] idUsuario convertido: $idUsuarioStr');
        _showVerificationModal(idUsuarioStr);
      } else if (result['success'] != null && result['success']) {
        print('✅ [LoginScreen] Login exitoso, determinando ruta por rol');
        final auth = context.read<AuthProvider>();
        if (auth.isEstudiante) {
          print('🎯 [LoginScreen] Rol Estudiante detectado, navegando a /estudiante');
          context.go('/estudiante');
        } else {
          print('🎯 [LoginScreen] Rol no estudiante, navegando a /home');
          context.go('/home');
        }
      }
    } catch (e) {
      print('❌ [LoginScreen] Error en login: $e');
    }
  }

  void _showVerificationModal(String idUsuario) {
    print('🎯 [LoginScreen] Intentando mostrar modal para usuario: $idUsuario');
    print('🎯 [LoginScreen] Context mounted: $mounted');
    print('🎯 [LoginScreen] Context widget: ${context.widget}');

    try {
      print('🎯 [LoginScreen] Llamando showDialog...');
      final dialogFuture = showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext dialogContext) {
          print('🎯 [LoginScreen] Builder del dialog ejecutándose...');
          print('🎯 [LoginScreen] Dialog context: $dialogContext');

          return Dialog(
            child: Container(
              padding: const EdgeInsets.all(20),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Text('Verificación MFA'),
                  const SizedBox(height: 16),
                  Text('Usuario: $idUsuario'),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () {
                      print('🎯 [LoginScreen] Botón cerrar presionado');
                      Navigator.of(dialogContext).pop();
                    },
                    child: const Text('Cerrar'),
                  ),
                ],
              ),
            ),
          );
        },
      );

      print('✅ [LoginScreen] showDialog completado, Future: $dialogFuture');

      dialogFuture
          .then((result) {
            print('🎯 [LoginScreen] Dialog cerrado con resultado: $result');
          })
          .catchError((error) {
            print('❌ [LoginScreen] Error en dialog: $error');
          });
    } catch (e, stackTrace) {
      print('❌ [LoginScreen] Error al mostrar modal: $e');
      print('❌ [LoginScreen] Stack trace: $stackTrace');
    }
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          if (GoRouter.of(context).canPop()) {
            context.pop();
          } else {
            SystemNavigator.pop();
          }
        }
      },
      child: Scaffold(
        backgroundColor: AppColors.background,
        appBar: AppBar(
          backgroundColor: Colors.transparent,
          elevation: 0,
          leading: IconButton(
            icon: const Icon(Icons.arrow_back, color: AppColors.textPrimary),
            onPressed: () {
              if (GoRouter.of(context).canPop()) {
                context.pop();
              } else {
                SystemNavigator.pop();
              }
            },
          ),
        ),
        body: SafeArea(
          child: Consumer<AuthProvider>(
            builder: (context, authProvider, child) {
              return SingleChildScrollView(
                padding: const EdgeInsets.all(AppStyles.spacing6),
                child: LayoutBuilder(
                  builder: (context, constraints) {
                    final screenHeight = MediaQuery.of(context).size.height;
                    final availableHeight =
                        screenHeight -
                        MediaQuery.of(context).padding.top -
                        MediaQuery.of(context).padding.bottom;

                    return ConstrainedBox(
                      constraints: BoxConstraints(minHeight: availableHeight),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(height: screenHeight * 0.02),
                          Container(
                            constraints: const BoxConstraints(maxWidth: 400),
                            padding: const EdgeInsets.all(AppStyles.spacing6),
                            decoration: BoxDecoration(
                              color: AppColors.surface,
                              borderRadius: BorderRadius.circular(AppStyles.radiusXl),
                              boxShadow: [
                                BoxShadow(
                                  color: AppColors.shadow,
                                  blurRadius: AppStyles.elevationLg,
                                  offset: const Offset(0, 4),
                                ),
                              ],
                              border: Border.all(color: AppColors.borderLight, width: 1),
                            ),
                            child: Form(
                              key: _formKey,
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  Column(
                                    children: [
                                      Container(
                                        width: 72,
                                        height: 72,
                                        decoration: BoxDecoration(
                                          color: AppColors.primary.withValues(alpha: 0.1),
                                          borderRadius: BorderRadius.circular(AppStyles.radiusLg),
                                        ),
                                        child: const Icon(
                                          Icons.school,
                                          size: 36,
                                          color: AppColors.primary,
                                        ),
                                      ),
                                      const SizedBox(height: AppStyles.spacing4),
                                      Text(
                                        'Portal Académico',
                                        style: AppStyles.headingLarge.copyWith(
                                          fontSize: AppStyles.textXl,
                                        ),
                                        textAlign: TextAlign.center,
                                      ),
                                      const SizedBox(height: AppStyles.spacing2),
                                      Text(
                                        'Inicia sesión con tu ID institucional',
                                        style: AppStyles.bodySmall,
                                        textAlign: TextAlign.center,
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: AppStyles.spacing5),
                                  Column(
                                    children: [
                                      CustomTextField(
                                        hintText: 'ID Usuario',
                                        controller: _idUsuarioController,
                                        maxLength: 50,
                                        autofocus: false,
                                        prefixIcon: const Icon(
                                          Icons.person_outline,
                                          color: AppColors.textLight,
                                        ),
                                        validator: (value) {
                                          if (value == null || value.isEmpty) {
                                            return 'Ingresa tu ID de usuario';
                                          }
                                          return null;
                                        },
                                      ),
                                      const SizedBox(height: AppStyles.spacing4),
                                      CustomTextField(
                                        hintText: 'Contraseña',
                                        controller: _contrasenaController,
                                        obscureText: true,
                                        maxLength: 100,
                                        autofocus: false,
                                        prefixIcon: const Icon(
                                          Icons.lock_outline,
                                          color: AppColors.textLight,
                                        ),
                                        validator: (value) {
                                          if (value == null || value.isEmpty) {
                                            return 'Ingresa tu contraseña';
                                          }
                                          return null;
                                        },
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: AppStyles.spacing6),
                                  CustomButton(
                                    text: 'Ingresar',
                                    onPressed: _handleSubmit,
                                    isLoading: authProvider.isLoading,
                                    customSize: const Size(double.infinity, 48),
                                  ),
                                  if (authProvider.errorMessage != null) ...[
                                    const SizedBox(height: AppStyles.spacing4),
                                    Container(
                                      padding: const EdgeInsets.all(AppStyles.spacing3),
                                      decoration: BoxDecoration(
                                        color: AppColors.errorLight,
                                        borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                                      ),
                                      child: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          Text(
                                            authProvider.errorMessage!,
                                            style: AppStyles.bodyMedium.copyWith(
                                              color: AppColors.error,
                                              fontWeight: FontWeight.w500,
                                            ),
                                          ),
                                          if (authProvider.errorMessage!.contains(
                                            'no tiene acceso',
                                          )) ...[
                                            const SizedBox(height: AppStyles.spacing1),
                                            Text(
                                              'Solo usuarios con rol de Administrador o Profesor pueden acceder al sistema.',
                                              style: AppStyles.bodySmall.copyWith(
                                                color: AppColors.error,
                                              ),
                                            ),
                                          ],
                                        ],
                                      ),
                                    ),
                                  ],
                                ],
                              ),
                            ),
                          ),
                          SizedBox(height: screenHeight * 0.02),
                        ],
                      ),
                    );
                  },
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}
