import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../constants/app_styles.dart';
import '../../providers/auth_provider.dart';
import '../../widgets/custom_button.dart';
import '../../widgets/custom_text_field.dart';

class VerificationModal extends StatefulWidget {
  final String idUsuario;
  final Function(String userRole) onSuccess;

  const VerificationModal({
    super.key,
    required this.idUsuario,
    required this.onSuccess,
  });

  @override
  State<VerificationModal> createState() => _VerificationModalState();
}

class _VerificationModalState extends State<VerificationModal> {
  final _codeController = TextEditingController();
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    print('🔧 [VerificationModal] initState - Modal inicializado para usuario: ${widget.idUsuario}');
  }

  @override
  void dispose() {
    print('🗑️ [VerificationModal] dispose - Modal siendo destruido');
    _codeController.dispose();
    super.dispose();
  }

  Future<void> _handleVerification() async {
    print('🔍 [VerificationModal] _handleVerification - Iniciando verificación');
    
    if (_codeController.text.trim().isEmpty) {
      setState(() {
        _errorMessage = 'Ingresa el código de verificación';
      });
      return;
    }

    final authProvider = context.read<AuthProvider>();
    
    try {
      print('🔄 [VerificationModal] Enviando código: ${_codeController.text.trim()}');
      final success = await authProvider.verifyCode(
        widget.idUsuario,
        _codeController.text.trim(),
      );

      if (!mounted) return;

      if (success) {
        print('✅ [VerificationModal] Verificación exitosa');
        widget.onSuccess(authProvider.userRole ?? 'usuario');
      } else {
        print('❌ [VerificationModal] Código inválido');
        setState(() {
          _errorMessage = 'Código de verificación inválido';
        });
      }
    } catch (e) {
      print('❌ [VerificationModal] Error en verificación: $e');
      setState(() {
        _errorMessage = 'Error al verificar el código';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    print('🎨 [VerificationModal] build - Construyendo UI del modal');
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(AppStyles.radiusLg),
      ),
      child: Container(
        constraints: const BoxConstraints(maxWidth: 400),
        padding: const EdgeInsets.all(AppStyles.spacing6),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Título
            Text(
              'Verificación de Código',
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                fontWeight: FontWeight.w600,
                color: AppColors.primary,
              ),
              textAlign: TextAlign.center,
            ),
            
            const SizedBox(height: AppStyles.spacing4),
            
            // Descripción
            Text(
              'Se ha enviado un código de verificación a tu email. Ingresa el código para continuar.',
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                color: AppColors.textSecondary,
              ),
              textAlign: TextAlign.center,
            ),
            
            const SizedBox(height: AppStyles.spacing6),
            
            // Campo de código
            CustomTextField(
              controller: _codeController,
              labelText: 'Código de verificación',
              hintText: 'Ingresa el código de 6 dígitos',
              keyboardType: TextInputType.number,
              errorText: _errorMessage,
            ),
            
            const SizedBox(height: AppStyles.spacing6),
            
            // Botones
            Row(
              children: [
                Expanded(
                  child: CustomButton(
                    text: 'Cancelar',
                    variant: ButtonVariant.secondary,
                    onPressed: () => Navigator.of(context).pop(),
                  ),
                ),
                const SizedBox(width: AppStyles.spacing3),
                Expanded(
                  child: Consumer<AuthProvider>(
                    builder: (context, authProvider, child) {
                      return CustomButton(
                        text: 'Verificar',
                        onPressed: authProvider.isLoading ? null : _handleVerification,
                        isLoading: authProvider.isLoading,
                      );
                    },
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
