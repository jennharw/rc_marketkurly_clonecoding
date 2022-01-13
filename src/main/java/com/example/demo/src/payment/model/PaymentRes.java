package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRes {
    private int id;
    private int discount;
    private String cardType;
    private String description;
}
