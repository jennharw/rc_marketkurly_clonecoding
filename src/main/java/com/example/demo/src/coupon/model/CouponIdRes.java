package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CouponIdRes {

    private int userIdx;
    private int couponId;
    private int eventId;
    private int isUsed;

}
