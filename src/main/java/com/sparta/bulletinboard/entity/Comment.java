package com.sparta.bulletinboard.entity;

import com.sparta.bulletinboard.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequestDto requestDto, User user) {
        this.comment = requestDto.getContent();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getContent();
    }
}
