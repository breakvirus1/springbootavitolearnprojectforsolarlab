package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.enums.RoleEnum;
import com.example.avitorest1.mapper.AuthorMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.PostResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(AuthorService.class);
    private final PostRepository postRepository;

    @PostConstruct
    public void preInit() {
        logger.info("Создаём бин AuthorService");
    }

    private void deleteAllPostsByAuthorId(Long authorId) {
        logger.info("Удаление всех постов автора с id: {}", authorId);
        List<PostEntity> posts = postRepository.findAllByAuthorId(authorId);
        if (!posts.isEmpty()) {
            logger.info("Найдено {} постов для удаления", posts.size());
            postRepository.deleteAll(posts);
            logger.info("Все посты автора успешно удалены");
        }
    }

    private void validateAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Требуется аутентификация");
        }
        logger.info("Пользователь аутентифицирован: {}", authentication.getName());
    }

    public AuthorEntity createAuthor(AuthorRequest authorRequest) {
        logger.info("Создание нового автора: {}", authorRequest.getUsername());

        if (!StringUtils.hasText(authorRequest.getUsername()) || !StringUtils.hasText(authorRequest.getPassword())) {
            throw new IllegalArgumentException("Имя пользователя и пароль обязательны");
        }

        if (authorRepository.findByUsername(authorRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с именем " + authorRequest.getUsername() + " уже существует");
        }

        AuthorEntity authorEntity = authorMapper.toAuthor(authorRequest);
        authorEntity.setPassword(passwordEncoder.encode(authorRequest.getPassword()));

        if (authorEntity.getRole() == null) {
            authorEntity.setRole(RoleEnum.ROLE_USER);
        }
        
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);
        logger.info("Автор успешно создан: {}", authorRequest.getUsername());
        return savedAuthor;
    }

    public AuthorResponse getAuthor(Long id) {
        validateAuthentication();
        logger.info("Получение автора из базы с id: {}", id);
        Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
        return authorEntity.map(authorMapper::toAuthorResponse).orElse(null);
    }

    public List<AuthorEntity> getAllAuthors() {
        logger.info("Получение всех авторов из базы");
        return authorRepository.findAll();
    }

    @Transactional
    public void updateAuthor(Long id, AuthorRequest updateRequest) {
        validateAuthentication();
        logger.info("Редактирование автора с id = {}, authorRequest = {}", id, updateRequest);
        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);

        AuthorEntity authorEntity = existingAuthor.get();
        authorEntity.setUsername(updateRequest.getUsername());
        authorEntity.setEmail(updateRequest.getEmail());
        if (StringUtils.hasText(updateRequest.getPassword())) {
            authorEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        authorEntity.setRole(updateRequest.getRole().toString().startsWith("ROLE_") ? updateRequest.getRole() : RoleEnum.valueOf("ROLE_" + updateRequest.getRole()));
        authorRepository.save(authorEntity);
        logger.info("Автор с id {} успешно обновлён", id);
        authorMapper.toAuthorResponse(authorEntity);
    }

    public boolean checkAuthorToDelete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Находим автора в базе по ID
        AuthorEntity authorToDelete = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор с id " + id + " не найден"));

        // Админ может удалять любые аккаунты, кроме своего
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Если это админ и он не пытается удалить свой аккаунт
        if (isAdmin && !authorToDelete.getUsername().equals(currentUsername)) {
            return true;
        }

        // Если это не админ и он пытается удалить не свой аккаунт
        if (!isAdmin && !authorToDelete.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Только администратор может удалять чужие аккаунты");
        }

        // Никто не может удалить свой собственный аккаунт
        if (authorToDelete.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Нельзя удалить собственный аккаунт");
        }

        return false;
    }

    private AuthorEntity validateAndGetAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Автор с id {} не найден", id);
                    return new RuntimeException("Автор не найден");
                });
    }

    @Transactional
    public void deleteAuthor(Long id) {
        logger.info("Начало процесса удаления автора с id: {}", id);
        
        // Проверяем аутентификацию
        validateAuthentication();
        
        // Проверяем существование автора и получаем его
        AuthorEntity authorToDelete = validateAndGetAuthor(id);
        
        // Проверяем права на удаление
        if (checkAuthorToDelete(id)) {
            // Сначала удаляем все посты автора
            deleteAllPostsByAuthorId(id);
            // Затем удаляем самого автора
            authorRepository.delete(authorToDelete);
            logger.info("Автор с id {} и все его посты успешно удалены", id);
        }
    }

    @Transactional
    public AuthorEntity addMoney(Long authorId, Double amount) {
        logger.info("Добавление {} денег автору с id: {}", amount, authorId);
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    throw new RuntimeException("Автор не найден");
                });

        author.setMoney(author.getMoney() + amount);
        AuthorEntity updatedAuthor = authorRepository.save(author);
        logger.info("Успешно добавлено {} денег автору {}. Новый баланс: {}", 
                   amount, author.getUsername(), author.getMoney());
        return updatedAuthor;
    }

    @Transactional
    public AuthorEntity minusMoney(Long authorId, Double amount) {
        logger.info("Снятие {} денег у автора с id: {}", amount, authorId);
        if (amount <= 0) {
            logger.error("Сумма должна быть положительной: {}", amount);
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    throw new RuntimeException("Автор не найден");
                });

        if (author.getMoney() < amount) {
            logger.error("Недостаточно средств у автора {}. Текущий баланс: {}, требуемая сумма: {}", 
                        author.getUsername(), author.getMoney(), amount);
            throw new RuntimeException("Недостаточно средств");
        }

        author.setMoney(author.getMoney() - amount);
        AuthorEntity updatedAuthor = authorRepository.save(author);
        logger.info("Успешно снято {} денег у автора {}. Новый баланс: {}", 
                   amount, author.getUsername(), author.getMoney());
        return updatedAuthor;
    }

    @Transactional(readOnly = true)
    public Double getBalance(Long authorId) {
        logger.info("Запрос баланса автора с id: {}", authorId);
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    throw new RuntimeException("Автор не найден");
                });
        logger.info("Баланс автора {}: {}", author.getUsername(), author.getMoney());
        return author.getMoney();
    }

    @Transactional
    public void buyPost(Long buyerId, Long postId) {
        logger.info("Попытка покупки поста {} автором {}", postId, buyerId);
        AuthorEntity buyer = authorRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден"));
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        AuthorEntity seller = post.getAuthor();

        if (buyer.getId().equals(seller.getId())) {
            throw new RuntimeException("Нельзя купить свой собственный пост");
        }

        if (buyer.getMoney() < post.getPrice()) {
            throw new RuntimeException("Недостаточно денег для покупки поста");
        }

        // Выполняем транзакцию
        buyer.setMoney(buyer.getMoney() - post.getPrice());
        seller.setMoney(seller.getMoney() + post.getPrice());
        post.setAuthor(buyer);

        authorRepository.save(buyer);
        authorRepository.save(seller);
        postRepository.save(post);

        logger.info("Пост {} куплен автором {}", postId, buyerId);
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("Удаляем бин AuthorService");
    }
}