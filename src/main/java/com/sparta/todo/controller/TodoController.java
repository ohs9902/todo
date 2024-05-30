package com.sparta.todo.controller;

import com.sparta.todo.apiResponse.ApiResponse;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
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
        ApiResponse<TodoResponseDto> apiResponse = new ApiResponse<>(HttpStatus.OK,"할일 생성",todoService.createTodo(todoRequestDto));
        return apiResponse;
    }

    @GetMapping("/inquireTodo")
    public ApiResponse<TodoResponseDto> inquireTodo(Long id) throws ChangeSetPersister.NotFoundException {
        ApiResponse<TodoResponseDto> apiResponse = new ApiResponse(HttpStatus.OK,"조회 성공",todoService.inquireTodo(id));
        return apiResponse;
    }

    @GetMapping("/todoList")
    public ApiResponse<List> todoList(){
        ApiResponse<List> apiResponse = new ApiResponse<>(HttpStatus.OK,"조회 성공",todoService.getTodo());
        return apiResponse;
    }

    @PutMapping("/updateTodo/{id}")
    public ApiResponse<Long> updateTodo(@Valid @RequestBody TodoRequestDto todoRequestDto,@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        ApiResponse<Long> apiResponse = todoService.updateTodo(todoRequestDto,id);
        return apiResponse;


    }
    @DeleteMapping("/deleteTodo/{id}")
    public ApiResponse<Long> deleteTodo(@PathVariable Long id, @Valid @RequestParam String password) throws ChangeSetPersister.NotFoundException {
        ApiResponse<Long> apiResponse = todoService.deleteTodo(id,password);
        return apiResponse;

    }
}
