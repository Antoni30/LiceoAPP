package com.liceo.notas.controllers;

import com.liceo.notas.dtos.*;
import com.liceo.notas.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        if (loginResponse.isSuccess() && loginResponse.getToken() != null) {
            Cookie cookie = new Cookie("token", loginResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 día, ajusta según expiración real
            response.addCookie(cookie);
        }
        return loginResponse;
    }

    @PostMapping("/verify-mfa")
    public MfaVerificationResponse verifyMfaCode(@RequestBody MfaVerificationRequest request) {
        return authService.verifyMfaCode(request);
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<Map<String, Object>> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationEmailRequest request) {

        try {
            boolean exito = authService.reenviarEmailVerificacion(
                    request.getNickname(),
                    request.getEmail()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", exito);
            response.put("message", exito
                    ? "Correo de verificación reenviado"
                    : "No se pudo reenviar el correo");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<?> solicitarRecuperacion(
            @RequestBody UsuarioDTO request) {

        authService.validarYEnviarCorreoRecuperacion(request.getIdUsuario(), request.getEmail());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Si los datos son correctos, recibirás un correo"
        ));
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<?> restablecerContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String nuevaContrasena = request.get("nuevaContrasena");
        String idUsuario = request.get("idUsuario");
        authService.cambiarContrasena(idUsuario,email, nuevaContrasena);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Contraseña actualizada exitosamente"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Borra cookie
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }



    @Controller  // Usa @Controller en lugar de @RestController para vistas
    @RequestMapping("/restablecer")
    public class RecuperacionController {

        @GetMapping
        public String mostrarFormularioRestablecimiento(
                @RequestParam String id,
                @RequestParam String email,
                Model model) {

            model.addAttribute("idUsuario", id);
            model.addAttribute("email", email);
            return "form-restablecimiento";  // Nombre de tu plantilla HTML/Thymeleaf
        }
    }

}