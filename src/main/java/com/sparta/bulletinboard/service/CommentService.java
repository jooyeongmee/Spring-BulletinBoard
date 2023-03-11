package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.CommentRequestDto;
import com.sparta.bulletinboard.entity.Comment;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.exception.CustomException;
import com.sparta.bulletinboard.exception.ErrorCode;
import com.sparta.bulletinboard.repository.CommentRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Comment createComment(Long id, CommentRequestDto requestDto, Claims claims) {
        Post post = postService.getOnePost(id);
        User user = userService.getUser(claims);

        Comment comment = new Comment(requestDto, user);
        post.addComment(comment);
        commentRepository.save(comment);

        return comment;
    }


    @Transactional
    public Comment updateComment(Long id, CommentRequestDto requestDto, Claims claims) {
        User user = userService.getUser(claims);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        userService.checkCommentRole(comment, user);
        comment.update(requestDto);

        return comment;
    }

    @Transactional
    public String deleteComment(Long id, Claims claims) {
        User user = userService.getUser(claims);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        userService.checkCommentRole(comment, user);
        commentRepository.deleteById(comment.getId());

        return "댓글 삭제 성공";
    }
}
