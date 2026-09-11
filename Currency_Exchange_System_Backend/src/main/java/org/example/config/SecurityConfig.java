package org.example.config;

import org.example.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        // We authenticate purely via JwtAuthenticationFilter, not via
        // Spring's UserDetailsService. Registering an empty one here just
        // stops Spring Boot from auto-generating a random "user" password
        // and printing it to the console on every startup.
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: add the real frontend URL(s) here before going to production.
    // Using "*" together with allowCredentials(true) is rejected by
    // browsers, so origins must be listed explicitly.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // register / login must be reachable without a token.
                        // NOTE: bare "/api/auth" was removed - no endpoint is
                        // actually mapped to that exact path, it matched nothing.
                        .requestMatchers(
                                "/api/auth/register/register-customer",
                                "/api/auth/login")
                        .permitAll()
                                // Role-level authorization for every other endpoint is enforced
                                // via @PreAuthorize annotations on each controller method
                                // (hasRole/hasAnyRole). We deliberately do NOT duplicate that
                                // per-endpoint role matrix here as URL matchers: keeping the rules
                                // in a single place (the @PreAuthorize annotations) avoids two
                                // definitions silently drifting apart over time. This layer only
                                // guarantees that a valid token is present.
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        // no / invalid token -> 401 with a small JSON body
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"message\": \"Missing or invalid token\", \"status\": 401}");
                        })

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}