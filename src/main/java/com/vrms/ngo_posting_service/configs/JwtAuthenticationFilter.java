package com.vrms.ngo_posting_service.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            
            log.debug("Processing request: {} with Authorization header: {}", 
                request.getRequestURI(), 
                authHeader != null ? "Present" : "Missing");

            // Check if Authorization header exists AND starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);  // Remove "Bearer " prefix
                
                log.debug("JWT token extracted from Authorization header");

                // Validate token
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.extractUserId(token);
                    String role = jwtUtil.extractRole(token);
                    String username = jwtUtil.extractUsername(token);

                    log.info("JWT VALID - UserId: {}, Role: {}, Username: {}", userId, role, username);

                    if (userId != null && role != null && username != null) {
                        // Create authentication token
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(authority)
                            );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Store in request attributes
                        request.setAttribute("userId", userId);
                        request.setAttribute("role", role);
                        request.setAttribute("username", username);

                        // SET SECURITY CONTEXT
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        log.info("✅ Security context set for user: {} with role: {}", username, role);
                    } else {
                        log.error("❌ Failed to extract claims - UserId: {}, Role: {}, Username: {}", 
                            userId, role, username);
                    }
                } else {
                    log.warn("❌ JWT token validation failed");
                }
            } else {
                log.debug("No Bearer token in Authorization header");
            }
        } catch (Exception e) {
            log.error("❌ Error in JWT filter: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
