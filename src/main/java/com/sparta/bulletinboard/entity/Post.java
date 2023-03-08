package com.sparta.bulletinboard.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Post(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
