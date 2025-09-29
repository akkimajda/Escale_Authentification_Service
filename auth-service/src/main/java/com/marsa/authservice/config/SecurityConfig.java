package com.marsa.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        // autoriser les pré-requêtes CORS
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // Swagger / OpenAPI
        .requestMatchers(
          "/v3/api-docs/**", "/v3/api-docs.yaml",
          "/swagger-ui.html", "/swagger-ui/**",
          "/swagger-resources/**", "/webjars/**"
        ).permitAll()

        // (optionnel) actuator de base
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

        // APIs ouvertes au front pendant le dev
        .requestMatchers("/api/ports/**", "/api/terminals/**").permitAll()

        // le reste (dev) — à durcir plus tard si besoin
        .anyRequest().permitAll()
      );
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    // Autoriser Angular en 4200/4201 et aussi 61021 (dev server possible)
    cfg.setAllowedOriginPatterns(List.of(
      "http://localhost:4200", "http://127.0.0.1:4200",
      "http://localhost:4201", "http://127.0.0.1:4201",
      "http://localhost:61021", "http://127.0.0.1:61021"
    ));
    cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
    cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
    cfg.setExposedHeaders(List.of("Authorization"));
    cfg.setAllowCredentials(true);
    cfg.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}
