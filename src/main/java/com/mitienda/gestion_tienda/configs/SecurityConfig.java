package com.mitienda.gestion_tienda.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mitienda.gestion_tienda.services.UsuarioDetallesService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UsuarioDetallesService customUserDetailsService;

    public SecurityConfig(UsuarioDetallesService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/usuarios/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/listar").hasRole("USER")
                .requestMatchers("/api/productos/**").hasRole("ADMIN")
                .requestMatchers("/api/compras/nueva").hasRole("USER")
                .requestMatchers("/api/compras/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.realmName("MyAppRealm"));
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        
        return authenticationManagerBuilder.build();
    }
}