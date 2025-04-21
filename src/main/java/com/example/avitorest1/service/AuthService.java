package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.RoleEnum;
import com.example.avitorest1.exception.AuthException;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.LoginRequest;
import com.example.avitorest1.request.RegisterRequest;
import com.example.avitorest1.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (authorRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username is already taken");
        }
        if (authorRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email is already registered");
        }

        AuthorEntity author = new AuthorEntity();
        author.setUsername(request.getUsername());
        author.setPassword(passwordEncoder.encode(request.getPassword()));
        author.setEmail(request.getEmail());
        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        author.setRole(RoleEnum.USER);

        authorRepository.save(author);

        return new AuthResponse("dummy-token", author.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return new AuthResponse("dummy-token", request.getUsername());
        }
        
        throw new AuthException("Invalid username/password combination");
    }
}