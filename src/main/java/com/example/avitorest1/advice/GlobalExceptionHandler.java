package com.example.avitorest1.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

//@ControllerAdvice
public class GlobalExceptionHandler {
////    final Logger logger =  LoggerFactory.getLogger(GlobalExceptionHandler.class);
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//        Logger logger = Logger.getLogger(String.valueOf(GlobalExceptionHandler.class));
//        logger.info("JSON request ошибка парсинга");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("неправильный Json формат");
//    }
}
