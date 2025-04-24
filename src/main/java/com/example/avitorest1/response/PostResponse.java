package com.example.avitorest1.response;

import com.example.avitorest1.enums.CategoryEnum;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PostResponse {

    private Long id;

    private String name;

    private String description;

    private Integer price;

    private CategoryEnum category;

    private LocalDateTime date;

    private Long authorId;

    private String authorName;
}

