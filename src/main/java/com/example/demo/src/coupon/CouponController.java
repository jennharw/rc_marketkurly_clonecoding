package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.coupon.model.CouponReq;
import com.example.demo.src.coupon.model.CouponRes;
import com.example.demo.src.level.LevelService;
import com.example.demo.src.level.model.LevelReq;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createCoupon(@RequestBody CouponReq couponReq) {

        try{
            int result = couponService.createCoupon(couponReq);
//            String result = "쿠폰 생성 되었습니다";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/coupon/{couponId}/user/{userId}")
    public BaseResponse<String> giveCoupon(@PathVariable int couponId, @PathVariable int userId) {

        try{
            int levelId = couponService.giveCoupon(couponId, userId);
            String result = "쿠폰이 발급되었습니다";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //user의 Level 확인하기 -> 적립률 (결제시) , 할인율 보여주기
    @ResponseBody
    @GetMapping("/user-coupon")
    public BaseResponse<List<CouponRes>> getUserCoupon() {

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            List<CouponRes> userCoupons = couponService.getUserCoupon(userIdxByJwt);
            return new BaseResponse<>(userCoupons);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{couponId}")
    public BaseResponse<CouponRes> getCoupons(@PathVariable int couponId) {

        try{
            CouponRes userCoupons = couponService.getCoupon(couponId);
            return new BaseResponse<>(userCoupons);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/all")
    public BaseResponse<List<CouponRes>> getCoupons() {

        try{
            List<CouponRes> userCoupons = couponService.getCoupons();
            return new BaseResponse<>(userCoupons);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
