package com.example.demo.src.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRes {
    private int itemId;
    private String name;
//    private String description;
//    private int price;
}
