package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.address.model.AddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final AddressDao addressDao;
    public int createAddress(int userIdxByJwt, AddressReq addressReq) throws BaseException {
        try{
            int addressId = addressDao.createAddress(addressReq, userIdxByJwt);
            return addressId;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
