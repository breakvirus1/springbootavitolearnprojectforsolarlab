package com.example.avitorest1.request;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.CategoryEnum;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String name;
    private CategoryEnum category;
    private String description;
    private LocalDateTime date;
    private int price;
    private AuthorEntity author;
}

