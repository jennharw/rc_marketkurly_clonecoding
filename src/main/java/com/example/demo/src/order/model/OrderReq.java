package com.example.demo.src.order.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderReq {
    private int orderId;
    private int userId;
    private int paymentId;
    private int deliverAddressId;
    private List<Integer> basketIds;
    private String deliverType;
    private String status;
    private LocalDateTime estimatedTime;
    private int price;
    private LocalDateTime deliverAt;
    private int driverId;

    private List<Integer> couponIdList; // 사용한 쿠폰
    private int points; //사용한 적립금

    public InfoNouser InfoNouser;

    //비회원
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoNouser{
        private String name;
        private String email;
        private String phoneNumber;

    }
}
