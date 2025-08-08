package com.liceo.notas.services;

import com.liceo.notas.dtos.*;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.services.ServiceImpl.UsuarioServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio para gestionar la autenticación de usuarios del sistema.
 * Incluye funcionalidad para login estándar, autenticación MFA (Multi-Factor Authentication),
 * generación de tokens JWT y validación de credenciales.
 */
@Service
public class AuthService {
    @Value("${app.base-url}")
    public String baseUrl;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MFAService mfaService;
    private final EmailService emailService;
    /**
     * Tiempo de expiración del código MFA en minutos.
     * Configurable mediante la propiedad app.mfa.code.expiration.minutes en application.properties.
     */
    @Value("${app.mfa.code.expiration.minutes:5}")
    public int mfaCodeExpirationMinutes;

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Debe ser segura y estar configurada en las propiedades de la aplicación.
     */
    @Value("${jwt.secret}")
    public String jwtSecret;

    /**
     * Tiempo de expiración de los tokens JWT en milisegundos.
     * Por defecto se establece en 86400000 ms = 24 horas.
     */
    @Value("${jwt.expirationMs:86400000}") // 24 horas por defecto
    public
    int jwtExpirationMs;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificacionResponse> verifyEmail(
            @RequestParam String token
    ) {
        boolean exito = usuarioService.verificarEmail(token);

        EmailVerificacionResponse response = new EmailVerificacionResponse();
        response.setExito(exito);
        response.setMensaje(exito ?
                "Email verificado exitosamente" :
                "Token inválido o expirado");

        return exito ?
                ResponseEntity.ok(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /**
     * Valida la clave secreta JWT tras la inicialización del bean.
     * Lanza una excepción si la clave no es válida.
     */

    /**
     * Constructor para inyectar dependencias necesarias.
     *
     * @param usuarioRepository Repositorio para acceder a datos del usuario
     * @param passwordEncoder   Encoder para verificar contraseñas encriptadas
     * @param mfaService        Servicio para generar y validar códigos MFA
     * @param emailService      Servicio para enviar correos electrónicos con códigos MFA
     */
    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       MFAService mfaService,
                       EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mfaService = mfaService;
        this.emailService = emailService;
    }

    /**
     * Proceso completo de inicio de sesión para un usuario.
     * Verifica credenciales y aplica MFA si está habilitado.
     *
     * @param request Objeto {@link LoginRequest} con nickname y contraseña del usuario
     * @return {@link LoginResponse} con resultado del proceso de autenticación
     */
    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByIdUsuario(request.getIdUsuario());
        Usuario usuarioEmail = usuarioOpt.get();
        // Validación básica de los campos de entrada
        if (request.getIdUsuario() == null || request.getIdUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nickname es requerido");
        }
        if (request.getContrasena() == null || request.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        // 2.Verificar si el email ya está verificado
        if (!usuarioEmail.isEmailVerificado()) {
            throw new RuntimeException("verifique su correo para poder iniciar sesión");
        }
        // Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByIdUsuario(request.getIdUsuario().trim())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        // Verificar estado del usuario
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new RuntimeException("El usuario no está activo");
        }
        if (!usuario.isEmailVerificado()) {
            throw new RuntimeException("Por favor verifica tu email primero");
        }
        // Verificar contraseña (ya está implementado correctamente)
        if (!passwordEncoder.matches(request.getContrasena().trim(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        // Procesar MFA si está habilitado
        if (usuario.getMfaHabilitado() != null && usuario.getMfaHabilitado()) {
            return processMfaLogin(usuario);
        }

        // Generar token JWT
        String token = generateJwtToken(usuario);
        return new LoginResponse(true, "Autenticación exitosa", token);
    }

    /**
     * Verifica el código MFA introducido por el usuario durante el proceso de autenticación.
     *
     * @param request Objeto {@link MfaVerificationRequest} con nickname y código MFA
     * @return {@link MfaVerificationResponse} con resultado de la verificación MFA
     */
    public MfaVerificationResponse verifyMfaCode(MfaVerificationRequest request) {
        try {
            // 1. Validar campos de entrada
            if (request.getIdUsuario() == null || request.getIdUsuario().trim().isEmpty()) {
                return new MfaVerificationResponse(false, "Credenciales inválidas", null);
            }
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return new MfaVerificationResponse(false, "El código MFA es requerido", null);
            }
            // 2. Buscar usuario
            Usuario usuario = usuarioRepository.findByIdUsuario(request.getIdUsuario().trim())
                    .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
            // 3. Verificar si MFA está habilitado (antes de verificar el código)
            if (usuario.getMfaHabilitado() == null || !usuario.getMfaHabilitado()) {
                return new MfaVerificationResponse(false, "MFA no está habilitado para este usuario", null);
            }
            // 4. Validar código MFA
            if (!mfaService.isCodeValid(usuario.getMfaSecret(), request.getCode().trim(), usuario.getMfaCodeExpiration())) {
                return new MfaVerificationResponse(false, "Código de verificación inválido o expirado", null);
            }
            // En verifyMfaCode():
            String token = generateJwtToken(usuario); // Primero genera el token
            usuario.setMfaSecret(null);              // Luego limpia el código
            usuario.setMfaCodeExpiration(null);
            usuarioRepository.save(usuario);
            return new MfaVerificationResponse(true, "Verificación MFA exitosa", token);

        } catch (Exception e) {
            e.printStackTrace();
            return new MfaVerificationResponse(false, "Error al verificar el código: " + e.getMessage(), null);
        }
    }

    public boolean verifyEmailToken(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenVerificacion(token);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmailVerificado(true);
            usuario.setTokenVerificacion(null);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    /**
     * Procesa el inicio de sesión cuando MFA está habilitado.
     * Genera un código temporal, lo asigna al usuario y lo envía por correo electrónico.
     *
     * @param usuario El usuario que intenta iniciar sesión
     * @return Respuesta indicando que se ha enviado el código MFA
     */
    private LoginResponse processMfaLogin(Usuario usuario) {
        String mfaCode = mfaService.generateCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(mfaCodeExpirationMinutes);

        usuario.setMfaSecret(mfaCode);
        usuario.setMfaCodeExpiration(expirationTime);
        usuarioRepository.save(usuario);

        emailService.sendMfaCode(usuario.getEmail(), mfaCode);

        return new LoginResponse(false, "Se ha enviado un código de verificación a tu email", null);
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param usuario El usuario para quien se genera el token
     * @return Token JWT como cadena de texto
     */
    private String generateJwtToken(Usuario usuario) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .subject(usuario.getNickname())
                    .claim("userId", usuario.getIdUsuario())
                    .claim("email", usuario.getEmail())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();  // Log del error para depuración
            throw new RuntimeException("Error al generar el token JWT: " + e.getMessage());
        }
    }

    /**
     * Valida un token JWT para verificar si es auténtico y no ha expirado.
     *
     * @param token El token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Reenvía el email de verificación a un usuario no verificado
     *
     * @param email Email del usuario que solicita el reenvío
     * @return true si el email fue reenviado exitosamente, false si el usuario no existe o ya está verificado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     */


    @Transactional
    public boolean reenviarEmailVerificacion(String nickname, String email) {
        // 1. Buscar usuario por nickname Y email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNicknameAndEmail(nickname, email);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Credenciales no coinciden o usuario no existe");
        }

        Usuario usuario = usuarioOpt.get();

        // 2. Verificar si el email ya está verificado
        if (usuario.isEmailVerificado()) {
            throw new RuntimeException("El email ya está verificado");
        }

        // 3. Generar nuevo token si es necesario
        if (usuario.getTokenVerificacion() == null) {
            usuario.setTokenVerificacion(UUID.randomUUID().toString());
            usuarioRepository.save(usuario);
        }

        // 4. Reenviar email
        usuarioService.enviarEmailVerificacion(usuario);
        return true;
    }



    @Transactional
    public void validarYEnviarCorreoRecuperacion(String idUsuario, String email) {
        // Validación combinada
        Usuario usuario = usuarioRepository.findByIdUsuarioAndEmail(idUsuario, email)
                .orElseThrow(() -> new RuntimeException("Credenciales no coinciden o usuario no existe"));

        // Generar enlace seguro (sin token en BD)
        String parametrosSeguros = "?id=" + URLEncoder.encode(idUsuario, StandardCharsets.UTF_8) +
                "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

        String resetUrl = baseUrl + "/restablecer" + parametrosSeguros;

        // Enviar correo
        emailService.sendHtmlEmail(
                email,
                "Recuperación de Contraseña",
                construirEmail(usuario.getNombres(), resetUrl)
        );
    }

    private String construirEmail(String nombre, String url) {
        return String.format("""
            <html>
                <body>
                    <h2>¡Hola, %s!</h2>
                    <p>Haz clic para restablecer tu contraseña:</p>
                    <a href="%s">Restablecer ahora</a>
                    <p><small>Este enlace expira en 1 hora.</small></p>
                </body>
            </html>
            """, nombre, url);
    }



    @Transactional
    public void cambiarContrasena(String idUsuario, String email, String nuevaContrasena) {
        // 1. Busca por ID (único)

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("ID no registrado"));
        // 2. Valida email (opcional)
        if (!usuario.getEmail().equals(email)) {
            throw new IllegalArgumentException("El email no coincide con el usuario");
        }

        // 3. Cambia la contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        // No necesita save() por @Transactional
    }



    /*public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }*/
}