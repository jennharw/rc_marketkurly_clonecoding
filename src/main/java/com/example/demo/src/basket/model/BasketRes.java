package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BasketRes {
    private int basketId;
    private int itemId;
    private int count;
    private int userId;
    private int orderId;
}
