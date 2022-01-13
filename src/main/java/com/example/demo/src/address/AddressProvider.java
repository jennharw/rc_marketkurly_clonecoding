package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.AddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class AddressProvider {
    private final AddressDao addressDao;
    public List<AddressRes> getAddress(int userIdxByJwt) throws BaseException {
        try{
            List<AddressRes> address = addressDao.getAddress(userIdxByJwt);
            return address;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<AddressRes> chooseAddress(int userIdxByJwt, int addressIdx) throws BaseException {
        try{
            List<AddressRes> address = addressDao.chooseAddress(addressIdx, userIdxByJwt);
            return address;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
