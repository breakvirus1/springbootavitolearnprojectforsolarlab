package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.mapper.PostMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.repository.PostRepository;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    @PostConstruct
    public void preInit() {
        logger.info("\nPre Init post");
    }

    public void createPost(PostRequest postRequest) {

//        LocalDateTime now = LocalDateTime.now();
        if (postRequest == null) {
            throw new IllegalArgumentException("\nпост не может быть null");
        }
        Long authorId = postRequest.getAuthor().getId();
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.warn("\nнет автора с {} базе", authorId);
                    return new IllegalArgumentException("\nНет автора c Id: " + authorId);
                });

        PostEntity postEntity = postMapper.mapPost(postRequest);
        postEntity.setAuthorName(author);
        logger.info("\nсоздаем пост: \nauthorEmail = " + postEntity.getAuthorName().getEmail() +
                ", postId = " + postEntity.getId() +
                ", postName = " + postEntity.getName() +
                ", postDescription = " + postEntity.getDescription() +
                ", postDate = " + postEntity.getDate());

        postRepository.save(postEntity);


//
//        PostEntity postEntity = postMapper.mapPost(postRequest);
////        logger.info("создаем пост: \nauthorEmail=" + postEntity.getAuthor().getEmail()+
////                "postId"+ postEntity.getId()+
////                "postName"+ postEntity.getName()+
////                "postDescription"+ postEntity.getDescription()+
////                "postDate"+postEntity.getDate());
//        postRepository.save(postEntity);
    }

    public List<PostResponse> getAllPosts(Integer limit) {

        List<PostEntity> postEntities = postRepository.findAll()
                .stream()
                .limit(limit != null && limit > 0 ? limit : Integer.MAX_VALUE)
                .collect(Collectors.toList());
        logger.info("\nПолучаем все посты с лимитом {}", limit);
        logger.info(postEntities.stream()
                .map(post -> "\nPost{id=" + post.getId() + ", " +
                        "name='" + post.getName() + "', " +
                        "category='" + post.getCategory() +
                        "authorEmail='" + post.getAuthorName().getEmail() +
                        "'}")
                .collect(Collectors.joining(", ")));
        return postMapper.toPostResponseList(postEntities);
    }

    public PostResponse getPost(Long postId) {
        logger.info("\nПолучаем пост по id {}", postId);
        PostEntity postEntity = postRepository.findById(postId).orElse(null);
        logger.info("\nПост");
        return postMapper.toPostResponse(postEntity);
    }

    public long getAllPostsCount() {
        return postRepository.count();
    }

    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        logger.info("\nОбновляем пост по id {}", postId);
        System.out.println("\npostId = " + postId + ", postRequest = " + postRequest);
        PostEntity postEntity = postRepository.findById(postId).orElse(null);
        return postMapper.toPostResponse(postEntity);

    }

    public void deletePost(Long postId) {
        logger.info("\nDelete Post by id {}", postId);
        postRepository.deleteById(postId);

    }

    @PreDestroy
    public void preDestroy() {
        logger.info("Pre Destroy post");
    }


}
