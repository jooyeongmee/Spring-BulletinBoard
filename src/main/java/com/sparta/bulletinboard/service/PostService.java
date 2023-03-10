package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.request.PostRequestDto;
import com.sparta.bulletinboard.dto.response.PostResponseDto;
import com.sparta.bulletinboard.dto.response.ResponseDto;
import com.sparta.bulletinboard.dto.response.ResponseMessageDto;
import com.sparta.bulletinboard.dto.response.UserResponseDto;
import com.sparta.bulletinboard.entity.Comment;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.entity.UserRoleEnum;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.repository.PostRepository;
import com.sparta.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Post getOnePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물이 없습니다.")
        );
    }

    @Transactional
    public ResponseEntity<ResponseDto> createPost(PostRequestDto requestDto, HttpServletRequest request) {
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        String username = userResponseDto.getUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }
        Post post = new Post(requestDto, username);
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));
    }

    public boolean checkUserRole(Post post, String username, UserRoleEnum role) {
        if(role == UserRoleEnum.USER) {
            if (!post.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public ResponseEntity<ResponseDto> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        if (userResponseDto.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }

        Post post = postRepository.findById(id).orElseThrow();
        if (checkUserRole(post, userResponseDto.getUsername(), userResponseDto.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        post.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));
    }

    @Transactional
    public ResponseEntity<ResponseDto> deletePost(Long id, HttpServletRequest request) {
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        if (userResponseDto.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }

        Post post = postRepository.findById(id).orElseThrow();
        if (checkUserRole(post, userResponseDto.getUsername(), userResponseDto.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        postRepository.deleteById(post.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("게시글 삭제 성공", HttpStatus.OK.value()));

    }
}

