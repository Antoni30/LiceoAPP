package com.liceo.notas.security;

import com.liceo.notas.services.AuthService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(AuthService authService) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtAuthenticationFilter(authService));
        registrationBean.addUrlPatterns("/api/*"); // Rutas a filtrar, ajústalo a tus rutas
        registrationBean.setOrder(1); // orden, si tienes más filtros

        return registrationBean;
    }

}