package com.example.security.securingweb;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //REST API 환경에서는 csrf를 사용하지 않는다. 브라우저의 쿠키에 정보를 저장하지 않기 때문.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth").permitAll() ///api/v1/auth/경로에 대한 url은 인증 없이 접근 가능.
                        .anyRequest().authenticated()) // 그 외의 모든 url은 인증 필요.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 관리 정책 구성. 세션을 생성하지 않고 무상태 설정.
                .authenticationProvider(authenticationProvider) //커스텀 authenticationProvider를 사용하겠다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthFilter), UsernamePasswordAuthenticationFilter.class); //JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가.

        return http.build();
    }
}
