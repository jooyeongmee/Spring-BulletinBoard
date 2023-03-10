package com.sparta.bulletinboard.dto.response;

import com.sparta.bulletinboard.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto implements ResponseDto{
    String username;
    UserRoleEnum role;
    ResponseDto response;


}
