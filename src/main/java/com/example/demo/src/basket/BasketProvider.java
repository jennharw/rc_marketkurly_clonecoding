package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.BasketRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class BasketProvider {

    private final BasketDao basketDao;

    public List<BasketRes> getBaskets(int userId) throws BaseException {
        try{
            List<BasketRes> baskets = basketDao.getBaskets(userId);
            return baskets;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }


}
