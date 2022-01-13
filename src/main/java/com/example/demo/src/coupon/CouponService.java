package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.coupon.model.CouponIdRes;
import com.example.demo.src.coupon.model.CouponReq;
import com.example.demo.src.coupon.model.CouponRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponDao couponDao;

    public List<CouponRes> getUserCoupon(int userIdxByJwt) throws BaseException {
        try{
            List<CouponIdRes> couponIdList = couponDao.getUserCoupon(userIdxByJwt);

            return couponIdList.stream()
                    .map(couponIdRes ->  couponDao.getCoupon(couponIdRes.getCouponId()))
                    .collect(Collectors.toList());

//            List<CouponRes> coupons = new ArrayList<>();
//            for (CouponIdRes couponIdRes : couponIdList) {
//                CouponRes couponRes = couponDao.getCoupon(couponIdRes.getCouponId());
//                coupons.add(couponRes);
//            }
//            return coupons;
        }

        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int giveCoupon(int couponId, int userId) throws BaseException {
        try{
            couponDao.giveCouponUser(userId, couponId);
            return 0;
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    public void useCoupon(int couponId, int userId) throws BaseException {
        try{
            couponDao.userCoupon(userId, couponId);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int createCoupon(CouponReq couponReq) throws BaseException {
        try{
            return couponDao.createCoupon(couponReq);

        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}

