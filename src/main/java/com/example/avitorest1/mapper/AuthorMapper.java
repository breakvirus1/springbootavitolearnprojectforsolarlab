package com.example.avitorest1.mapper;
import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.request.AuthorRequest;
import com.example.avitorest1.response.AuthorResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "money", constant = "0.0")
    AuthorEntity toAuthor(AuthorRequest authorRequest);

    @Mapping(target = "id", source = "id")
    AuthorResponse toAuthorResponse(AuthorEntity authorEntity);


}
