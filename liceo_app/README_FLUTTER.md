# Liceo App - Aplicación Móvil

Esta es la aplicación móvil Flutter que adapta el cliente web de LiceoAdmin.

## Características Implementadas

### ✅ Autenticación
- **Pantalla de Login**: Diseño adaptado del cliente web con colores y estilos similares
- **Verificación de Código**: Modal para verificación de doble factor
- **Gestión de Sesiones**: Persistencia con SharedPreferences
- **Roles de Usuario**: Soporte para administrador y profesor

### ✅ Navegación
- **Router Configurado**: Navegación con go_router
- **Rutas Protegidas**: Control de acceso basado en roles
- **Redirección Automática**: Basada en estado de autenticación

### ✅ Diseño Adaptado
- **Colores Consistentes**: Paleta de colores del cliente web
- **Componentes Reutilizables**: Botones, campos de texto, tarjetas
- **Responsive Design**: Adaptado para dispositivos móviles
- **Material Design 3**: UI moderna y consistente

### ✅ Arquitectura
- **Provider Pattern**: Gestión de estado con Provider
- **Separación de Capas**: Services, Models, Providers, Screens
- **Código Organizado**: Estructura de carpetas clara

## Estructura del Proyecto

```
lib/
├── constants/
│   └── app_styles.dart          # Colores y estilos del diseño
├── models/
│   ├── user_model.dart          # Modelo de usuario
│   └── academic_models.dart     # Modelos académicos (Año, Materia, Curso)
├── providers/
│   └── auth_provider.dart       # Provider de autenticación
├── router/
│   └── app_router.dart          # Configuración de rutas
├── screens/
│   ├── auth/
│   │   ├── login_screen.dart    # Pantalla de login
│   │   └── verification_modal.dart # Modal de verificación
│   └── home_screen.dart         # Pantalla principal
├── services/
│   └── auth_service.dart        # Servicio de autenticación
├── widgets/
│   ├── custom_button.dart       # Botón personalizado
│   ├── custom_text_field.dart   # Campo de texto personalizado
│   └── custom_cards.dart        # Tarjetas personalizadas
└── main.dart                    # Archivo principal
```

## Dependencias

```yaml
dependencies:
  flutter:
    sdk: flutter
  cupertino_icons: ^1.0.8
  http: ^1.2.0              # Peticiones HTTP
  provider: ^6.1.1          # Gestión de estado
  go_router: ^13.2.0        # Navegación
  shared_preferences: ^2.2.2 # Almacenamiento local
  json_annotation: ^4.8.1   # Serialización JSON
  flutter_svg: ^2.0.9       # Soporte para SVG
```

## Instalación

1. **Instalar Flutter**: Asegúrate de tener Flutter instalado y configurado
   ```bash
   flutter doctor
   ```

2. **Instalar dependencias**:
   ```bash
   flutter pub get
   ```

3. **Ejecutar la aplicación**:
   ```bash
   flutter run
   ```

## Configuración del Backend

La aplicación está configurada para conectarse a:
```
http://localhost:8080/api
```

Asegúrate de que el backend esté ejecutándose en esa dirección.

### Endpoints utilizados:
- `POST /auth/login` - Autenticación
- `POST /auth/verify` - Verificación de código
- `POST /auth/logout` - Cerrar sesión
- `GET /usuarios-roles/usuario/{id}` - Obtener roles del usuario
- `GET /anios-lectivos/activos` - Obtener años lectivos activos

## Características del Diseño

### Colores Principales
- **Primary**: `#1E3A8A` (blue-800)
- **Secondary**: `#FBBF24` (yellow-400)
- **Background**: `#E0F2FE` (sky-100)
- **Success**: `#22C55E` (green-500)
- **Error**: `#EF4444` (red-500)

### Componentes Personalizados

1. **CustomButton**: Botón con diferentes variantes (primary, secondary, outlined, etc.)
2. **CustomTextField**: Campo de texto con diseño consistente
3. **ActionCard**: Tarjeta para acciones principales
4. **InfoCard**: Tarjeta para mostrar información

### Pantallas Implementadas

1. **LoginScreen**: 
   - Formulario de autenticación
   - Manejo de errores
   - Carga con spinner
   - Diseño similar al cliente web

2. **HomeScreen**:
   - Dashboard con tarjetas de acciones
   - Información del usuario
   - Estadísticas (para admin)
   - Menú de usuario

3. **VerificationModal**:
   - Modal para verificación de código
   - Diseño consistente
   - Manejo de errores

## Próximos Pasos

### Pantallas por Implementar
- [ ] Perfil de usuario
- [ ] Dashboard administrativo
- [ ] Gestión de usuarios
- [ ] Gestión de materias
- [ ] Gestión de cursos
- [ ] Pantallas de profesor

### Mejoras Pendientes
- [ ] Implementar cache de datos
- [ ] Añadir indicadores de carga
- [ ] Implementar paginación
- [ ] Añadir filtros y búsqueda
- [ ] Implementar notificaciones push
- [ ] Añadir modo oscuro
- [ ] Optimizar para tablet

## Comandos Útiles

```bash
# Ejecutar en modo debug
flutter run

# Ejecutar en modo release
flutter run --release

# Generar APK
flutter build apk

# Limpiar proyecto
flutter clean && flutter pub get

# Ver dispositivos conectados
flutter devices

# Analizar código
flutter analyze
```

## Notas de Desarrollo

- La aplicación usa **Material Design 3** para una UI moderna
- Los colores y estilos están centralizados en `app_styles.dart`
- Se implementó el patrón **Provider** para gestión de estado
- Las rutas están protegidas basadas en roles de usuario
- Los servicios HTTP están separados en clases dedicadas
- La persistencia de sesión usa SharedPreferences

## Contribución

1. Crear una rama para la nueva funcionalidad
2. Seguir las convenciones de código existentes
3. Añadir tests si es necesario
4. Actualizar la documentación
