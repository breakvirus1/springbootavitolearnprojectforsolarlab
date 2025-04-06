package com.example.avitorest1.repository;

import com.example.avitorest1.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity,String> {

    Optional<AuthorEntity> findById(Long id);
    Optional<AuthorEntity> deleteById(Long id);
    Optional<AuthorEntity> findByNameIgnoreCase(String name);
}
