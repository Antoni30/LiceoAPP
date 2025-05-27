package com.liceo.notas.security;

import com.liceo.notas.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        List<String> excludedPaths = List.of("/api/auth/login", "/api/auth/verify-mfa");

        if (excludedPaths.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Para todas las demás rutas: validar token en cookie
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token == null || !authService.validateToken(token)) {
            // Token no presente o inválido => rechazo acceso
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acceso no autorizado: token invalido o ausente");
            return;
        }

        // Token válido, continuar la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
