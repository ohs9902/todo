package com.sparta.todo.dto;

import com.sparta.todo.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class TodoResponseDto {
    private Long id;
    private String title;
    private String  contents;
    private String manager;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.contents = todo.getContents();
        this.manager = todo.getManager();
        this.createAt = todo.getCreatedAt();
        this.modifiedAt = todo.getModifiedAt();
    }




}
