package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.item.model.ItemRes;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ReviewProvider {

    private final ReviewDao reviewDao;

    private final OrderProvider orderProvider;

    public List<ReviewRes> getReviewsByUser(int userIdx) throws BaseException {
        try{
            List<ReviewRes> reviewRes = (List<ReviewRes>) reviewDao.getReviewsByUser(userIdx);
            System.out.println(reviewRes);
            return reviewRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public ReviewRes getReview(int reviewId) throws BaseException {
        try{
            ReviewRes reviewRes = reviewDao.getReviewByUser(reviewId);
            System.out.println(reviewRes);
            return reviewRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ItemRes> getReviewsNotWrittenByUser(int userIdx) throws BaseException {
        List<ReviewRes> reviewRes = getReviewsByUser(userIdx);
        List<Integer> orderReviewedIds = new ArrayList<>();

        List<ItemRes> items = orderProvider.getOrderedItemsByUser(userIdx);
        List<Integer> orderItemList = new ArrayList<>();
        for (ItemRes item: items){
            orderItemList.add(item.getItemId());

        }
        for (ReviewRes review: reviewRes){
            orderReviewedIds.add(review.getItemId());
            orderItemList.remove(new Integer(review.getItemId()));
        }

        return orderItemList.stream()
                .map(integer -> ItemRes.builder().itemId(integer).build())
                .collect(Collectors.toList());

        //return orderItemList;
    }
}
