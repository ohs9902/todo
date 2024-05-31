package com.sparta.todo.entity;

import com.sparta.todo.dto.TodoRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor

public class Todo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200 ,nullable=false)
    private String title;

    @Column(length = 200 ,nullable=false)
    private String  contents;

    @Column
    private String manager;

    @Column(nullable = false)
    private String password;

    // 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Todo(Long id, String title, String contents, String manager, String password) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.manager = manager;
        this.password = password;
    }

    public Todo(TodoRequestDto todoRequestDto) {
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
        this.manager = todoRequestDto.getManager();
        this.password = todoRequestDto.getPassword();
    }

    public void update(TodoRequestDto todoRequestDto) {
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
        this.manager = todoRequestDto.getManager();
        this.password = todoRequestDto.getPassword();
    }
}
