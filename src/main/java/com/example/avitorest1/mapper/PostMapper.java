package com.example.avitorest1.mapper;

import com.example.avitorest1.entity.PostEntity;
import com.example.avitorest1.request.PostRequest;
import com.example.avitorest1.response.PostResponse;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(PostEntity postEntity);
    PostEntity mapPost(PostRequest postRequest);
    List<PostResponse> toPostResponseList(List<PostEntity> postEntityList);

}
