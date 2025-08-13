import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/auth_provider.dart';
import 'router/app_router.dart';
import 'constants/app_styles.dart';

void main() {
  runApp(const LiceoApp());
}

class LiceoApp extends StatelessWidget {
  const LiceoApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => AuthProvider()),
      ],
      child: Consumer<AuthProvider>(
        builder: (context, authProvider, child) {
          final router = AppRouter.createRouter();
          
          return MaterialApp.router(
            title: 'Liceo App',
            debugShowCheckedModeBanner: false,
            theme: ThemeData(
              colorScheme: ColorScheme.fromSeed(
                seedColor: AppColors.primary,
                brightness: Brightness.light,
              ),
              useMaterial3: true,
              fontFamily: 'System',
              
              // AppBar theme
              appBarTheme: const AppBarTheme(
                backgroundColor: AppColors.surface,
                foregroundColor: AppColors.textPrimary,
                elevation: 1,
                shadowColor: AppColors.shadow,
                centerTitle: false,
              ),
              
              // Card theme
              cardTheme: CardThemeData(
                elevation: AppStyles.elevationSm,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(AppStyles.radiusLg),
                ),
                color: AppColors.surface,
              ),
              
              // Input decoration theme
              inputDecorationTheme: InputDecorationTheme(
                filled: true,
                fillColor: AppColors.surface,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                  borderSide: const BorderSide(color: AppColors.border),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                  borderSide: const BorderSide(color: AppColors.border),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                  borderSide: const BorderSide(color: AppColors.secondary, width: 2),
                ),
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: AppStyles.spacing3,
                  vertical: AppStyles.spacing4,
                ),
              ),
              
              // Elevated button theme
              elevatedButtonTheme: ElevatedButtonThemeData(
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primary,
                  foregroundColor: Colors.white,
                  elevation: AppStyles.elevationSm,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                  ),
                  padding: const EdgeInsets.symmetric(
                    horizontal: AppStyles.spacing5,
                    vertical: AppStyles.spacing3,
                  ),
                ),
              ),
              
              // Text button theme
              textButtonTheme: TextButtonThemeData(
                style: TextButton.styleFrom(
                  foregroundColor: AppColors.primary,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(AppStyles.radiusMd),
                  ),
                ),
              ),
              
              // Scaffold theme
              scaffoldBackgroundColor: AppColors.background,
            ),
            routerConfig: router,
          );
        },
      ),
    );
  }
}
