package com.example.avitorest1.controller;

import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import com.example.avitorest1.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "посты", description = "API постов")
public class PostController {
    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    @Operation(summary = "Создать новый пост")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Пост создан успешно"),
        @ApiResponse(responseCode = "400", description = "Неверный ввод"),
        @ApiResponse(responseCode = "401", description = "Надо авторизоваться"),
        @ApiResponse(responseCode = "403", description = "Нет доступа")
    })
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        logger.info("Creating new post for user: {}", authentication.getName());
        PostResponse response = postService.createPost(postRequest, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Получить все посты")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Посты получены успешно"),
        @ApiResponse(responseCode = "401", description = "Надо авторизоваться")
    })
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        logger.info("Получаем все посты");
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пост по id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пост получен успешно"),
        @ApiResponse(responseCode = "404", description = "Пост не найден"),
        @ApiResponse(responseCode = "401", description = "Надо авторизоваться")
    })
    public ResponseEntity<PostResponse> getPostById(@PathVariable @PositiveOrZero Long id) {
        logger.info("Получение поста по ид: {}", id);
        try {
            return ResponseEntity.ok(postService.getPostById(id));
        } catch (RuntimeException e) {
            logger.error("ошибка получения поста: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пост")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пост обновлен успешно"),
        @ApiResponse(responseCode = "400", description = "Неверный ввод"),
        @ApiResponse(responseCode = "401", description = "Надо авторизоваться"),
        @ApiResponse(responseCode = "403", description = "Нет доступа - можно обновить только свои посты"),
        @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable @PositiveOrZero Long id,
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        logger.info("Обновление поста {} пользователем {}", id, authentication.getName());
        try {
            PostResponse response = postService.updatePost(id, postRequest, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            logger.warn("Доступ запрещен для пользователя {} обновление поста {}: {}", authentication.getName(), id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            logger.error("Ошибка обновления поста: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - can only delete own posts"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost(
            @PathVariable @PositiveOrZero Long id,
            Authentication authentication) {
        logger.info("Удаление поста {} юзером {}", id, authentication.getName());
        try {
            postService.deletePost(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            logger.warn("запрещено юзеру {} удалить пост {}: {}",
                authentication.getName(), id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            logger.error("какаято ошибка: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
