package com.sparta.todo.apiResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Setter
@Getter
public class ApiResponse <T>{
    private final HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status,String message,T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
