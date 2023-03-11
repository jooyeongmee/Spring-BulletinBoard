package com.sparta.bulletinboard.service;

import com.sparta.bulletinboard.dto.LoginRequestDto;
import com.sparta.bulletinboard.dto.SignupRequestDto;
import com.sparta.bulletinboard.entity.Comment;
import com.sparta.bulletinboard.entity.Post;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.entity.UserRoleEnum;
import com.sparta.bulletinboard.exception.CustomException;
import com.sparta.bulletinboard.exception.ErrorCode;
import com.sparta.bulletinboard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isCheckAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(ErrorCode.WRONG_ADMIN_TOKEN);
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
        userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public User login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new CustomException(ErrorCode.NOT_PROPER_PASSWORD);
        }
        return user;
    }

    public User getUser(Claims claims) {
        User user = userRepository.findById(Long.parseLong(claims.getSubject())).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return user;
    }

    public void checkCommentRole(Comment comment, User user) {
        if(user.getRole() == UserRoleEnum.USER) {
            if (!(comment.getUser().getId() == user.getId())) {
                throw new CustomException(ErrorCode.NOT_AUTHOR);
            }
        }
    }

    public void checkPostRole(Post post, User user) {
        if(user.getRole() == UserRoleEnum.USER) {
            if (!(post.getUser().getId() == user.getId())) {
                throw new CustomException(ErrorCode.NOT_AUTHOR);
            }
        }
    }
}

