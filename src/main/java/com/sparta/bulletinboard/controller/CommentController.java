package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.CommentRequestDto;
import com.sparta.bulletinboard.entity.Comment;
import com.sparta.bulletinboard.exception.ResponseMessage;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/comments/{id}")
    public Comment createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, jwtUtil.getUserInfoFromToken(request));
    }

    @PutMapping("/comments/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, jwtUtil.getUserInfoFromToken(request));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        String responseMessage = commentService.deleteComment(id, jwtUtil.getUserInfoFromToken(request));
        return ResponseMessage.SuccessResponse(responseMessage);
    }


}
