import 'package:flutter/material.dart';
import '../constants/app_styles.dart';

class CustomButton extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;
  final bool isLoading;
  final ButtonType type;
  final ButtonVariant? variant;
  final ButtonSize? size;
  final Size? customSize;
  final Widget? icon;
  final IconData? iconData; // Agregamos soporte para IconData

  const CustomButton({
    super.key,
    required this.text,
    this.onPressed,
    this.isLoading = false,
    this.type = ButtonType.primary,
    this.variant,
    this.size,
    this.customSize,
    this.icon,
    this.iconData, // Nuevo parámetro
  });

  // Constructor con IconData para mayor conveniencia
  const CustomButton.icon({
    super.key,
    required this.text,
    required this.iconData,
    this.onPressed,
    this.isLoading = false,
    this.type = ButtonType.primary,
    this.variant,
    this.size,
    this.customSize,
  }) : icon = null;

  @override
  Widget build(BuildContext context) {
    final effectiveType = variant?.asButtonType ?? type;
    final buttonHeight = _getHeight();
    
    return SizedBox(
      width: customSize?.width,
      height: buttonHeight,
      child: ElevatedButton(
        onPressed: isLoading ? null : onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: _getBackgroundColor(effectiveType),
          foregroundColor: _getForegroundColor(effectiveType),
          disabledBackgroundColor: AppColors.border,
          disabledForegroundColor: AppColors.textLight,
          elevation: effectiveType == ButtonType.primary ? AppStyles.elevationSm : 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(AppStyles.radiusMd),
            side: effectiveType == ButtonType.outlined 
                ? const BorderSide(color: AppColors.primary)
                : BorderSide.none,
          ),
        ),
        child: isLoading
            ? SizedBox(
                width: _getIconSize(),
                height: _getIconSize(),
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                  valueColor: AlwaysStoppedAnimation<Color>(
                    _getForegroundColor(effectiveType),
                  ),
                ),
              )
            : Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (icon != null || iconData != null) ...[
                    icon ?? Icon(iconData!, size: _getIconSize()),
                    const SizedBox(width: AppStyles.spacing2),
                  ],
                  Text(
                    text,
                    style: TextStyle(
                      fontSize: _getFontSize(),
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
      ),
    );
  }

  double _getHeight() {
    switch (size) {
      case ButtonSize.small:
        return 36.0;
      case ButtonSize.medium:
      case null:
        return 48.0;
      case ButtonSize.large:
        return 56.0;
    }
  }

  double _getFontSize() {
    switch (size) {
      case ButtonSize.small:
        return AppStyles.textXs;
      case ButtonSize.medium:
      case null:
        return AppStyles.textSm;
      case ButtonSize.large:
        return AppStyles.textBase;
    }
  }

  double _getIconSize() {
    switch (size) {
      case ButtonSize.small:
        return 16.0;
      case ButtonSize.medium:
      case null:
        return 20.0;
      case ButtonSize.large:
        return 24.0;
    }
  }

  Color _getBackgroundColor(ButtonType buttonType) {
    switch (buttonType) {
      case ButtonType.primary:
        return AppColors.primary;
      case ButtonType.secondary:
        return AppColors.secondary;
      case ButtonType.success:
        return AppColors.success;
      case ButtonType.error:
        return AppColors.error;
      case ButtonType.outlined:
        return Colors.transparent;
      case ButtonType.text:
        return Colors.transparent;
    }
  }

  Color _getForegroundColor(ButtonType buttonType) {
    switch (buttonType) {
      case ButtonType.primary:
      case ButtonType.success:
      case ButtonType.error:
        return Colors.white;
      case ButtonType.secondary:
        return AppColors.textPrimary;
      case ButtonType.outlined:
      case ButtonType.text:
        return AppColors.primary;
    }
  }
}

enum ButtonType {
  primary,
  secondary,
  success,
  error,
  outlined,
  text,
}

// Enum aliases para mantener compatibilidad
enum ButtonVariant {
  primary,
  secondary,
  success,
  error,
  outline,
  text,
}

enum ButtonSize {
  small,
  medium,
  large,
}

extension ButtonVariantExtension on ButtonVariant {
  ButtonType get asButtonType {
    switch (this) {
      case ButtonVariant.primary:
        return ButtonType.primary;
      case ButtonVariant.secondary:
        return ButtonType.secondary;
      case ButtonVariant.success:
        return ButtonType.success;
      case ButtonVariant.error:
        return ButtonType.error;
      case ButtonVariant.outline:
        return ButtonType.outlined;
      case ButtonVariant.text:
        return ButtonType.text;
    }
  }
}
