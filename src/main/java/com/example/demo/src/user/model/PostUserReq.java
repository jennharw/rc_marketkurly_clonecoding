package com.example.demo.src.user.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserReq {
    private String id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birth;
    private String gender;
}
