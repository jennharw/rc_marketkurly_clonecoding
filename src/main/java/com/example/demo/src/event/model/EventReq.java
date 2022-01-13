package com.example.demo.src.event.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventReq {
    private int id;
    private String description;

}