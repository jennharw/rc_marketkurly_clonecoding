package com.example.demo.src.level.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelReq {
    private int id;
    private String name;
    private Double pointsRate;
    private int discountRate;
    private String description;
    private int nextPoints;
}
