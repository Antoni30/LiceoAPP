# Configuración de API - Liceo App

## 🔧 Configuración Rápida de URLs

### 📍 Archivo de Configuración Principal
El archivo `lib/constants/api_config.dart` es el **único lugar** donde necesitas cambiar las URLs del servidor.

### 🌍 Entornos Disponibles

#### 1. Entorno Local (por defecto)
```dart
static const Environment environment = Environment.local;
```
- URL: `http://192.168.100.238:8080/api`
- Usado para desarrollo local

#### 2. Entorno de Desarrollo
```dart
static const Environment environment = Environment.development;
```
- URL: `http://192.168.100.238:8080/api`
- Mismo que local por ahora

#### 3. Entorno de Producción
```dart
static const Environment environment = Environment.production;
```
- URL: `https://liceo-api.dominio.com/api`
- Para cuando tengas servidor en producción

### 🔄 Cómo Cambiar de Entorno

1. **Abre el archivo**: `lib/constants/api_config.dart`
2. **Busca la línea**:
   ```dart
   static const Environment environment = Environment.local;
   ```
3. **Cambia el valor** por el entorno que necesites:
   - `Environment.local` - Para desarrollo local
   - `Environment.development` - Para servidor de desarrollo
   - `Environment.production` - Para producción

### 🏠 Cambiar IP del Servidor Local

Si necesitas cambiar la IP del servidor:

1. **Modifica las URLs** en `api_config.dart`:
   ```dart
   case Environment.local:
     return 'http://TU_NUEVA_IP:8080/api';  // Cambia TU_NUEVA_IP
   ```

2. **Actualiza el archivo de red** `android/app/src/main/res/xml/network_security_config.xml`:
   ```xml
   <domain includeSubdomains="true">TU_NUEVA_IP</domain>
   ```

### 📱 Probar la Conectividad

La app incluye un widget de prueba de conectividad:

1. **Usar el NetworkTestWidget** en cualquier pantalla
2. **Botón "Probar Conexión"** - Verifica conectividad básica
3. **Botón "Login Real"** - Prueba con credenciales reales

### 🐛 Debug y Logs

Los logs aparecen en logcat con estos tags:
- `AuthService` - Login y autenticación
- `BaseService` - Peticiones HTTP generales
- `NetworkTest` - Pruebas de conectividad

Para ver logs específicos:
```bash
adb logcat | grep "AuthService\|BaseService\|NetworkTest"
```

### ⚙️ Configuraciones Adicionales

#### Headers HTTP
Los headers se configuran automáticamente desde `ApiConfig.defaultHeaders`

#### Timeouts
```dart
static const Duration connectionTimeout = Duration(seconds: 30);
static const Duration receiveTimeout = Duration(seconds: 30);
```

#### Modo Debug
Se activa automáticamente en entornos `local` y `development`

## 📝 Ejemplo de Cambio Rápido

### Para cambiar de IP local a otra IP:

1. **Cambia en api_config.dart**:
   ```dart
   case Environment.local:
     return 'http://192.168.1.100:8080/api';  // Nueva IP
   ```

2. **Actualiza network_security_config.xml**:
   ```xml
   <domain includeSubdomains="true">192.168.1.100</domain>
   ```

3. **Recompila la app**:
   ```bash
   flutter clean
   flutter run
   ```

¡Y listo! Toda la app usará la nueva configuración automáticamente.

## 🚨 Notas Importantes

- **NO** hardcodees URLs en otros archivos
- **SIEMPRE** usa `ApiConfig` para obtener URLs
- **Recuerda** actualizar network_security_config.xml para nuevas IPs
- **Prueba** la conectividad después de cada cambio

---

### 📞 Flujo de Login Actualizado

El login ahora sigue el mismo flujo que el cliente web:

1. POST `/api/auth/login` con credenciales
2. Si requiere 2FA: maneja `success: false`
3. GET `/api/usuarios-roles/usuario/{id}` para obtener roles
4. GET `/api/roles/{idRol}` para nombre del rol
5. Validar que el rol sea 'administrador' o 'profesor'

Todos los logs se muestran con el tag `AuthService` para facilitar el debugging.
