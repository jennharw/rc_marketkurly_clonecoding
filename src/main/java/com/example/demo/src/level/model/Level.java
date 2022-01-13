package com.example.demo.src.level.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Level {
    private int id;
    private int name;
    private int points_rate;
    private String description;
    private int next_points;
}
