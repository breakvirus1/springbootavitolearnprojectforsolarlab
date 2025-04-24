package com.example.avitorest1.controller;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;

import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.response.PostResponse;
import com.example.avitorest1.service.AuthorService;
import com.example.avitorest1.service.PostService;
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



@RestController
@AllArgsConstructor
@Tag(name = "авторы", description = "API авторов")
@RequestMapping("/api/authors")
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создание автора")
    public ResponseEntity<AuthorEntity> createAuthor(@RequestBody AuthorRequest authorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Создание нового автора: {} пользователем: {}", authorRequest.getUsername(), authentication.getName());
        AuthorEntity createdAuthor = authorService.createAuthor(authorRequest);
        logger.info("Автор успешно создан: {}", authorRequest.getUsername());
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Получение списка авторов")
    public ResponseEntity<List<AuthorEntity>> getAllAuthors() {
        logger.info("Получение списка всех авторов");
        List<AuthorEntity> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получение автора по id")
    public ResponseEntity<AuthorEntity> getAuthorById(@PathVariable Long id) {
        Optional<AuthorEntity> author = authorRepository.findById(id);
        return author.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Получение всех постов автора по id")
    public ResponseEntity<List<PostResponse>> getAuthorPosts(@PathVariable Long id) {
        logger.info("Получение всех постов автора с ID: {}", id);
        if (!authorRepository.findById(id).isPresent()) {
            logger.warn("Автор с ID {} не найден", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PostResponse> posts = postService.getPostsByAuthorId(id);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновление автора по id")
    public ResponseEntity<AuthorEntity> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequest updatedAuthor) {
        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isPresent()) {
            authorService.updateAuthor(id,updatedAuthor);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление автора по id")
    public ResponseEntity<AuthorEntity> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{buyerId}/buy/{postId}")
    @Operation(summary = "Покупка поста одним автором у другого")
    public ResponseEntity<?> buyPost(@PathVariable Long buyerId, @PathVariable Long postId) {
        try {
            authorService.buyPost(buyerId, postId);
            return ResponseEntity.ok().body("Пост успешно куплен автором");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/money/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавление денег автору")
    public ResponseEntity<AuthorEntity> addMoney(
            @PathVariable Long id,
            @RequestParam Double amount) {
        try {
            AuthorEntity updatedAuthor = authorService.addMoney(id, amount);
            return ResponseEntity.ok(updatedAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/money/minusmoney")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Снятие денег у автора")
    public ResponseEntity<AuthorEntity> minusMoney(
            @PathVariable Long id,
            @RequestParam Double amount) {
        try {
            AuthorEntity updatedAuthor = authorService.minusMoney(id, amount);
            return ResponseEntity.ok(updatedAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Недостаточно средств")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/money/balance")
    @Operation(summary = "Получение баланса автора")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) {
        try {
            Double balance = authorService.getBalance(id);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}