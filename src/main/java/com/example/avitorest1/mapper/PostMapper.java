package com.example.avitorest1.mapper;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.repository.AuthorRepository;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    //    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "authorNameRequest", source = "authorNameEntity")
//    @Mapping(target = "category")
//    @Mapping(target = "description")
//    @Mapping(target = "date")
//    @Mapping(target = "price")
//    @Mapping(target = "author")
//    @Mapping(target = "author", expression = "java(mapAuthor(postRequest.getAuthorId(), authorRepository))")
    PostEntity mapPost(PostRequest postRequest);
    PostResponse toPostResponse(PostEntity postEntity);
    List<PostResponse> toPostResponseList(List<PostEntity> postEntityList);

//    default AuthorEntity mapAuthor(Long authorId, AuthorRepository authorRepository) {
//        return authorRepository.findById(authorId).orElseThrow(()->new RuntimeException("Автор не найден id "+ authorId));
//    }
}
