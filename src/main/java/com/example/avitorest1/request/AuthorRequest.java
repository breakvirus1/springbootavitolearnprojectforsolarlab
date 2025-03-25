package com.example.avitorest1.request;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
//    private Long id;

    private String name;

    private String email;
}