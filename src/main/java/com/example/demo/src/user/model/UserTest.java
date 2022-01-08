package com.example.demo.src.user.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserTest {
    private int userId;
    private String name;
    private String address;
    private String level;
    private int point;
    private String email;
    private String password;

    public UserTest(String name) {
        this.name = name;
    }
}
