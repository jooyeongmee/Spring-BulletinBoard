package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.request.CommentRequestDto;
import com.sparta.bulletinboard.dto.response.ResponseDto;
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

    @PostMapping("/comments/{id}")
    public ResponseEntity<ResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }


}
