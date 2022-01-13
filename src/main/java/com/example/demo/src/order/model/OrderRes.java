package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRes {
    private int orderId;
    private int userId;
    private int paymentId;
    private String deliverType;
    private String status;
    private LocalDateTime estimatedTime;
    private int price;
    private LocalDateTime deliverAt;
    private int driverId;
}
