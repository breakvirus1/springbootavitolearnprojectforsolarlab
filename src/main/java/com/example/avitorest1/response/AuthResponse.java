package com.example.avitorest1.response;

import com.example.avitorest1.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private RoleEnum role;
    private String error;
    private boolean success;

    public static AuthResponse success(String token, String username, RoleEnum role) {
        return AuthResponse.builder()
                .token(token)
                .username(username)
                .role(role)
                .success(true)
                .build();
    }

    public static AuthResponse error(String errorMessage) {
        return AuthResponse.builder()
                .success(false)
                .error(errorMessage)
                .build();
    }
} 