package com.example.avitorest1.advice;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.annotation.PostConstruct;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @PostConstruct
    public void init() {
        logger.info("GlobalExceptionHandler инициализирован");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        logger.error("Ошибка валидации: {}", ex.getMessage());
        return new ResponseEntity<>("{\"error\":\"Ошибка валидации: " + ex.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        logger.error("Доступ запрещен: {}", ex.getMessage());
        return new ResponseEntity<>("{\"error\":\"Доступ запрещен\"}", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Неавторизованный доступ: {}", ex.getMessage());
        return new ResponseEntity<>("{\"error\":\"Неавторизованный доступ: " + ex.getMessage() + "\"}", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Произошла ошибка: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("{\"error\":\"Внутренняя ошибка сервера: " + ex.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}