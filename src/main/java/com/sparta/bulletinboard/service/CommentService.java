package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.request.CommentRequestDto;
import com.sparta.bulletinboard.dto.response.CommentResponseDto;
import com.sparta.bulletinboard.dto.response.ResponseDto;
import com.sparta.bulletinboard.dto.response.ResponseMessageDto;
import com.sparta.bulletinboard.dto.response.UserResponseDto;
import com.sparta.bulletinboard.entity.Comment;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.UserRoleEnum;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.repository.CommentRepository;
import com.sparta.bulletinboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<ResponseDto> createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시글이 없습니다.")
        );
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        String username = userResponseDto.getUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }
        Comment comment = new Comment(requestDto, username);
        post.addComment(comment);
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));
    }

    public boolean checkUserRole(Comment comment, String username, UserRoleEnum role) {
        if(role == UserRoleEnum.USER) {
            if (!comment.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    @Transactional
    public ResponseEntity<ResponseDto> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        if (userResponseDto.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }

        Comment comment = commentRepository.findById(id).orElseThrow();
        if (checkUserRole(comment, userResponseDto.getUsername(), userResponseDto.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        
        comment.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<ResponseDto> deleteComment(Long id, HttpServletRequest request) {
        UserResponseDto userResponseDto = jwtUtil.getUserInfoFromToken(request);
        if (userResponseDto.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDto.getResponse());
        }

        Comment comment = commentRepository.findById(id).orElseThrow();
        if (checkUserRole(comment, userResponseDto.getUsername(), userResponseDto.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        commentRepository.deleteById(comment.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }
}
