package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.PostRequestDto;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/api/posts")
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/api/post/{id}")
    public Post getOnePost(@PathVariable Long id) {
        return postService.getOnePost(id);
    }

    @PostMapping("/api/post")
    public Post createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @PutMapping("/api/post/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public String deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.deletePost(id, requestDto);
    }
}
