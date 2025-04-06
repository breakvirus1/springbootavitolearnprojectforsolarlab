package com.example.avitorest1.repository;

import com.example.avitorest1.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    Optional<PostEntity> findById(Long id);
    long count();
//    Optional<PostEntity> findByName(String name);
//    Optional<PostEntity> findByAuthor(String name);
//    Optional<PostEntity> findByCategory(String categoryName);
}
