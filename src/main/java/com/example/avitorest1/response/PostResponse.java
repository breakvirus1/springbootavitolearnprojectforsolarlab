package com.example.avitorest1.response;

import com.example.avitorest1.entity.AuthorEntity;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PostResponse {

    private Long id;

    private String name;

    private com.example.avitorest111.enums.CategoryEnum category;

    private String description;

    private Date date;

    private int price;

    private AuthorEntity author;
}

