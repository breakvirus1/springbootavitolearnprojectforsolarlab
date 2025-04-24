package com.example.avitorest1.mapper;

import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    PostEntity mapPost(PostRequest postRequest);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.username")
    PostResponse mapPostResponse(PostEntity postEntity);
}
