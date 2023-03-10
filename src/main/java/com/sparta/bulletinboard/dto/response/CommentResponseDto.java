package com.sparta.bulletinboard.dto.response;


import com.sparta.bulletinboard.entity.Comment;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CommentResponseDto implements ResponseDto{
    private Comment comment;

    public CommentResponseDto(Comment comment) {
        this.comment = comment;
    }
}