package com.sparta.bulletinboard.entity;

import com.sparta.bulletinboard.dto.request.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String username;

    public Comment(CommentRequestDto requestDto, String username) {
        this.comment = requestDto.getContent();
        this.username = username;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getContent();
    }
}
