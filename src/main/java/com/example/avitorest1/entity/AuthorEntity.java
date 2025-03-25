package com.example.avitorest1.entity;

import jakarta.persistence.*;
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
    private String name;
    @NotNull
    @Column(name = "email",nullable = false,unique = true)
    private String email;

}
