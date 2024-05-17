package com.sparta.todo.service;

import com.sparta.todo.apiResponse.ApiResponse;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public ApiResponse<TodoResponseDto> createTodo(TodoRequestDto todoRequestDto){
        Todo todo = new Todo(todoRequestDto);
        Todo saveTodo = todoRepository.save(todo);
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);
        ApiResponse<TodoResponseDto> apiResponse = new ApiResponse<>(HttpStatus.OK,"할일 생성",todoResponseDto);
        return apiResponse;
    }

    public ApiResponse<TodoResponseDto> inquireTodo(Long id){

        Todo todo = findByTodo(id);
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);
        ApiResponse<TodoResponseDto> apiResponse = new ApiResponse(HttpStatus.OK,"조회 성공",todoResponseDto);
        return apiResponse;
    }
    public ApiResponse<List> getTodo(){
        List<TodoResponseDto> todoList = todoRepository.findAllByOrderByModifiedAtDesc().stream().map(TodoResponseDto::new).toList();
        ApiResponse<List> apiResponse = new ApiResponse<>(HttpStatus.OK,"조회 성공",todoList);
        return apiResponse;
    }

    @Transactional
    public ApiResponse<Long> updateTodo(TodoRequestDto todoRequestDto,Long id){
        Todo todo = findByTodo(id);
        ApiResponse<Long> apiResponse;
        if(todo == null)
            return new ApiResponse(HttpStatus.NOT_FOUND,id+"는 존재하지 않는 ID입니다.",id);
        String password = validationPassword(todoRequestDto,id);
        if(!password.equals("0")){
            todo.update(todoRequestDto);
            apiResponse = new ApiResponse<Long>(HttpStatus.OK,"수정 성공",id);
        }else{
            apiResponse = new ApiResponse(HttpStatus.FORBIDDEN,"수정 실패",id);
        }

        return apiResponse;
    }

    public ApiResponse<Long> deleteTodo(Long id,String password){
        Todo todo = findByTodo(id);
        ApiResponse<Long> apiResponse;
        if(todo == null)
            return new ApiResponse(HttpStatus.NOT_FOUND,id+"는 존재하지 않는 ID입니다.",id);
        if(todo.getPassword().equals(password)){
            todoRepository.delete(todo);
            apiResponse = new ApiResponse<Long>(HttpStatus.OK,"삭제 성공",id);
        }else{
            apiResponse = new ApiResponse<Long>(HttpStatus.FORBIDDEN,"삭제 실패",id);
        }
        return apiResponse;
    }
    public String validationPassword(TodoRequestDto todoRequestDto,Long id){
        Todo todo = findByTodo(id);
        if(todo.getPassword().equals(todoRequestDto.getPassword())){
            return todoRequestDto.getPassword();
        }
        return "0";
    }

    public Todo findByTodo(Long id){
        return todoRepository.findById(id).orElseThrow(()-> null);
    }


}
