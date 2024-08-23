package com.heartlink.config;

import com.heartlink.member.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userJwt = getJwtFromCookies(request, "token"); // 일반 사용자 토큰
        String adminJwt = getJwtFromCookies(request, "adminToken"); // 어드민 토큰

        if (StringUtils.hasText(userJwt) && jwtUtil.validateToken(userJwt)) {
            handleAuthentication(request, userJwt, "user");
        } else if (StringUtils.hasText(adminJwt) && jwtUtil.validateToken(adminJwt)) {
            handleAuthentication(request, adminJwt, "admin");
        }

        filterChain.doFilter(request, response);
    }

    private void handleAuthentication(HttpServletRequest request, String jwt, String userType) {
        String email = jwtUtil.getEmailFromToken(jwt);

        if ("user".equals(userType)) {
            int userId = jwtUtil.getUserNumberFromToken(jwt);
            request.setAttribute("userId", userId);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, jwt, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if ("admin".equals(userType)) {
            int adminNumber = jwtUtil.getAdminNumberFromToken(jwt);
            request.setAttribute("adminNumber", adminNumber);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, jwt, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getJwtFromCookies(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
