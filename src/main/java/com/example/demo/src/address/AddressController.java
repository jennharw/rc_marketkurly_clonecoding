package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.address.model.AddressRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/address")
public class AddressController {

    private final JwtService jwtService;
    private final AddressService addressService;
    private final AddressProvider addressProvider;

    //주소
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<AddressRes>> getAddress(@PathVariable("userIdx") int userIdx, @RequestParam(required = false) String addressIdx) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        if (addressIdx == null) {
            try{
                List<AddressRes> addressResList = addressProvider.getAddress(userIdxByJwt);

                return new BaseResponse<>(addressResList);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));

            }
        } else {
            //하나 선택
            List<AddressRes> addressResList = addressProvider.chooseAddress(userIdxByJwt, Integer.parseInt(addressIdx));
            return new BaseResponse<>(addressResList);

        }


    }

    //주소 등록
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<Integer> createAddress(@PathVariable("userIdx") int userIdx, @RequestBody AddressReq addressReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        try{
            int addressId = addressService.createAddress(userIdxByJwt, addressReq);
            return new BaseResponse<>(addressId);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }
    }
    //주소수정
    @ResponseBody
    @PatchMapping("/{userIdx}/content/{addressIdx}")
    public BaseResponse<Integer> modifyAddress(@PathVariable("userIdx") int userIdx, @PathVariable("addressIdx") int addressIdx, @RequestBody AddressReq addressReq) throws BaseException {

        try{
            int addressId = addressService.modifyAddress(userIdx ,addressIdx, addressReq);
            return new BaseResponse<>(addressId);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody //주소삭제
    @DeleteMapping("/kurly/{addressIdx}")
    public BaseResponse<String> deleteAddress(@PathVariable("addressIdx") int addressIdx) throws BaseException {

        try{
            int addressId = addressService.deleteAddress(addressIdx);
            return new BaseResponse<>("삭제되었습니다");
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
