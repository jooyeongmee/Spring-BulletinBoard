package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.PostRequestDto;
import com.sparta.bulletinboard.dto.ResponseMessageDto;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.repository.PostRepository;
import com.sparta.bulletinboard.repository.UserRepository;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;
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
    public User checkToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 조회 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;
        } else {
            throw new IllegalArgumentException("로그인 먼저 해주세요.");
        }
    }

    @Transactional
    public Post createPost(PostRequestDto requestDto, HttpServletRequest request) {
        User user = checkToken(request);
        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), user.getUsername());
        postRepository.save(post);
        return post;
    }


    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        User user = checkToken(request);
        Post post = postRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("입력하신 정보에 맞는 유저가 없습니다.")
        );
        post.update(requestDto.getTitle(), requestDto.getContent());
        return post;
    }

    @Transactional
    public ResponseEntity<ResponseMessageDto> deletePost(Long id, HttpServletRequest request) {
        User user = checkToken(request);
        Post post = postRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("입력하신 정보에 맞는 유저가 없습니다.")
        );
        postRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("게시글 삭제 성공", HttpStatus.OK.value()));

    }
}

