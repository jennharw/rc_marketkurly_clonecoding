package com.example.demo.src.wishlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.itemV2Deleted.ItemV2Provider;
import com.example.demo.src.itemV2Deleted.model.GetItemRes;
import com.example.demo.src.wishlist.model.WishReq;
import com.example.demo.src.wishlist.model.WishRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
@RestController
@RequestMapping("/app/wishlist")
@RequiredArgsConstructor
public class WishController {
    private final JwtService jwtService;
    private final WishlistService wishlistService;
    private final WishlistProvider wishlistProvider;

    private final ItemV2Provider itemV2Provider;

    //찜 목록 보기
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetItemRes>> getWishlist(@PathVariable("userIdx") int userIdx) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        List<WishRes> wishResList = wishlistProvider.getWishlist(userIdxByJwt);
        List<GetItemRes> wishes = wishResList.stream()
                .map(wishRes -> {
                    try {
                        return itemV2Provider.getItem(wishRes.getItemId());
                    } catch (BaseException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        //GetItemRes getItemRes = itemProvider.getItem(ItemId);

        return new BaseResponse<>(wishes);
    }

    //wishlist 생성 하기
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<Integer> createWishlist(@PathVariable("userIdx") int userIdx, @RequestBody WishReq wishReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        int wish_id = wishlistService.createWishlist(userIdxByJwt, wishReq);
        String result = "";
        return new BaseResponse<>(wish_id);
    }


    //wishlist 생성 하기
    @ResponseBody
    @DeleteMapping("/{userIdx}")
    public BaseResponse<Integer> deleteWishlist(@PathVariable("userIdx") int userIdx, @RequestBody WishReq wishReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        int wish_id = wishlistService.deleteWishlist(userIdxByJwt, wishReq);
        String result = "";
        return new BaseResponse<>(wish_id);
    }

}
