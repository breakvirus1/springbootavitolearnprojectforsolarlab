package com.example.avitorest1.controller;

import com.example.avitorest1.DTO.LoginDto;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.JWTAuthResponse;
import com.example.avitorest1.service.AuthService;
import com.example.avitorest1.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private AuthService authService;

    @PostMapping({"/login"})
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }
    @PostMapping({"/register"})
    public ResponseEntity<String> register(@RequestBody AuthorRequest authorRequest) {
        authService.register(authorRequest);
        return ResponseEntity.ok("Пользователь зареген");

    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Успешный выход из системы");
    }
}
