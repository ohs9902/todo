package com.sparta.todo.controller;

import com.sparta.todo.apiResponse.ApiResponse;
import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/createComment")
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CommentRequestDto requestDto){
        CommentResponseDto responseDto = commentService.createComment(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
