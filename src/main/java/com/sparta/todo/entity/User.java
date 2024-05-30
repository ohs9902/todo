package com.sparta.todo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
@Getter
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nickname;

    @Column(nullable = false,unique = true)
    @Length(min = 4, max = 10)
    private String username;

    @Column(nullable = false)
    @Length(min = 8, max = 15)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

}
