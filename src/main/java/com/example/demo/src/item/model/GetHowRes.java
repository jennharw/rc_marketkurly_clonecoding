package com.example.demo.src.item.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetHowRes {
    private String category;
    private String name;
    private String price;
    private String items_img_url;
    private String discount_rate;
    private String member_discount_price;
    private int present;
    private int coupon;
}
