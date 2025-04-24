package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.enums.CategoryEnum;
import com.example.avitorest1.enums.RoleEnum;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class DataInitService {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    @Getter
    @Setter
    private LocalDateTime now = LocalDateTime.now();

    public DataInitService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final Logger logger = LoggerFactory.getLogger(DataInitService.class);

    public void initData() {
        if (postRepository.count() != 0) {
            postRepository.deleteAll();
            postRepository.flush();
        }

        List<AuthorEntity> authors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AuthorEntity author = new AuthorEntity();
            author.setUsername("author" + i);
            author.setEmail("author-" + i + "@gmail.com");
            author.setFirstName("firstName - " + i);
            author.setLastName("lastName - " + i);
            author.setPassword(passwordEncoder.encode("password" + i));
            author.setRole(i % 2 == 0 ? RoleEnum.ROLE_ADMIN : RoleEnum.ROLE_USER);
            author.setMoney(1000.0);
            authors.add(author);
            logger.info("Добавлен автор : {}", author.getUsername());
        }
        authorRepository.saveAll(authors);
        if (authors.isEmpty()) {
            logger.error("Список авторов пуст после сохранения");
            throw new IllegalStateException("Не удалось сохранить авторов");
        } else {
            logger.info("Сохранение авторов завершено. Новый размер: {}", authors.size());
        }
        List<PostEntity> posts = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            PostEntity post = new PostEntity();
            post.setName("Post name - " + i);
            post.setCategory(CategoryEnum.values()[i % CategoryEnum.values().length]);
            int authorIndex = i % authors.size();
            AuthorEntity authorEntityIndex = authors.get(authorIndex);
            Optional<AuthorEntity> authorOptional = authorRepository.findById(authorEntityIndex.getId());
            if (authorOptional.isEmpty()) {
                throw new IllegalStateException("Автор не найден");
            }
            post.setAuthor(authorOptional.get());
            post.setDate(now);
            post.setCreatedAt(now);
            post.setDescription("описание " + i * 10);
            post.setPrice(i * 1000);
            posts.add(post);
            logger.info("Добавлен пост : {} автор поста {}", post.getName(), authorEntityIndex.getEmail());
        }

        logger.info("Сохранено постов перед: {}", postRepository.count());
        postRepository.saveAll(posts);
        logger.info("Сохранено постов после: {}", postRepository.count());
    }
}
