package com.example.demo.src.item;

import com.example.demo.src.item.model.GetHowRes;
import com.example.demo.src.item.model.GetItemMoreRes;
import com.example.demo.src.item.model.GetItemRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app")
public class ItemController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ItemProvider itemProvider;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final JwtService jwtService;




    public ItemController(ItemProvider itemProvider, ItemService itemService, JwtService jwtService){
        this.itemProvider = itemProvider;
        this.itemService = itemService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/item") // (GET) 127.0.0.1:9000/app/items
    public BaseResponse<List<GetItemRes>> getItems() { //성공여부 매핑
        try{
            List<GetItemRes> getItemsRes = itemProvider.getItems();
            return new BaseResponse<>(getItemsRes);
            // Get Users
//            List<GetUserRes> getItemRes = userProvider.getUsersByEmail(Email);
//            return new BaseResponse<>(getItemRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //



    @ResponseBody
    @GetMapping("/category/HOW") // (GET) 127.0.0.1:9000/app/items
    public BaseResponse<List<GetHowRes>> getHows() { //성공여부 매핑
        try{
            List<GetHowRes> getHowsRes = itemProvider.getHows();
            return new BaseResponse<>(getHowsRes);
            // Get Users
//            List<GetUserRes> getItemRes = userProvider.getUsersByEmail(Email);
//            return new BaseResponse<>(getItemRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //    /**
//     * 상품 1개 조회 API
//     */
//    @RequestMapping("/app/item")
//     Path-variable
    @ResponseBody
    @GetMapping("/{ItemId}")
    public BaseResponse<GetItemRes> getItem(@PathVariable("ItemId") int ItemId) { //Items DTO 받는거 아님
        try{
            GetItemRes getItemRes = itemProvider.getItem(ItemId);
            return new BaseResponse<>(getItemRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품설명 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/more/itemsmores") // (GET) 127.0.0.1:9000/app/items
    public BaseResponse<List<GetItemMoreRes>> getItemsmores() { //성공여부 매핑
        try{
            List<GetItemMoreRes> getItemMoreRes = itemProvider.getItemsmores();
            return new BaseResponse<>(getItemMoreRes);
            // Get Users
//            List<GetUserRes> getItemRes = userProvider.getUsersByEmail(Email);
//            return new BaseResponse<>(getItemRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


        /**
     * 상품설명 1개 조회 API
     * [GET] /items/:itemId
     * @return BaseResponse<GetUserRes>
     */

    @ResponseBody
    @GetMapping("/more/{ItemMore}")
    public BaseResponse<GetItemMoreRes> getItemMore(@PathVariable("ItemMore") int ItemMore) {
        try{
            GetItemMoreRes getItemMoreRes = itemProvider.getItemMore(ItemMore);
            return new BaseResponse<>(getItemMoreRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }



    //    Path-variable
//    @ResponseBody
//    @GetMapping("{itemMore}") // (GET) 127.0.0.1:9000/items/itemId
//    public BaseResponse<GetItemMoreRes> getItemMore(@PathVariable("itemMore") int item) {
//        // Get Users
//        try{
//            GetItemMoreRes getItemMoreRes = itemProvider.getItemMore(item);
//            //Get User
//            return new BaseResponse<>(getItemMoreRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }



}
