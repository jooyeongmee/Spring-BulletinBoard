package com.sparta.bulletinboard.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
public class SignupRequestDto {

    @NotNull
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+]{8,15}$")
    private String password;

    private boolean checkAdmin;
    private String adminToken;

}
