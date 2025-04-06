package com.example.avitorest1.response;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.CategoryEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PostResponse {

//    private Long id;

    private String name;

    private CategoryEnum category;

    private String description;

    private LocalDateTime date;

    private int price;

    private AuthorEntity author;
}

