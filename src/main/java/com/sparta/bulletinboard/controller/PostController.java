package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.request.PostRequestDto;
import com.sparta.bulletinboard.dto.response.ResponseDto;
import com.sparta.bulletinboard.entity.Post;
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

    @GetMapping("/posts")
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getOnePost(@PathVariable Long id) {
        return postService.getOnePost(id);
    }

    @PostMapping("/posts")
    public ResponseEntity<ResponseDto> createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {

        return postService.createPost(requestDto, request);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<ResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }
}
