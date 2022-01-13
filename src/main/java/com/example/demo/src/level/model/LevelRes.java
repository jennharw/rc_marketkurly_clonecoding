package com.example.demo.src.level.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelRes {
    private int id;
    private String name;
    private Double pointsRate;
    private Double discountRate;
    private String description;
    private int nextPoints;
}
