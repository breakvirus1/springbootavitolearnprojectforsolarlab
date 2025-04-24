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
    @Column(nullable = false, length = 100)
    private String username;
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Double money = 0.0;
}
