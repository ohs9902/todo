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

    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public ApiResponse(HttpStatus status,String message,T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message,T data){
        return new ApiResponse<>(HttpStatus.OK,message,data);
    }

    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(HttpStatus.OK,message);
    }

    public static <T> ApiResponse<T> error(HttpStatus status,String message){
        return new ApiResponse<>(status,message);
    }
}
