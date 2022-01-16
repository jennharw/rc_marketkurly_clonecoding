package com.example.demo.src.address.model;


import lombok.*;

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
        private String deliverCompleteMessage;
    }

}
