package com.example.demo.src.itemV2Deleted;


import com.example.demo.config.BaseException;
import com.example.demo.src.itemV2Deleted.model.GetHowRes;
import com.example.demo.src.itemV2Deleted.model.GetItemMoreRes;
import com.example.demo.src.itemV2Deleted.model.GetItemRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ItemV2Provider {

    private final ItemV2Dao itemV2Dao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass()); // POST

    @Autowired
    public ItemV2Provider(ItemV2Dao itemV2Dao, JwtService jwtService) {
        this.itemV2Dao = itemV2Dao;
        this.jwtService = jwtService;
    }

//    /**
//     * 상품 전체 조회 API
//     */
//
//    public List<GetItemRes> getItems() throws BaseException{ //Users -> Items로 변경
//        try{
//            List<GetItemRes> getItemRes = itemV2Dao.getItems(); //Users -> Items로 변경
//            return getItemRes; //GetUserRes -> GetItemsRes
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
////HOW 전체 조회 API
//
//    public List<GetHowRes> getHows() throws BaseException{ //Users -> Items로 변경
//        try{
//            List<GetHowRes> getHowRes = itemV2Dao.getHows(); //Users -> Items로 변경
//            return getHowRes; //GetUserRes -> GetItemsRes
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }





    // 상품 1개 조회
    public GetItemRes getItem(int ItemId) throws BaseException {
        try {
            GetItemRes getItemRes = itemV2Dao.getItem(ItemId);
            return getItemRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //    /**
////     * 상품설명 전체 조회 API
////     */
//    public List<GetItemMoreRes> getItemsmores() throws BaseException{ //Users -> Items로 변경
//        try{
//            List<GetItemMoreRes> getItemMoreRes = itemV2Dao.getItemsmores(); //Users -> Items로 변경
//            return getItemMoreRes;
//            //GetUserRes -> GetItemsRes
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    //    // 상세상품 1개 조회
//    public GetItemMoreRes getItemMore(int ItemMore) throws BaseException {
//        try {
//            GetItemMoreRes getItemMoreRes = itemV2Dao.getItemMore(ItemMore);
//            return getItemMoreRes;
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


//    // 상세상품 1개 조회
//    public GetItemMoreRes getItemMore(int IDX) throws BaseException {
//        try {
//            GetItemMoreRes getItemMoreRes = itemDao.getItemMore(IDX);
//            return getItemMoreRes;
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }





}
