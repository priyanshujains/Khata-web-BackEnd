package com.example.KhataWeb.Security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = null;
        String username = null;



        System.out.println("=== Cookie Debug ===");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found in request");
        }

        // Extract token from Authorization header or cookie
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println(token);
        try {
            if (token != null) {
                username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.isTokenValid(token, username)) {
                        Set<String> roles = jwtUtil.extractRoles(token);

                        var authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (ExpiredJwtException ex) {
            // Token expired, delete the cookie
            Cookie expiredCookie = new Cookie("jwt", null);
            expiredCookie.setMaxAge(0); // remove it
            expiredCookie.setPath("/");
            expiredCookie.setHttpOnly(true);
            expiredCookie.setSecure(true); // remove or false for localhost
            response.addCookie(expiredCookie);
            System.out.println("JWT expired: user logged out");
        }

        chain.doFilter(request, response);
    }
}
