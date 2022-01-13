package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.coupon.CouponDao;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.coupon.model.CouponRes;
import com.example.demo.src.event.model.EventReq;
import com.example.demo.src.event.model.EventRes;
import com.example.demo.src.level.model.LevelRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventDao eventDao;
    private final CouponDao couponDao;
    private final CouponService couponService;

    public int createEvent(EventReq eventReq) throws BaseException {
        try{
            int eventId =  eventDao.createEvent(eventReq);
            return eventId;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<EventRes> getEvent() throws BaseException {
        try{
            List<EventRes> eventRes = eventDao.getEvent();
            return eventRes;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int createParticipants(int eventId, int userIdxByJwt) throws BaseException {
        try{
            //event 참가
            eventDao.participate(eventId, userIdxByJwt);
            //event의 쿠폰
            List<CouponRes> coupons = couponDao.getCouponByEvent(eventId);
            //쿠폰 발급
            for (CouponRes couponRes:coupons){
                couponDao.giveCouponUser(userIdxByJwt, couponRes.getId());
            }
            return userIdxByJwt;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }


}
