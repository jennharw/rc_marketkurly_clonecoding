package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.coupon.model.CouponReq;
import com.example.demo.src.event.model.EventReq;
import com.example.demo.src.event.model.EventRes;
import com.example.demo.src.level.model.LevelReq;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/app/event")
@RequiredArgsConstructor
public class EventController {

    //이벤트 생성
    //이벤트 조회

    //이벤트 참여

    //coupons 발행
    private final EventService eventService;
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createEvent(@RequestBody EventReq eventReq) {

        try{
            int eventId = eventService.createEvent(eventReq);
            String result = "";
            return new BaseResponse<>(eventId);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<EventRes>> getEvent() {

        try{
            List<EventRes> events =  eventService.getEvent();
            return new BaseResponse<>(events);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //이벤트 참여-> 쿠폰 발행
    @ResponseBody
    @GetMapping("/{eventId}")
    public BaseResponse<String> getCouponUser(@PathVariable int eventId) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();

        try{
            //참가
            int i = eventService.createParticipants(eventId, userIdxByJwt);


            String result = "참가하셨고, 쿠폰 발행했습니다. ";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
