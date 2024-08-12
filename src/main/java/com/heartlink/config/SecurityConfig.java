package com.heartlink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 위한 CorsFilter 빈 등록
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 자격 증명 허용
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Spring Security의 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .cors(cors -> cors.disable())  // CORS 설정 비활성화 (필요시 CORS 설정 활성화 가능)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**", "/image/**", "/fonts/**").permitAll() // 정적 리소스에 대한 접근 허용
                                .anyRequest().permitAll() // 모든 요청 허용
                );
        return http.build();
    }
}
