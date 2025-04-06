package com.example.avitorest1.response;


import com.example.avitorest1.enums.RoleEnum;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
//    private Long id;

    private String name;

    private String email;

    private String firstName;

    private String lastName;

    private RoleEnum role;
}