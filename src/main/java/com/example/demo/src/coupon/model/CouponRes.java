package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CouponRes {
    private int id;
    private String couponName;
    private int discount;
    private int eventId;

    private Date expired_date;
    private String description;
}
