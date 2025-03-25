package com.example.avitorest1.request;

import com.example.avitorest1.entity.AuthorEntity;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PostRequest {

    private Long id;

    private String name;

    private com.example.avitorest111.enums.CategoryEnum category;

    private String description;

    private Date date;

    private int price;

    private AuthorEntity author;
}

