package com.example.demo.src.wishlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.wishlist.model.WishReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistDao wishlistDao;
    public int createWishlist(int userIdxByJwt, WishReq wishReq) throws BaseException {
        try{
            int wishId =  wishlistDao.createWishlist(userIdxByJwt, wishReq);
            return wishId;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
