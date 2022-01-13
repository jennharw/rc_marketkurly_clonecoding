package com.example.demo.src.present.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PresentReq {

    private int orderId;
    private int userId;
    private int paymentId;

    //address 없이
    private int itemId; //basket 없이

    private String deliverType;
    private String status;
    private LocalDateTime estimatedTime;
    private int price;
    private LocalDateTime deliverAt;
    private int driverId;

    private List<Integer> couponIdList;
    private int points;

    private String message;
    private String receiverName;
    private String messenger;
    private String receiverPhoneNumber;
}
