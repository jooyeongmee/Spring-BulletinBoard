package com.sparta.bulletinboard.dto;

import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private String msg;
    private int statusCode;

    public ResponseMessageDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
