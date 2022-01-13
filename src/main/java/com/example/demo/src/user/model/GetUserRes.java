package com.example.demo.src.user.model;


import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRes {
    private int userId;
    private String name;
    //private String address;
    private int level;
    private int point;
    private String email;
    private String password;
    private String phoneNumber;
    private Date birthDay;


    private String birthString;

    private String gender;

    @Enumerated(EnumType.STRING)
    private Status status;

	public enum Status{
        USING, DELETED
    };

    public GetUserRes(int userId) {
        this.userId = userId;
    }

    public void afterBirthString() {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String to  = transFormat.format(this.birthDay);
        this.birthString  = to;
        System.out.println(to);
        //return this;
    }
}
