package com.sparta.todo.service;

import com.sparta.todo.apiResponse.ApiResponse;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoService todoService;

    @Test
    @DisplayName("할일을 등록하는 로직 테스트 ")
    public void testCreateTodo(){
        TodoRequestDto todoRequestDto = new TodoRequestDto();
        todoRequestDto.setTitle("Test Title");
        todoRequestDto.setContents("Test Contents");
        todoRequestDto.setManager("dhgudtjr6635@naver.com");
        todoRequestDto.setPassword("1357");

        Todo todo = new Todo(todoRequestDto);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        ApiResponse<TodoResponseDto> response = todoService.createTodo(todoRequestDto);

        assertNotNull(response.getData());
        System.out.println("Pass");
        assertEquals(HttpStatus.OK,response.getStatus());
        System.out.println("Pass");
        assertEquals(todo.getId() , response.getData().getId());
        System.out.println("Pass");
        assertEquals(todo.getTitle(),response.getData().getTitle());
        System.out.println("Pass");
        assertEquals(todo.getContents(),response.getData().getContents());
        System.out.println("Pass");
        assertEquals(todo.getManager(),response.getData().getManager());
        System.out.println("Pass");
        verify(todoRepository , times(1)).save(any(Todo.class));
    }
    @Test
    @DisplayName("조회 하는 로직 테스트 ")
    public void testInquireTodo(){
        Long id = 1L;
        Todo todo = new Todo();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        ApiResponse<TodoResponseDto> response = todoService.inquireTodo(id);

        assertNotNull(response.getData(),"Response data should not null");
        assertEquals(HttpStatus.OK,response.getStatus(),"Response data should be OK");

        verify(todoRepository,times(1)).findById(id);
    }
    @Test
    @DisplayName("전체조회 로직 테스트")
    public void testInquireTodoList(){
        Todo todo1 = new Todo();
        Todo todo2 = new Todo();
        List<Todo> todoList = Arrays.asList(todo1,todo2);
        when(todoRepository.findAllByOrderByModifiedAtDesc()).thenReturn(todoList);
        ApiResponse<List> response = todoService.getTodo();

        assertNotNull(response.getData(), "Response data should not be null");
    }

    @Test
    @DisplayName("수정 NotFound 테스트")
    public void testUpdateTodoNotFound(){
        TodoRequestDto todoRequestDto = new TodoRequestDto();

        Long id = 1L;

        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        ApiResponse<Long> response = todoService.updateTodo(todoRequestDto,id);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatus());
    }
    @Test
    @DisplayName("수정 password 불일치 테스트")
    public void testUpdateTodoForbidden(){
        TodoRequestDto todoRequestDto = new TodoRequestDto();
        todoRequestDto.setTitle("테스트3");
        todoRequestDto.setContents("테스트3");
        todoRequestDto.setManager("dhgudtjr6635@naver.com");
        todoRequestDto.setPassword("string");
        Long id =1L;
        Todo todo = new Todo(1L,"테스트3","테스트3","dhgudtjr6635@naver.com","string1");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoService.validationPassword(todoRequestDto,1L)).thenReturn("유효하지 않는 비밀번호");
        ApiResponse<Long> response = todoService.updateTodo(todoRequestDto,id);

        assertEquals(HttpStatus.FORBIDDEN,response.getStatus());
    }
}