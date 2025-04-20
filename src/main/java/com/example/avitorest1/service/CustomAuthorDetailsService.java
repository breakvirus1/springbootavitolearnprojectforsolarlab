package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomAuthorDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthorDetailsService.class);
    private final AuthorRepository authorRepository;

    public CustomAuthorDetailsService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Поиск пользователя с username: {}", username);
        AuthorEntity author = authorRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Пользователь с username {} не найден", username);
                    return new UsernameNotFoundException("Пользователь не найден: " + username);
                });

        String role = author.getRole().toString().startsWith("ROLE_") ? String.valueOf(author.getRole()) : "ROLE_" + author.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        logger.debug("Пользователь {} найден с ролью: {}", username, authority.getAuthority());

        return new User(
                author.getUsername(),
                author.getPassword(),
                Collections.singletonList(authority)
        );
    }
}