package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.address.model.AddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final AddressDao addressDao;
    private final AddressProvider addressProvider;

    public int createAddress(int userIdxByJwt, AddressReq addressReq) throws BaseException {
        try {
            // 배송지 생성 또는 변경 시 사용자의 다른 주소의 기본, 선택을 0으로 바꿔야 함.
            List<AddressRes> addressList = addressDao.getAddress(userIdxByJwt);
            addressList.stream()
                    .map(addressRes -> {
                        try {
                            return modifyAddressNotSelectedFirst(addressRes.getId());
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }




        try{


            int addressId = addressDao.createAddress(addressReq, userIdxByJwt);
            return addressId;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int modifyAddress(int userIdxByJwt,int addressIdx , AddressReq addressReq) throws BaseException {
        List<AddressRes> addressList = addressDao.getAddress(userIdxByJwt);
        if (addressReq.getIsSelected() == 1){
            addressList.stream()
                    .filter(addressRes -> addressRes.getIsSelected() == 1)
                    .map(addressRes -> {
                        try {
                            return modifyAddressNotSelected(addressRes.getId());
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
        }
        if(addressReq.getIsFirst() == 1){
            addressList.stream()
                    .filter(addressRes -> addressRes.getIsFirst() == 1)
                    .map(addressRes -> {
                        try {
                            return modifyAddressNotFirst(addressRes.getId());
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
        }


        try{
            int addressId = addressDao.modifyUserName(addressReq, addressIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int deleteAddress(int addressIdx) throws BaseException {
        try{
            int addressId = addressDao.deleteAddress(addressIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int modifyAddressNotSelectedFirst(int addressIdx) throws BaseException {
        try{
            int addressId = addressDao.modifyAddressNotSelectedFirst(addressIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int modifyAddressNotSelected(int addressIdx) throws BaseException {
        try{
            int addressId = addressDao.modifyAddressNotSelected(addressIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int modifyAddressNotFirst(int addressIdx) throws BaseException {
        try{
            int addressId = addressDao.modifyAddressNotFirst(addressIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
