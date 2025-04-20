package com.example.avitorest1.entity;

import com.example.avitorest1.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name="name", nullable = false,length = 100)
    private String username;
    @NotBlank
    @Column(name = "email",nullable = false,unique = true)
    @Email
    private String email;
    @Column(name="firstName")
    private String firstName;
    @Column(name="lastName")
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private RoleEnum role;
    @Column(name="password", nullable=false)
    private String password;
}
