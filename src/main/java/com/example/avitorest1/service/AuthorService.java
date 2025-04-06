package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.mapper.AuthorMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.request.AuthorRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

private final AuthorRepository authorRepository;
private final AuthorMapper authorMapper;
private final Logger logger = LoggerFactory.getLogger(AuthorService.class);
    @PostConstruct
    public void preInit(){
        logger.info("\nсоздаем бин автора");
    }

    public void createAuthor(AuthorRequest authorRequest){
        logger.info("Создаем нового автора");
        AuthorEntity authorEntity = authorMapper.toAuthor(authorRequest);
        authorRepository.save(authorEntity);

    }

    public AuthorResponse getAuthor(Long id){
        logger.info("\nполучаем автора из базы с id:{}", id);
        Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
        return authorEntity.map(authorMapper::toAuthorResponse).orElse(null);
    }

    public List<AuthorResponse> getAllAuthors(Integer limit){
        logger.info("\nПолучение из базы авторов");
        List<AuthorEntity> authorEntities = authorRepository.findAll()
                .stream()
                .limit(limit!=null && limit>0? limit:Integer.MAX_VALUE)
                .collect(Collectors.toList());
        return authorMapper.toListAuthorResponse(authorEntities);
    }


    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest){
        logger.info("\nредактируем запись id = {}, authorRequest = {}", id, authorRequest);
        return AuthorResponse.builder()
                .email(authorRequest.getEmail())
                .name(authorRequest.getName())
                .build();
    }

    public void deleteAuthor(Long id){
        logger.info("\nудаления автора по id = " + id);

    }

    @PreDestroy
    public void preDestroy(){
        logger.info("\nудаляем бин AuthorService");
    }
}
