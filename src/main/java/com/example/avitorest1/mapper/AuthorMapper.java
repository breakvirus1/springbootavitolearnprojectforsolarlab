package com.example.avitorest1.mapper;
import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.AuthorResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = false)
    @Mapping(target = "email", ignore = false)
    AuthorEntity toAuthor(AuthorRequest authorRequest);
    AuthorResponse toAuthorResponse(AuthorEntity authorEntity);
    List<AuthorResponse> toListAuthorResponse(List<AuthorEntity> authorEntityList);

}
