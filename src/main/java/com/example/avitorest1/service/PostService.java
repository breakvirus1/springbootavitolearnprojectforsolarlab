package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.mapper.PostMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final PostMapper postMapper;
    private final AuthorRepository authorRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    private void validateAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Требуется аутентификация");
        }
        logger.info("Пользователь аутентифицирован: {}", authentication.getName());
    }

    public PostResponse createPost(PostRequest postRequest, String username) {
        validateAuthentication();
        AuthorEntity author = authorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        PostEntity postEntity = postMapper.mapPost(postRequest);
        postEntity.setAuthor(author);
        PostEntity savedPost = postRepository.save(postEntity);
        logger.info("Пост создан автором: {}", username);
        return postMapper.mapPostResponse(savedPost);
    }

    public List<PostResponse> getAllPosts() {
        validateAuthentication();

        return postRepository.findAll().stream()
                .map(postMapper::mapPostResponse)
                .toList();
    }

    public List<PostResponse> getPostsByAuthorId(Long authorId) {
        validateAuthentication();
        return postRepository.findAllByAuthorId(authorId).stream()
                .map(postMapper::mapPostResponse)
                .toList();
    }

    public PostResponse getPostById(Long id) {
        validateAuthentication();
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        return postMapper.mapPostResponse(postEntity);
    }

    public PostResponse updatePost(Long id, PostRequest postRequest, String username) {
        validateAuthentication();
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // Проверяем, является ли пользователь автором поста или администратором
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !postEntity.getAuthor().getUsername().equals(username)) {
            logger.warn("Пользователь {} попытался обновить пост {} принадлежащий {}", 
                username, id, postEntity.getAuthor().getUsername());
            throw new AccessDeniedException("Вы можете обновить только свои посты");
        }

        postEntity.setName(postRequest.getName());
        postEntity.setDescription(postRequest.getDescription());
        postEntity.setPrice(postRequest.getPrice());
        postEntity.setCategory(postRequest.getCategory());

        PostEntity updatedPost = postRepository.save(postEntity);
        logger.info("Пост {} обновлен пользователем {}", id, username);
        return postMapper.mapPostResponse(updatedPost);
    }

    public void deletePost(Long id, String username) {
        validateAuthentication();
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));

        // Проверяем, является ли пользователь автором поста или администратором
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !postEntity.getAuthor().getUsername().equals(username)) {
            logger.warn("Пользователь {} попытался удалить пост {} принадлежащий {}", 
                username, id, postEntity.getAuthor().getUsername());
            throw new AccessDeniedException("Вы можете удалить только свои посты");
        }

        postRepository.delete(postEntity);
        logger.info("Пост {} удален пользователем {}", id, username);
    }
}
