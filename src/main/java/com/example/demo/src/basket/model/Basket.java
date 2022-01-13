package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Basket {
    private int basketId;
    private int itemId;
    private int count;
    private int orderId;
}
