package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.PostRequestDto;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.exception.ResponseMessage;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @GetMapping("/posts")
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getOnePost(@PathVariable Long id) {
        return postService.getOnePost(id);
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, jwtUtil.getUserInfoFromToken(request));
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, jwtUtil.getUserInfoFromToken(request));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseMessage> deletePost(@PathVariable Long id, HttpServletRequest request) {
        String responseMessage = postService.deletePost(id, jwtUtil.getUserInfoFromToken(request));
        return ResponseMessage.SuccessResponse(responseMessage);
    }
}
