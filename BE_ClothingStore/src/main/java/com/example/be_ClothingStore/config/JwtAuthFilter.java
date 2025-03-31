package com.example.be_ClothingStore.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    public JwtAuthFilter(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) {
    //     String path = request.getServletPath();
    //     return path.equals("/user/signup") || path.equals("/user/login");
    // }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        boolean jwtFound = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtFound = true;
                    String token = cookie.getValue();
                    try {
                        Jwt jwt = jwtDecoder.decode(token);
                        Authentication auth = jwtAuthenticationConverter.convert(jwt);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } catch (Exception e) {
                        SecurityContextHolder.clearContext();
                    }
                }
            }
        }
        if (!jwtFound) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
