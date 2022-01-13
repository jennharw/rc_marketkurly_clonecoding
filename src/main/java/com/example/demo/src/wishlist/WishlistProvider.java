package com.example.demo.src.wishlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.wishlist.model.WishRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishlistProvider {
    private final WishlistDao wishlistDao;

    public List<WishRes> getWishlist(int userIdxByJwt) throws BaseException {

            try{
                List<WishRes> wishResList = wishlistDao.getWishlist(userIdxByJwt);
                return wishResList;
            }catch (Exception e){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        }
    }

