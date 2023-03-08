package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.LoginRequestDto;
import com.sparta.bulletinboard.dto.ResponseMessageDto;
import com.sparta.bulletinboard.dto.SignupRequestDto;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Transactional
    public ResponseEntity<ResponseMessageDto> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("회원가입 성공", HttpStatus.OK.value()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseMessageDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("로그인 성공", HttpStatus.OK.value()));
    }
}

