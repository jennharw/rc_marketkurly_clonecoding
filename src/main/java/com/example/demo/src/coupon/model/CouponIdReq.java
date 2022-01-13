package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CouponIdReq {

    private int userIdx;
    private int couponId;
    private int eventId;
    private int isUsed;

}
