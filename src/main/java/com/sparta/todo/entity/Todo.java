package com.sparta.todo.entity;

import com.sparta.todo.dto.TodoRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Todo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String  contents;

    private String manager;

    private String password;

    public Todo(TodoRequestDto todoRequestDto) {
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
        this.manager = todoRequestDto.getManager();
        this.password = todoRequestDto.getPassword();
    }
}
