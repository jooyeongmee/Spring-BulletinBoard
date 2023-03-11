package com.sparta.bulletinboard.repository;

import com.sparta.bulletinboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}