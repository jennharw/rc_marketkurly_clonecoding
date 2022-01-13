package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.OrderDao;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.review.model.ReviewReq;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final OrderDao orderDao;
    private final OrderService orderService;

    public ReviewRes createReview(ReviewReq reviewReq, int itemId, int userId) throws BaseException {
        try { // 주문 제품인지
            if (orderService.checkItem(itemId, userId)){
            } else {
                throw new BaseException(NOT_BUY_ITEM);

            }
        }catch (Exception e){
            throw new BaseException(NOT_BUY_ITEM);
        }

        try {                 //리뷰썻는지도 확인
            if (checkReviewItem(itemId, userId)){
            }else {
                throw new BaseException(ALREADY_WRITE_REVIEW);
            }
        }catch (Exception e) {
            throw new BaseException(ALREADY_WRITE_REVIEW);
        }
        try {
            return reviewDao.createReview(reviewReq, itemId, userId); //orderId?
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    private boolean checkReviewItem(int itemId, int userId) throws BaseException {
        try{
            return orderDao.checkReviewItem(itemId, userId);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
