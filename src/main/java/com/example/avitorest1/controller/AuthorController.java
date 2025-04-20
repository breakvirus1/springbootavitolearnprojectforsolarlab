package com.example.avitorest1.controller;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@AllArgsConstructor
@Tag(name = "авторы", description = "API авторов")
@RequestMapping("/api/authors")
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    // Создать нового автора
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AuthorEntity> createAuthor(@RequestBody AuthorRequest authorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Создание нового автора: {} пользователем: {}", authorRequest.getUsername(), authentication.getName());
        AuthorEntity createdAuthor = authorService.createAuthor(authorRequest);
        logger.info("Автор успешно создан: {}", authorRequest.getUsername());
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    // Получить всех авторов
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AuthorEntity>> getAllAuthors() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Получение списка всех авторов пользователем: {}", authentication.getName());
        List<AuthorEntity> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    // Получить автора по ID
    @GetMapping("{id}")
    public ResponseEntity<AuthorEntity> getAuthorById(@PathVariable Long id) {
        Optional<AuthorEntity> author = authorRepository.findById(id);
        return author.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Обновить автора
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AuthorEntity> updateAuthor(@PathVariable Long id, @RequestBody AuthorEntity updatedAuthor) {
        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isPresent()) {
            updatedAuthor.setId(id);
            AuthorEntity savedAuthor = authorRepository.save(updatedAuthor);
            return new ResponseEntity<>(savedAuthor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Удалить автора
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorEntity> deleteAuthor(@PathVariable Long id) {
            authorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}