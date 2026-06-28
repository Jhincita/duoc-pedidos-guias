package cl.duoc.ms_guias.config;


import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${azure.ad.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${azure.ad.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,   "/api/guias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/guias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/guias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/guias/**").hasAnyRole("ADMIN", "CONSULTA")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Object adminRole = jwt.getClaim("extension_adminRole");
            if (adminRole != null && "admin".equalsIgnoreCase(adminRole.toString())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            Object consultaRole = jwt.getClaim("extension_consultaRole");
            if (consultaRole != null && "consulta".equalsIgnoreCase(consultaRole.toString())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_CONSULTA"));
            }

            return authorities;
        });
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();
        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(issuerValidator, timestampValidator));

        return decoder;
    }
}