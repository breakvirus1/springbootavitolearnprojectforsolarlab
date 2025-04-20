package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.RoleEnum;
import com.example.avitorest1.mapper.AuthorMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.request.AuthorRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @PostConstruct
    public void preInit() {
        logger.info("Создаём бин AuthorService");
    }

    public AuthorEntity createAuthor(AuthorRequest authorRequest) {
        logger.info("Создание нового автора: {}", authorRequest.getUsername());

        if (!StringUtils.hasText(authorRequest.getUsername()) || !StringUtils.hasText(authorRequest.getPassword())) {
            logger.error("Имя пользователя или пароль пусты");
            throw new IllegalArgumentException("Имя пользователя и пароль обязательны");
        }

        if (authorRepository.findByUsername(authorRequest.getUsername()).isPresent()) {
            logger.error("Пользователь с именем {} уже существует", authorRequest.getUsername());
            throw new RuntimeException("Пользователь с именем " + authorRequest.getUsername() + " уже существует");
        }

        AuthorEntity authorEntity = authorMapper.toAuthor(authorRequest);
        authorEntity.setPassword(passwordEncoder.encode(authorRequest.getPassword()));
        authorEntity.setRole(authorRequest.getRole().toString().startsWith("ROLE_") ? authorRequest.getRole() : RoleEnum.valueOf("ROLE_" + authorRequest.getRole()));
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);
        logger.info("Автор успешно создан: {}", authorRequest.getUsername());
        return savedAuthor;
    }

    public AuthorResponse getAuthor(Long id) {
        logger.info("Получение автора из базы с id: {}", id);
        Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
        return authorEntity.map(authorMapper::toAuthorResponse).orElse(null);
    }

    public List<AuthorEntity> getAllAuthors() {
        logger.info("Получение всех авторов из базы");
        return authorRepository.findAll();
    }

    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
        logger.info("Редактирование автора с id = {}, authorRequest = {}", id, authorRequest);
        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isPresent()) {
            AuthorEntity authorEntity = existingAuthor.get();
            authorEntity.setUsername(authorRequest.getUsername());
            authorEntity.setEmail(authorRequest.getEmail());
            if (StringUtils.hasText(authorRequest.getPassword())) {
                authorEntity.setPassword(passwordEncoder.encode(authorRequest.getPassword()));
            }
            authorEntity.setRole(authorRequest.getRole().toString().startsWith("ROLE_") ? authorRequest.getRole() : RoleEnum.valueOf("ROLE_" + authorRequest.getRole()));
            authorRepository.save(authorEntity);
            logger.info("Автор с id {} успешно обновлён", id);
            return authorMapper.toAuthorResponse(authorEntity);
        }
        logger.warn("Автор с id {} не найден", id);
        return null;
    }

    public void deleteAuthor(String username) {
        logger.info("Удаление автора {}", username);
        if (authorRepository.existsByUsername(username)) {
            authorRepository.deleteByUsername(username);
            logger.info("Автор {} успешно удалён", username);
        } else {
            logger.warn("Автор {} не найден", username);
            throw new RuntimeException("Автор " + username + " не найден");
        }
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("Удаляем бин AuthorService");
    }
}