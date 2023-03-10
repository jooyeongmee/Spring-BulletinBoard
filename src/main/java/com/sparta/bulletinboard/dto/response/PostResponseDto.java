package com.sparta.bulletinboard.dto.response;

import com.sparta.bulletinboard.entity.Post;
import lombok.Data;

@Data
public class PostResponseDto implements ResponseDto{
    private Post post;

    public PostResponseDto(Post post) {
        this.post = post;
    }
}
