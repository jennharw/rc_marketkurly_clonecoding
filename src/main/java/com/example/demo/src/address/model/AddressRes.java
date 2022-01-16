package com.example.demo.src.address.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRes {
    private int id;
    private int userId;
    private String address;
    private String detail_address;

    private String name;
    private String phoneNumber;
    private int isSelected;
    private int isFirst;

    private DetailAddressInfo detailAddressInfo;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailAddressInfo{
        private String receiveSpace;
        private String receiveSpaceDetail;
        private String receiveSpaceEnter;
        private String receiveSpaceEnterDetail;
        private String deliverCompletedMessage;
    }


}
