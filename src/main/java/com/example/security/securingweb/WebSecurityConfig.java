package com.example.security.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //Spring MVC와 통합 제공 및 Spring Security의 웹 보안 지원을 활성화
public class WebSecurityConfig {
    /*
    SecurityFilterChain으로 어떤 URL 경로를 보안해야 하는지 정의한다. "/", "/home"은 누구나 접근해야 하기에 인증을 요구하지 않도록 한다.
    성공적으로 로그인하면 이전에 요청한 페이지로 리디렉션된다. /login 페이지 또한 누구나 볼 수 있도록 한다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll() //requestMatcher로 해당 url 접근 허가 설정
                        .anyRequest().authenticated() // "/", "/home"을 제외한 나머지 요청은 모두 인증되어야 한다는 의미를 가진다.
                )
                .formLogin((form) -> form
                        .loginPage("/login") //로그인페이지 경로 설정
                        .permitAll() //누구나 접근 가능
                )
                .logout((logout) -> logout.permitAll());

        return httpSecurity.build(); //build()는 예외를 필요로 한다. 그래서 build()를 반환하는 메서드 또한 예외를 던져야 한다.
    }

    /*
    UserDetailService는 단일 사용자를 저장하기 위한 메모리 내 저장소를 설정한다.
    해당 사용자에게는 사용자 이름 user, 비밀번호 password, 역할 USER가 제공된다.
    그래서 DefaultUser로 해당 사용자에게 제공할 것들을 설정한다.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
            User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();

        return new InMemoryUserDetailsManager(user);
    }

}
