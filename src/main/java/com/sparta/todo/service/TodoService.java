package com.sparta.todo.service;

import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.exception.HttpException;
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

    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto){
        Todo todo = new Todo(todoRequestDto);
        Todo saveTodo = todoRepository.save(todo);
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        return todoResponseDto;
    }

    public TodoResponseDto inquireTodo(Long id){

        Todo todo = findByTodo(id);
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);
        return todoResponseDto;
    }
    public List<TodoResponseDto> getTodo(){
        return todoRepository.findAllByOrderByModifiedAtDesc().stream().map(TodoResponseDto::new).toList();
    }

    @Transactional
    public Long updateTodo(TodoRequestDto todoRequestDto,Long id){
        Todo todo = findByTodo(id);
        String password = validationPassword(todoRequestDto,id);
        if(!password.equals("0")){
            todo.update(todoRequestDto);
        }else{
            throw new HttpException("패스워드가 틀렸습니다.", HttpStatus.FORBIDDEN);
        }

        return id;
    }

    public Long deleteTodo(Long id,String password){
        Todo todo = findByTodo(id);
        if(todo.getPassword().equals(password)){
            todoRepository.delete(todo);
        }


        return id;
    }
    public String validationPassword(TodoRequestDto todoRequestDto,Long id){
        Todo todo = findByTodo(id);
        if(todo.getPassword().equals(todoRequestDto.getPassword())){
            return todoRequestDto.getPassword();
        }
        return "0";
    }

    public Todo findByTodo(Long id){
        return todoRepository.findById(id).orElseThrow(()-> new HttpException("없는 id입니다.",HttpStatus.NOT_FOUND));
    }


}
