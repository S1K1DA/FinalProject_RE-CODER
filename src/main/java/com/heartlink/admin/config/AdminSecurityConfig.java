package com.heartlink.admin.config;

import com.heartlink.config.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(1)
public class AdminSecurityConfig{

    private final JwtFilter jwtFilter;
//    private final CorsFilter corsFilter;

    @Autowired
    public AdminSecurityConfig(JwtFilter jwtFilter, CorsFilter corsFilter){
        this.jwtFilter = jwtFilter;
//        this.corsFilter = corsFilter;
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain (HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .securityMatcher("/admin/**") // /admin/** 경로에 대해서만 이 보안 설정 적용
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/register/**", "/admin/login/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 모든 요청은 ADMIN 권한 필요
                )
                .formLogin(form -> form
                        .loginPage("/admin/login") // Admin 로그인 페이지 설정
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")  // 로그아웃 URL 설정
                        .logoutSuccessUrl("/admin/login")  // 로그아웃 성공 시 리디렉션할 URL
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("adminToken")  // adminToken 쿠키 삭제
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
