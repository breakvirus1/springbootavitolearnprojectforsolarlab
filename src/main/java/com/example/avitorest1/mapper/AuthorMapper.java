package com.example.avitorest1.mapper;
import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.AuthorResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
//    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "authorEntityName", ignore = false)
//    @Mapping(target = "authorEntityEmail", ignore = false)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", source = "name")
    AuthorEntity toAuthor(AuthorRequest authorRequest);

    @Mapping(target = "name", source = "username")
    @Mapping(target = "role", constant = "USER")
    AuthorResponse toAuthorResponse(AuthorEntity authorEntity);

    List<AuthorResponse> toListAuthorResponse(List<AuthorEntity> authorEntityList);

}
