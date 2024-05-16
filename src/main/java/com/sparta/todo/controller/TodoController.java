package com.sparta.todo.controller;

import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class TodoController {
    TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }



    @PostMapping("/createTodo")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto todoRequestDto){
        return todoService.createTodo(todoRequestDto);
    }

    @GetMapping("/todoList")
    public List<TodoResponseDto> todoList(){
        return todoService.getTodo();
    }
}
