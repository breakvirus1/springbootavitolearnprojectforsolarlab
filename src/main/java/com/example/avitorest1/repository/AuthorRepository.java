package com.example.avitorest1.repository;

import com.example.avitorest1.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity,String> {

    Optional<AuthorEntity> findById(Long id);
    Optional<AuthorEntity> findByName(String firstName);
    Optional<AuthorEntity> deleteById(Long id);
}
