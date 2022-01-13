package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.BasketProvider;
import com.example.demo.src.basket.BasketService;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.level.LevelService;
import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.order.model.OrderRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.OVER_POINT;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;
    private final UserProvider userProvider;
    private final LevelService levelService;
    private final UserService userService;
    private final CouponService couponService;
    private final BasketService basketService;


    public int createOrder(OrderReq order, int userIdx) throws BaseException {
        try {
            //basketProvider.getBaskets(order.getOrderId());
            int orderId = orderDao.createOrder(order, userIdx);
            return orderId;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public OrderRes completePay(int orderId) throws BaseException {
        try {
            OrderRes orderRes = orderDao.completePay(orderId);
            return orderRes;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkItem(int itemId, int userId) throws BaseException {
        try {
            //basketProvider.getBaskets(order.getOrderId());
            boolean checkOrderItem = orderDao.checkOrderItem(itemId, userId);
            return checkOrderItem;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public OrderRes realOrder(OrderReq orderReq, int userIdx) throws BaseException {
        GetUserRes user = userProvider.getUser(userIdx);
        if (orderReq.getPoints() > user.getPoint()){
            throw new BaseException(OVER_POINT);

        }

        int orderId = createOrder(orderReq, userIdx); //1. order생성, 결제 중

        //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.결제 api 결제, 결제처리 중
        OrderRes orderRes = completePay(orderId); //결제완료

        //3. 적립금 누적, 포인트 차감

        Double pointsRateUserLevel = levelService.getLevel(user.getLevel()).getPointsRate();

        //적립금 - 주문위해 사용한 포인트 차감 해서  DB 에 저장
        Double points = user.getPoint() + (orderReq.getPrice() * pointsRateUserLevel) - orderReq.getPoints();
        userService.givePoints(points, userIdx); // 적립금 적립

        //쿠폰 사용
        if ( orderReq.getCouponIdList() != null){
            for (Integer couponId : orderReq.getCouponIdList()){
                couponService.useCoupon(couponId, userIdx);
            }
        }
        //장바구니 삭제
        if (orderReq.getBasketIds() !=  null ){
            basketService.completeBasket(orderId, orderReq.getBasketIds());
        }
        return orderRes;
    }
}
