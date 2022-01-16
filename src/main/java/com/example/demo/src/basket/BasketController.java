package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    @ResponseBody //장바구니삭제
    @DeleteMapping("/kurly/{basketIdx}")
    public BaseResponse<String> deleteAddress(@PathVariable("basketIdx") int basketIdx) throws BaseException {

        try{
            int basketId = basketService.deleteBasket(basketIdx);
            return new BaseResponse<>("삭제되었습니다");
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
