package com.example.avitorest1.request;

import com.example.avitorest1.entity.AuthorEntity;
import com.example.avitorest1.enums.CategoryEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PostRequest {

//    private Long id;
    private String name;
    private CategoryEnum category;
    private String description;
    private LocalDateTime date;
    private int price;
    private AuthorEntity author;
}

