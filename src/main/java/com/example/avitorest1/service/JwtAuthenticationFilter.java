package com.example.avitorest1.service;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        logger.info("JwtAuthenticationFilter готов к работе");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws  IOException {
        try {
            String jwt = getJwtFromRequest(request);
            String requestUri = request.getRequestURI();

            logger.debug("Обработка запроса: {}", requestUri);

            if (StringUtils.hasText(jwt)) {
                if (jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Установлена аутентификация для пользователя: {}", authentication.getName());
                } else {
                    logger.warn("Недействительный JWT токен");
                    SecurityContextHolder.clearContext();
                }
            } else {
                logger.debug("JWT токен не найден в запросе");
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            logger.error("Ошибка аутентификации: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Ошибка аутентификации: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка в фильтре JWT: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format("{\"error\":\"%s\"}", message);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/error") ||
               path.equals("/favicon.ico");
    }
}