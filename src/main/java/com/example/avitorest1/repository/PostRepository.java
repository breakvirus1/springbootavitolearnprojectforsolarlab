package com.example.avitorest1.repository;

import com.example.avitorest1.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    Optional<PostEntity> findById(Long id);
    long count();
    List<PostEntity> findAllByAuthorId(Long authorId);
}
