package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.PostRequestDto;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post getOnePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물이 없습니다.")
        );
    }

    @Transactional
    public Post createPost(PostRequestDto requestDto) {

        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), requestDto.getAuthor(),requestDto.getPassword());
        postRepository.save(post);
        return post;
    }


    public Post checkPassword(Long id, PostRequestDto requestDto) {
        String password = requestDto.getPassword();
        Post post = postRepository.findByIdAndPassword(id, password).orElseThrow(
                () -> new IllegalArgumentException("입력하신 정보에 맞는 유저가 없습니다.")
        );
        return post;
    }

    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDto) {
        Post post = checkPassword(id, requestDto);
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getAuthor());
        postRepository.save(post);
        return post;

    }

    @Transactional
    public String deletePost(Long id, PostRequestDto requestDto) {
        Post post = checkPassword(id, requestDto);
        postRepository.deleteById(id);
        return "success: true";
    }
}
