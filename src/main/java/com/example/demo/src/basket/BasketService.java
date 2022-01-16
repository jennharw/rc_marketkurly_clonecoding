package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.BasketReq;
import com.example.demo.src.basket.model.BasketRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketDao basketDao;


    public int createBaskets(BasketReq basketReq, int userid) throws BaseException {
        try {
            //private int orderId = 1;

            //orderId++;
           // orderId = orderDao.createOrder(basketReq, userid);

           // List<BasketRes> baskets = orderDao.getBaskets(orderId);
            int basketId = basketDao.createBasket(basketReq, userid);

            return basketId;

        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void completeBasket(int orderId, List<Integer> basketIds) throws BaseException {
        try{
            for (Integer basket : basketIds){
                basketDao.completeBaskets(basket, orderId);
            }

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public int deleteBasket(int basketIdx) throws BaseException {
        try{
            int addressId = basketDao.deleteBasket(basketIdx);
            return addressId;
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
