package com.libraryspring.filter;

import com.libraryspring.utils.TokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        System.out.println(token);
        if (token != null) {
            Map<String, String> params = TokenUtils.getParametersToken(token);
            System.out.println(params);
            if (params == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Authentication authentication = createAuthentication(params.get("id"), params.get("role"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    public Authentication createAuthentication(String userId, String role) {
        UserDetails userDetails = User.builder()
                .username(userId)
                .password("") // Пароль не використовується в аутентифікації
                .roles(role) // Вказуємо роль користувача
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
