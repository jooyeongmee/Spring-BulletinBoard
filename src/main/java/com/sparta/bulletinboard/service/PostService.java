package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.PostRequestDto;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.exception.CustomException;
import com.sparta.bulletinboard.exception.ErrorCode;
import com.sparta.bulletinboard.repository.PostRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Post getOnePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }

    @Transactional
    public Post createPost(PostRequestDto requestDto, Claims claims) {
        User user = userService.getUser(claims);
        Post post = new Post(requestDto, user);
        userService.checkPostRole(post, user);
        postRepository.save(post);
        return post;
    }


    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDto, Claims claims) {
        User user = userService.getUser(claims);
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
        userService.checkPostRole(post, user);
        post.update(requestDto.getTitle(), requestDto.getContent());
        return post;
    }

    @Transactional
    public String deletePost(Long id, Claims claims) {
        User user = userService.getUser(claims);
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
        userService.checkPostRole(post, user);
        postRepository.deleteById(post.getId());
        return "게시글 삭제 성공";

    }
}

