package com.sparta.bulletinboard.entity;

import com.sparta.bulletinboard.dto.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @OneToMany
    @JoinColumn(name = "post_id")
    @OrderBy(value = "createdAt desc")
    private List<Comment> comments = new ArrayList<>();

    public Post(PostRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
