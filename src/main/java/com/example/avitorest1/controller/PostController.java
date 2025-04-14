package com.example.avitorest1.controller;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.mapper.PostMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import com.example.avitorest1.service.AuthorService;
import com.example.avitorest1.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Tag(name = "посты", description = "API постов")
@RequestMapping("/api/posts")
@Controller
public class PostController {

    private final AuthorRepository authorRepository;
    final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final PostRepository postRepository;
    private final List<PostEntity> postEntityList;
    private final AuthorService authorService;
    private final PostMapper postMapper;
    private final AuthorRepository aRepository;


    //новый пост
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isPostAuthor(principal, #id)")

    @PostMapping
    @Operation(summary = "Новый пост")
    @ResponseStatus(HttpStatus.CREATED)
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "всё ok"),
//            @ApiResponse(responseCode = "400", description = "неправильные данные"),
//            @ApiResponse(responseCode = "500", description = "в сервисе трабла")
//    })
    public void createPost(@Parameter(description = "Запрос на отправку уведомление")
             @RequestBody PostRequest postRequest, BindingResult bindingResult) {
        logger.info("получен JSON: {}", postRequest);
        if (bindingResult.hasErrors()) {
            logger.error("\nГдето чтото сломалось... {}",bindingResult.getAllErrors().get(0).getDefaultMessage());
        }else{
        postService.createPost(postRequest);}
    }

    // Получить все посты
    @GetMapping
    @Operation(summary = "получить все посты")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "всё ok"),
//            @ApiResponse(responseCode = "400", description = "неправильные данные"),
//            @ApiResponse(responseCode = "500", description = "в сервисе трабла")
//    })
    public List<PostResponse> getPosts(
            @Parameter(description = "Количество элементов на странице")
            @RequestParam(value = "limit", required = false) @Positive Integer limit) {

        return postService.getAllPosts(limit);
    }


    // Получить пост по ID

    @GetMapping("/{id}")
    @Operation(summary = "Получение поста по id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успех"),
//            @ApiResponse(responseCode = "400", description = "Неверно переданные данные"),
//            @ApiResponse(responseCode = "500", description = "Ошибка работы сервиса")
//    })
    public PostResponse getPostbyId(@Parameter(description = "Идентификатор")
                                    @PathVariable @PositiveOrZero Long id) {
        return Objects.requireNonNull(postService).getPost(id);
    }


    // обновить пост
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isPostAuthor(principal, #id)")

    @PutMapping("{id}")
    @Operation(summary = "обновление поста")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "всё ok"),
//            @ApiResponse(responseCode = "400", description = "неправильные данные"),
//            @ApiResponse(responseCode = "500", description = "в сервисе трабла")
//    })

    public PostResponse updatePostById(@PathVariable @PositiveOrZero Long id,
                                       @RequestBody PostRequest postRequest) {
        return Objects.requireNonNull(postService).updatePost(id, postRequest);
    }


    // Удалить пост
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isPostAuthor(principal, #id)")

    @DeleteMapping("{id}")
    @Operation(summary = "удаление поста")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "всё ok"),
//            @ApiResponse(responseCode = "400", description = "неправильные данные"),
//            @ApiResponse(responseCode = "500", description = "в сервисе трабла")
//    })
    public void deletePostById(@PathVariable @PositiveOrZero Long id) {

        Objects.requireNonNull(postService).deletePost(id);
    }


}
