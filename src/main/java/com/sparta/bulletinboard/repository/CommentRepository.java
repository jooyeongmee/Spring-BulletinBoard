package com.sparta.bulletinboard.repository;

import com.sparta.bulletinboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUsername(Long id, String username);



}
