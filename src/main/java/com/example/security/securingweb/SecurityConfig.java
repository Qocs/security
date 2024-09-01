package com.example.security.securingweb;

import com.example.security.jwt.JwtAuthenticationFilter;
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

/*
 Spring Security 동작 설정 : Spring security는 여러 필터를 체인 형태로 연결하여 인증과 인가를 처리한다. 필터의 순서에 따라 인증 방식이 달라질 수 있다.
 JWT 필터를 보안 체인에 추가
    UsernamePasswordAuthenticationFilter의 역할 : 전통적인 폼 로그인 기반 로그인을 처리. 사용자명과 비밀번호를 이용한 인증 담당
    JWT는 상태를 저장하지 않는 무상태 인증 방식이다. 모든 필요한 정보를 토큰에 담고 있다.
    JWT 필터를 앞에 두는 이유 : JWT 필터를 앞에 두는 이유는 JWT 토큰이 요청에 포함되어 있다면, 이미 인증된 사용자로 간주할 수 있다.
                            UsernamePassswordAuthenticationFilter 이전에 JWT 인증을 처리함으로써, 이미 인증된 사용자는 추가적인 인증 과정을 거치지 않아도 된다.
    성능 효율성 : JWT 인증을 먼저 처리함으로써 불필요한 DB 조회나 세션 관리를 줄일 수 있다.
    유연성 제공 : JWT 인증과 전통적인 폼 로그인 기반 인증을 동시에 처리할 수 있다.
                JWT 토큰이 없거나 유효하지 않은 경우, 다음 필터인 UsernamePasswordAuthenticationFilter에서 폼 기반 인증을 시도할 수 있다.
 URL 별 접근 권한 설정
 */
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
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가.

        return http.build();
    }
}
