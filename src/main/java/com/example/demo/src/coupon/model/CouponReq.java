package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CouponReq {
    private int id;
    private String couponName;
    private int discount;

    private int eventId;

    private LocalDate expired_date;
}
