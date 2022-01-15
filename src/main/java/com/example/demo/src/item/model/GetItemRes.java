package com.example.demo.src.item.model;

import jdk.internal.joptsimple.internal.Strings;
import lombok.*;
import org.w3c.dom.Text;

import java.sql.*;   //Timestamp
import java.text.DecimalFormat;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GetItemRes {
    private int id;
    private String name;
    private int price;
    private String discount_rate;
    private int member_discount_price;
    private Timestamp created_at;
    private String items_img_url;
    private String category;
    private String sub_category;
    private int coupon;
    private int present;
}

