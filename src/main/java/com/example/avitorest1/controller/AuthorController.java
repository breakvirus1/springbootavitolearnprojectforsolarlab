package com.example.avitorest1.controller;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "авторы", description = "API авторов")
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    // Создать нового автора
    @PostMapping
    @Operation(summary = "Новый автор")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAuthor(@RequestBody AuthorRequest authorRequest) {

        authorService.createAuthor(authorRequest);

    }

    // Получить всех авторов
    @GetMapping
    public ResponseEntity<List<AuthorEntity>> getAllAuthors() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorEntity> deleteAuthor(@PathVariable Long id) {
            authorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}