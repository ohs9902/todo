package com.sparta.todo.entity;

import com.sparta.todo.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    // 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(CommentRequestDto requestDto,User user,Todo todo){
        this.comment = requestDto.getComment();
        this.user = user;
        this.todo = todo;
    }

    public void update(String comment){
        this.comment = comment;
    }



}
