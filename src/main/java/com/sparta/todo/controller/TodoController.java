package com.sparta.todo.controller;

import com.sparta.todo.apiResponse.ApiResponse;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }



    @PostMapping("/createTodo")
    public ApiResponse<TodoResponseDto> createTodo(@Valid @RequestBody TodoRequestDto todoRequestDto){

        return todoService.createTodo(todoRequestDto);
    }

    @GetMapping("/inquireTodo")
    public ApiResponse<TodoResponseDto> inquireTodo(Long id){
        return todoService.inquireTodo(id);
    }

    @GetMapping("/todoList")
    public ApiResponse<List> todoList(){
        return todoService.getTodo();
    }

    @PutMapping("/updateTodo/{id}")
    public ApiResponse<Long> updateTodo(@Valid @RequestBody TodoRequestDto todoRequestDto,@PathVariable Long id){
        ApiResponse<Long> apiResponse = todoService.updateTodo(todoRequestDto,id);
        return apiResponse;


    }
    @DeleteMapping("/deleteTodo/{id}")
    public ApiResponse<Long> deleteTodo(@PathVariable Long id, @Valid @RequestParam String password){
        ApiResponse<Long> apiResponse = todoService.deleteTodo(id,password);
        return apiResponse;

    }
}
