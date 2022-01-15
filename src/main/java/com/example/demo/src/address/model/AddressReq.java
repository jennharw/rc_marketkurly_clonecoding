package com.example.demo.src.address.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddressReq {
    private String address;
    private String detailAddress;
    private int id;
    private String name;
    private String phoneNumber;
    private int isSelected;
    private int isFirst;
}
