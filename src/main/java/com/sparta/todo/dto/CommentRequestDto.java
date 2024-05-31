package com.sparta.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank
    private String comment;
    @NotBlank
    private String username;
    @NotNull
    private Long todoId;


}
