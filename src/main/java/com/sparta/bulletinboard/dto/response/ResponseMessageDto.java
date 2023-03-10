package com.sparta.bulletinboard.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMessageDto implements ResponseDto{
    private String msg;
    private int statusCode;

    public ResponseMessageDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
