package com.sparta.bulletinboard.controller;

import com.sparta.bulletinboard.dto.request.LoginRequestDto;
import com.sparta.bulletinboard.dto.response.ResponseMessageDto;
import com.sparta.bulletinboard.dto.request.SignupRequestDto;
import com.sparta.bulletinboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ResponseMessageDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);

    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<ResponseMessageDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }
}
