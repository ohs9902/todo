package com.sparta.todo.service;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private TodoRepository todoRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TodoRepository todoRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto){
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("User Not Found"));
        Todo todo = todoRepository.findById(requestDto.getTodoId())
                .orElseThrow(()-> new IllegalArgumentException("Todo Not Found"));
        Comment comment = new Comment();
        comment.update(requestDto,user,todo);

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId,CommentRequestDto requestDto){
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()-> new IllegalArgumentException("Comment not found!!"));
        if (!comment.getUser().getUsername().equals(requestDto.getUsername())){
            throw new IllegalArgumentException("댓글 작성자 아이디와 회원님의 아이디가 다릅니다.");
        }
        comment.update(requestDto.getComment());

        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDto(updatedComment);
    }
}
