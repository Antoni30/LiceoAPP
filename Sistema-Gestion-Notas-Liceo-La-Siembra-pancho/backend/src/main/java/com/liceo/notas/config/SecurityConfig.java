package com.liceo.notas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de seguridad para la aplicación.
 * Esta clase define beans relacionados con la seguridad, específicamente
 * un codificador de contraseñas utilizando BCrypt.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Crea y configura un bean de tipo PasswordEncoder.
     * Este bean se utiliza para encriptar y verificar contraseñas en la aplicación,
     * utilizando el algoritmo BCryptPasswordEncoder.
     *
     * @return Una instancia de PasswordEncoder configurada con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // habilita CORS con configuración por defecto (tomará el bean corsConfigurationSource)
                .csrf(csrf -> csrf.disable())     // deshabilita CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // ajusta tus reglas de seguridad

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3001")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}