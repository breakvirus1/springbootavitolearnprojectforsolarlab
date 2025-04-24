package com.example.avitorest1.service;

import com.example.avitorest1.request.LoginRequest;
import com.example.avitorest1.request.AuthorRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthorService authorService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginRequest loginRequest) {
        if (!StringUtils.hasText(loginRequest.getUsername()) || !StringUtils.hasText(loginRequest.getPassword())) {
            logger.error("Имя пользователя или пароль пусты");
            throw new IllegalArgumentException("Имя пользователя и пароль обязательны");
        }

        logger.info("Попытка входа для пользователя: {}", loginRequest.getUsername());
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            logger.debug("Аутентификация успешна для пользователя: {}", loginRequest.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            logger.info("JWT-токен сгенерирован для пользователя: {}", loginRequest.getUsername());
            return token;
        } catch (BadCredentialsException e) {
            logger.error("Неверное имя пользователя или пароль для пользователя: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("Неверное имя пользователя или пароль", e);
        } catch (Exception e) {
            logger.error("Ошибка аутентификации для пользователя: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("Ошибка аутентификации", e);
        }
    }


    public void register(AuthorRequest authorRequest) {
        logger.info("Регистрация нового пользователя: {}", authorRequest.getUsername());
        authorService.createAuthor(authorRequest);
        logger.info("Пользователь зарегистрирован: {}", authorRequest.getUsername());
    }

    public void logout() {
        SecurityContextHolder.clearContext();
        logger.info("Пользователь вышел из системы");
    }
}