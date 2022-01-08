package com.example.demo.src.user.model;


import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRes {
    private int userId;
    private String name;
    //private String address;
    //private String level;
    private int point;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDay;
    private String gender;

    @Enumerated(EnumType.STRING)
    private Status status;

	public enum Status{
        USING, DELETED
    };

    public GetUserRes(int userId) {
        this.userId = userId;
    }
}