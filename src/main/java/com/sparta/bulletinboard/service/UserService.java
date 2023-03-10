package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.request.LoginRequestDto;
import com.sparta.bulletinboard.dto.response.ResponseMessageDto;
import com.sparta.bulletinboard.dto.request.SignupRequestDto;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.entity.UserRoleEnum;
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

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    @Transactional
    public ResponseEntity<ResponseMessageDto> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isCheckAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("관리자 암호가 틀려 등록이 불가능합니다.", HttpStatus.BAD_REQUEST.value()));
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("로그인 성공", HttpStatus.OK.value()));
    }
}

