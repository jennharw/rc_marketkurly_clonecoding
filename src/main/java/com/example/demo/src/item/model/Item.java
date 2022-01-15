package com.example.demo.src.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    private int itemId;
    private int restaurantId;
    private String name;
    private String description;
    private int price;
}
