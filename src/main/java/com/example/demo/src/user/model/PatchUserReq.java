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

    public PatchUserReq(int userIdx) {
        this.userIdx = userIdx;
    }
}
