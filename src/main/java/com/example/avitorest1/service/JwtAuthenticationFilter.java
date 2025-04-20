package com.example.avitorest1.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        logger.info("Filter 'JwtAuthenticationFilter' configured for use");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String jwt = getJwtFromRequest(httpRequest);
        String requestUri = httpRequest.getRequestURI();
        String authHeader = httpRequest.getHeader("Authorization");

        logger.debug("Authorization header: {} for request: {}", authHeader, requestUri);
        logger.info("JWT-токен: {}", jwt != null ? jwt : "отсутствует");

        if (requiresAuthentication(requestUri)) {
            if (StringUtils.hasText(jwt)) {
                try {
                    if (jwtTokenProvider.validateToken(jwt)) {
                        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("JWT-токен успешно проверен для пользователя: {} с ролями: {} для запроса: {}",
                                authentication.getName(), authentication.getAuthorities(), requestUri);
                    } else {
                        logger.warn("Недействительный JWT-токен для запроса: {}", requestUri);
                        sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                                "Недействительный JWT-токен");
                        return;
                    }
                } catch (AuthenticationException e) {
                    logger.error("Ошибка проверки JWT-токена для запроса {}: {}", requestUri, e.getMessage());
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                            "Ошибка проверки JWT-токена: " + e.getMessage());
                    return;
                }
            } else {
                logger.debug("JWT-токен отсутствует в запросе: {}", requestUri);
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        "Требуется аутентификация: JWT-токен отсутствует");
                return;
            }
        } else {
            logger.debug("Запрос к публичному эндпоинту: {}. Пропускаем проверку токена.", requestUri);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.info("Request: {}", request);
        logger.info("Bearer token: {}", bearerToken != null ? bearerToken : "null");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            logger.info("Извлечённый JWT-токен: {}", jwt);
            return jwt;
        }
        return null;
    }

    private boolean requiresAuthentication(String requestUri) {
        return requestUri.startsWith("/api/authors");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format("{\"error\":\"%s\"}", message);
        response.getWriter().write(jsonResponse);
    }
}