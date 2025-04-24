package com.example.avitorest1.response;

import com.example.avitorest1.enums.RoleEnum;
import lombok.Data;

@Data
public class AuthorResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private RoleEnum role;
    private Double money;
}