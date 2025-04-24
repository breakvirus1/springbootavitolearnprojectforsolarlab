package com.example.avitorest1.controller;

import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.request.LoginRequest;
import com.example.avitorest1.response.AuthResponse;
import com.example.avitorest1.service.AuthService;
import com.example.avitorest1.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthorRequest authorRequest) {
        try {
            var author = authorService.createAuthor(authorRequest);
            // After successful registration, perform login
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(authorRequest.getUsername());
            loginRequest.setPassword(authorRequest.getPassword());
            String token = authService.login(loginRequest);
            return AuthResponse.success(token, author.getUsername(), author.getRole());
        } catch (Exception e) {
            return AuthResponse.error("Ошибка при регистрации: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest);
            var author = authorRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            return AuthResponse.success(token, author.getUsername(), author.getRole());
        } catch (Exception e) {
            return AuthResponse.error("Ошибка при входе: " + e.getMessage());
        }
    }
}
