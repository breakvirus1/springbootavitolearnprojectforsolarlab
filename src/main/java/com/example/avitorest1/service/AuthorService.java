package com.example.avitorest1.service;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.mapper.AuthorMapper;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.response.AuthorResponse;
import com.example.avitorest1.request.AuthorRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
//    @Autowired
//    private final AuthorRepository authorRepository;
//
//    public AuthorEntity saveAuthor(AuthorEntity author) {
//        System.out.println("вносим в базу автора: " + author);
//        return authorRepository.save(author);
//    }
//
////    public List<AuthorEntity> getAllAuthors() {}
//
//    public List<AuthorEntity> getAllAuthors() {
//        System.out.println("смотрим всех авторов в базе");
//        return authorRepository.findAll();
//    }
//
//    public AuthorEntity getAuthorById(long id) {
//        System.out.println("ищем автора по id = " + id);
//        return authorRepository.findById(id).orElse(null);
//    }
//    public AuthorEntity getAuthorByName(String name) {
//        System.out.println("ищем автора по имени: " + name);
//        return authorRepository.findByName(name).orElse(null);
//    }
//
//    public String deleteAuthorById(long id) {
//        authorRepository.deleteById(id);
//        return "автор по id "+id+" удален";
//    }
//
//    public AuthorEntity updateAuthor(AuthorEntity author) {
//        System.out.println("редактирование автора " + author);
//        AuthorEntity authorEntity = authorRepository.findById(author.getId()).orElse(null);
//        authorEntity.setName(author.getName());
//        authorEntity.setEmail(author.getEmail());
//        return authorRepository.save(authorEntity);
//    }






private final AuthorRepository authorRepository;
private final AuthorMapper authorMapper;
    @PostConstruct
    public void preInit(){
        System.out.println("создаем бин автора");
    }

    public void createAuthor(AuthorRequest authorRequest){
        System.out.println("Создаем нового автора");
        AuthorEntity authorEntity = authorMapper.toAuthor(authorRequest);
        authorRepository.save(authorEntity);

    }

    public AuthorResponse getAuthor(Long id){
        System.out.println("получаем автора из базы с id:"+id);
        Optional<AuthorEntity> authorEntity = authorRepository.findById(id);
        return authorEntity.map(authorMapper::toAuthorResponse).orElse(null);
    }

    public List<AuthorResponse> getAllAuthors(Integer limit){
        System.out.println("Получение из базы " + limit+ "авторов");
        List<AuthorEntity> authorEntities = authorRepository.findAll()
                .stream()
                .limit(limit!=null && limit>0? limit:Integer.MAX_VALUE)
                .collect(Collectors.toList());
        return authorMapper.toListAuthorResponse(authorEntities);
    }


    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest){
        System.out.println("редактируем запись id = " + id + ", authorRequest = " + authorRequest);
        return AuthorResponse.builder()
                .email(authorRequest.getEmail())
                .name(authorRequest.getName())
                .build();
    }

    public void deleteAuthor(Long id){
        System.out.println("удаления автора по id = " + id);

    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("удаляем бин AuthorService");
    }
}
