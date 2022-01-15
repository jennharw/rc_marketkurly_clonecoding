package com.example.demo.src.user.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserReq {
    private String id;
    @NotNull
    private String username;
    private String email;
    private String name;
    @NotNull
    private String password;
    private String phoneNumber;
    private LocalDate birth;
    private String gender;

    private LocalDate createdAt;
    private String address;
    private String detailAddress;
}
