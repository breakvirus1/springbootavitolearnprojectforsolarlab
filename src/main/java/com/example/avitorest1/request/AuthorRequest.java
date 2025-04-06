package com.example.avitorest1.request;

import com.example.avitorest1.enums.RoleEnum;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
//    private Long id;

    private String name;

    private String email;
    private String firstName;

    private String lastName;

    private RoleEnum role;
}