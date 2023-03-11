package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.LoginRequestDto;
import com.sparta.bulletinboard.dto.SignupRequestDto;
import com.sparta.bulletinboard.entity.User;
import com.sparta.bulletinboard.exception.ResponseMessage;
import com.sparta.bulletinboard.jwt.JwtUtil;
import com.sparta.bulletinboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseMessage.SuccessResponse("회원가입 성공");

    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        User user = userService.login(loginRequestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getId(), user.getRole()));
        return ResponseMessage.SuccessResponse("로그인 성공");
    }
}
