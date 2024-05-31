package com.sparta.todo.dto;

import com.sparta.todo.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String username;
    private Long todoId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.todoId = comment.getTodo().getId();
    }
}
