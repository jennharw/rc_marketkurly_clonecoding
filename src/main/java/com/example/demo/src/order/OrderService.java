package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.BasketProvider;
import com.example.demo.src.basket.BasketService;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.level.LevelService;
import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.order.model.OrderRes;
import com.example.demo.src.user.PointService;
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

    private final PointService pointService;

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

        int orderId = createOrder(orderReq, userIdx); //1. order??????, ?????? ???

        //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.?????? api ??????, ???????????? ???
        OrderRes orderRes = completePay(orderId); //????????????

        //3. ????????? ??????, ????????? ??????

        Double pointsRateUserLevel = levelService.getLevel(user.getLevel()).getPointsRate();

        //????????? ??????
        int points = (int) Math.ceil(orderReq.getPrice() * pointsRateUserLevel);

        //?????????id, ?????????, ??????id
        pointService.accumulatePoints(userIdx, points, orderId);

        //????????? - ???????????? ????????? ????????? ?????? ??????  DB ??? ??????
        int points_acc = user.getPoint() + points - orderReq.getPoints();

        userService.givePoints(points_acc, userIdx); // ????????? ??????

        if (orderReq.getPoints() != 0){ //???????????????
            pointService.usePoints(userIdx, orderReq.getPoints(), orderId);
        }

        //?????????id, ?????????, ??????id

        //?????? ??????
        if ( orderReq.getCouponIdList() != null){
            for (Integer couponId : orderReq.getCouponIdList()){
                couponService.useCoupon(couponId, userIdx);
            }
        }
        //???????????? ??????
        if (orderReq.getBasketIds() !=  null ){
            basketService.completeBasket(orderId, orderReq.getBasketIds());
        }
        return orderRes;
    }

    public OrderRes realOrderNouser(OrderReq orderReq, int userIdx) throws BaseException {

        int orderId = createOrder(orderReq, userIdx); //1. order??????, ?????? ???

        //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.?????? api ??????, ???????????? ???
        OrderRes orderRes = completePay(orderId); //????????????

        //???????????? ??????
        if (orderReq.getBasketIds() !=  null ){
            basketService.completeBasket(orderId, orderReq.getBasketIds());
        }
        return orderRes;
    }
}
