package com.baeldung.resource.spring;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/user/info", "/api/foos/**")
            .hasAuthority("SCOPE_read")
            .antMatchers(HttpMethod.POST, "/api/foos")
            .hasAuthority("SCOPE_write")
            .anyRequest()
            .authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt();
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
                properties.getJwt().getJwkSetUri()).build();

        jwtDecoder.setClaimSetConverter(new CustomClaimAdapter());
        return jwtDecoder;
    }
}