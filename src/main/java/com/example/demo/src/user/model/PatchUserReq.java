package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PatchUserReq {
    private int userIdx;
    private String userName;
    private String password;
    private String newPassword;
    private String newName;
    private String newPhoneNumber;
    private String newEmail;
}
