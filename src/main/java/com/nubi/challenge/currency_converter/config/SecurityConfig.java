package com.nubi.challenge.currency_converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //Sin seguridad solo para probar api´s
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests().anyRequest().permitAll(); // Permitir acceso a todos los endpoints
        return http.build();
    }
    /* 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Deshabilita CSRF para simplificar las pruebas
            .authorizeRequests()
            .antMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html"
            ).permitAll() // Permitir acceso sin autenticación a Swagger
            .anyRequest().authenticated(); // Requerir autenticación para otros endpoints
        return http.build();
    }
    */
}