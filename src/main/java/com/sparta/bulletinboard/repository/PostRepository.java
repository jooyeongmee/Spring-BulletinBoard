package com.sparta.bulletinboard.repository;

import com.sparta.bulletinboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findByIdAndPassword(Long id, String password);

}
