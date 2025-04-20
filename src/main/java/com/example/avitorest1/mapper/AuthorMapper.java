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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    AuthorEntity toAuthor(AuthorRequest authorRequest);
    AuthorResponse toAuthorResponse(AuthorEntity authorEntity);
    List<AuthorResponse> toListAuthorResponse(List<AuthorEntity> authorEntityList);
    AuthorEntity loginAuthor(AuthorRequest authorRequest);

}
