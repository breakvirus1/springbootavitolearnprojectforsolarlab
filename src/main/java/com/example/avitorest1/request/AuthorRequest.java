package com.example.avitorest1.request;

import com.example.avitorest1.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
    private String username;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private RoleEnum role;
    private String password;
}