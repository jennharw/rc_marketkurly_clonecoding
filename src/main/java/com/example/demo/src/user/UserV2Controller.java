package com.example.demo.src.user;

import com.example.demo.common.util.TokenGenerator;
import com.example.demo.src.address.AddressService;
import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.basket.BasketProvider;
import com.example.demo.src.basket.model.BasketRes;
import com.example.demo.src.basket.model.BasketResNouser;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.itemV2Deleted.ItemV2Provider;
import com.example.demo.src.itemV2Deleted.model.ItemRes;
import com.example.demo.src.level.LevelService;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.order.model.OrderRes;
import com.example.demo.src.basket.BasketService;
import com.example.demo.src.basket.model.BasketReq;
import com.example.demo.src.present.PresentProvider;
import com.example.demo.src.present.PresentService;
import com.example.demo.src.present.model.PresentReq;
import com.example.demo.src.present.model.PresentRes;
import com.example.demo.src.review.ReviewProvider;
import com.example.demo.src.review.ReviewService;
import com.example.demo.src.review.model.ReviewReq;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

@RestController
@RequestMapping("/app/users/v2")
@RequiredArgsConstructor
public class UserV2Controller {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    private final BasketService basketService;

    private final BasketProvider basketProvider;

    private final OrderService orderService;

    private final ReviewService reviewService;

    private final OrderProvider orderProvider;

    private final ReviewProvider reviewProvider;

    private final PresentProvider presentProvider;

    private final LevelService levelService;

    private final CouponService couponService;

    private final AddressService addressService;

    private final PresentService presentService;

    private final PointService pointService;

    private final ItemV2Provider itemV2Provider;

    /**
     * ?????? ?????? API
     * [GET] /users
     * ?????? ?????? ??? ????????? ?????? ?????? API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<List<GetUserRes>> getUsers() {
        try{
            //if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            //}
//            //Get Users
//            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
//            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * ?????? 1??? ?????? API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * ???????????? API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) throws BaseException {
        // TODO: email ????????? ?????? validation ???????????????. ??? ??? ??? ??????????????? ??????????????????!
        System.out.println(postUserReq);
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //????????? ????????????
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if(!isRegexPhone(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        //??????????????????
        if (userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1){
            return new BaseResponse<>(POST_USERS_EXISTS_PHONE_NUMBER);
        }

        //????????????
        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(postUserReq.getUsername() == null){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(postUserReq.getEmail()== null){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        if(postUserReq.getPhoneNumber()== null){
            return new BaseResponse<>(USERS_EMPTY_PHONE_NUMBER);
        }
//        if(postUserReq.getBirth()== null){
//            return new BaseResponse<>(USERS_EMPTY_BIRTH);
//        }
        if(postUserReq.getName()== null){
            return new BaseResponse<>(USERS_EMPTY_NAME);
        }
        if(postUserReq.getAddress()== null){
            return new BaseResponse<>(USERS_EMPTY_ADDRESS);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            addressService.createAddress(postUserRes.getUserIdx(), AddressReq.builder()
                            .address(postUserReq.getAddress())
                            .detailAddress(postUserReq.getDetailAddress())
                    .build());
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * ????????? API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) throws BaseException {
        if (userProvider.checkUsername(postLoginReq.getUsername()) == 0){
            return new BaseResponse<>(CANNOT_FIND_USERNAME);
        }
        try{

            System.out.println(postLoginReq.getUsername());
            // TODO: ????????? ????????? ?????? ???????????? validatin ???????????????????????????!
            // TODO: ????????? status ex) ??????????????? ??????, ????????? ?????? ?????? ??????????????? ????????? ?????? ????????? ?????? validation ????????? ?????????????????????.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * ????????? ?????? API - ????????????! ?????? ?????? ?????? ?????? API
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/id") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<GetUserRes> findId(@RequestParam(required = true) String Email) {
        try {
            GetUserRes getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/id/phone") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<GetUserRes> findIdByPhone(@RequestParam(required = true) String phoneNumber) {
        try {
            if(!isRegexPhone(phoneNumber)){
                return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
            }
            GetUserRes getUsersRes = userProvider.getUsersByPhoneNumber(phoneNumber);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * ???????????? ?????? API - ?????? ?????? ??? ????????? ?????? ?????? API
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/password") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<PostLoginRes> findPassword(@RequestBody FindUserReq findUserReq) {
        try {
            PostLoginRes getUsersRes = userProvider.getPasswordByEmailAndUsername(findUserReq.getEmail(), findUserReq.getUsername());
            //id, jwt
            //Token?????????
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * ???????????? ?????? API - ?????? ?????? ??? ????????? ?????? ?????? API
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/password/phone") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<PostLoginRes> findPasswordByPhoneNumber(@RequestBody FindUserReq findUserReq) {
        try {
            PostLoginRes getUsersRes = userProvider.getPasswordByPhoneNumberAndUsername(findUserReq.getPhoneNumber(), findUserReq.getUsername());
            //id, jwt
            //Token?????????
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //???????????????????????? //???????????? ???????????? , ?????? ??????
    @ResponseBody
    @PatchMapping("/{userIdx}/new-password")
    public BaseResponse<String> modifyUserPassword(@RequestBody PatchUserReq patchUserReq){
        //(jwt token)??? ??????
        try {
            //jwt?????? idx ??????.
            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx??? ????????? ????????? ????????? ??????
//            if (userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            try {
                patchUserReq.setUserIdx(userIdxByJwt);

                userService.modifyUserInfo(patchUserReq, userIdxByJwt);
                String result = "?????????????????????";
                return new BaseResponse<>(result);

            }
            catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }

        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }



    @ResponseBody
    @PatchMapping("/check-token-expiration")
    public BaseResponse<String> testExp(){
    //logout (jwt token)?????? ??????
        try {
            if(jwtService.checkExp()){
                return new BaseResponse<>(EXPIRED_TOKEN);
            }
            //userIdx??? ????????? ????????? ????????? ??????
        //????????? ???????????? ??????
            String result = "";
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(LOGOUT_USER);
        }
    }


    @ResponseBody
    @PostMapping("/user-info")
    public BaseResponse<String> checkForChangeUserInfo(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: ????????? ????????? ?????? ???????????? validatin ???????????????????????????!
            // TODO: ????????? status ex) ??????????????? ??????, ????????? ?????? ?????? ??????????????? ????????? ?????? ????????? ?????? validation ????????? ?????????????????????.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            String result = "???????????? ?????? ??????";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * ?????????????????? API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq){
        //logout (jwt token)?????? ??????
        try {
            //jwt?????? idx ??????.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx??? ????????? ????????? ????????? ??????
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            try {
                patchUserReq.setUserIdx(userIdxByJwt);
                if (userProvider.checkPassword(patchUserReq)){
                    userService.modifyUserInfo(patchUserReq, userIdxByJwt);
                    String result = "?????????????????????";
                    return new BaseResponse<>(result);
                }

            }
            catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }

        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
        return null;
    }

    //????????????
//    @ResponseBody
//    @PatchMapping("/{userIdx}")
//    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody NotifyUserReq notifyUserReq){
//        //logout (jwt token)?????? ??????
//        try {
//            //jwt?????? idx ??????.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx??? ????????? ????????? ????????? ??????
//            if (userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//            userService.notifyUser(notifyUserReq);
//            String result = "??????????????????";
//            return new BaseResponse<>(result);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    /**
     * ???????????? API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PutMapping("/content/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx) throws BaseException {
        //jwt?????? idx ??????.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx??? ????????? ????????? ????????? ??????
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        userService.deleteUser(userIdxByJwt);

        String result = "???????????? ???????????????";
        return new BaseResponse<>(result);
    }

    //???????????? ??????
    @ResponseBody
    @PostMapping("/{userIdx}/basket/v2")
    public BaseResponse<Integer> createBasket(@PathVariable("userIdx") int userIdx, @RequestBody BasketReq basketReq) throws BaseException {
        try{
            int basketId = basketService.createBaskets(basketReq, userIdx);
            return new BaseResponse<>(basketId);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //???????????? ??????
    @ResponseBody
    @GetMapping("/{userIdx}/basket/v2")
    public BaseResponse<List<BasketRes>> getBasket(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            List<BasketRes> baskets = basketProvider.getBaskets(userIdx);
            baskets.stream()
                    .forEach(basketRes -> {
                        try {
                            basketRes.setGetItemRes(
                                    itemV2Provider.getItem(basketRes.getItemId())
                            );
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                    });

            return new BaseResponse<>(baskets);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody //????????????
    @PostMapping("/{userIdx}/order")
    public BaseResponse<OrderRes> createOrder(@PathVariable("userIdx") int userIdx, @RequestBody OrderReq orderReq) throws BaseException {
        try{
            //TODO : ????????????, ????????? ?????? ??? ,

            //??? ?????????
            List<OrderRes> getorderRes = orderProvider.getOrdersByUser(userIdx);
            if (getorderRes.size() == 0){
                //????????? ?????? ??????
                couponService.giveCoupon(6, userIdx);
                couponService.giveCoupon(7, userIdx);

            }

            //???????????? -> OrderService realOrder ; refactoring
            OrderRes orderRes = orderService.realOrder(orderReq, userIdx);
            //????????????

//            int orderId = orderService.createOrder(orderReq, userIdx); //1. order??????, ?????? ???
//
//            //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.?????? api ??????, ???????????? ???
//            OrderRes orderRes = orderService.completePay(orderId); //????????????
//
//            //3. ????????? ??????, ????????? ??????
//            GetUserRes user = userProvider.getUser(userIdx);
//            int pointsRateUserLevel = levelService.getLevel(user.getLevel()).getPointsRate();
//
//            //????????? - ???????????? ????????? ????????? ?????? ??????  DB ??? ??????
//            int points = user.getPoint() + (orderReq.getPrice() * pointsRateUserLevel) - orderReq.getPoints();
//            userService.givePoints(points, userIdx); // ????????? ??????
//
//            //?????? ??????
//            for (Integer couponId : orderReq.getCouponIdList()){
//                couponService.useCoupon(couponId, userIdx);
//            }
//
//            //???????????? ??????
//            basketService.completeBasket(orderId, orderReq.getBasketIds());

            return new BaseResponse<>(orderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //????????? ?????? ??????
    @ResponseBody
    @GetMapping("/{userIdx}/points")
    public BaseResponse<GetUserPoints> getPointsByUser(@PathVariable("userIdx") int userIdx){
        try{
            GetUserPoints getUserPoints = pointService.getPoints(userIdx);
            return new BaseResponse<>(getUserPoints);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //???????????? ??????
    @ResponseBody
    @GetMapping("/{userIdx}/orders")
    public BaseResponse<List<OrderRes>> getOrdersByUser(@PathVariable("userIdx") int userIdx){
        try{
            List<OrderRes> orderRes = orderProvider.getOrdersByUser(userIdx);
            return new BaseResponse<>(orderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //???????????? ??????
    @ResponseBody
    @GetMapping("/{userIdx}/orders/{orderId}")
    public BaseResponse<OrderRes> getOrdersByUser(@PathVariable("userIdx") int userIdx, @PathVariable int orderId){
        try{
            OrderRes orderRes = orderProvider.getOrderByUser(userIdx, orderId);
            return new BaseResponse<>(orderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody //????????????
    @PostMapping("/{userIdx}/present")
    public BaseResponse<PresentRes> createPresent(@PathVariable("userIdx") int userIdx, @RequestBody PresentReq presentReq) throws BaseException {
        try{
            OrderReq orderReq = OrderReq.builder().orderId(presentReq.getOrderId())
                    .deliverType("??????")
                    .paymentId(presentReq.getPaymentId())
                    .deliverType(presentReq.getDeliverType())
                    .price(presentReq.getPrice())
                    .build();
            OrderRes orderRes = orderService.realOrder(orderReq, userIdx);//?????? ???
            int presentId = presentService.givePresent(presentReq, userIdx);//?????? ???

            //?????? X ->  ???????????? api ??????
            //????????????

            //???????????????
            //presentService.sendMessage(presentReq.getMessage(), presentReq.getMessenger(), presentReq.getReceiver_name())

            PresentRes presentRes = PresentRes.builder()
                    .present_id(presentId)
                    .status(orderRes.getStatus())
                    .message(presentReq.getMessage())
                    .receiverName(presentReq.getReceiverName())
                    .messenger(presentReq.getMessenger())
                    .receiverPhoneNumber(presentReq.getReceiverPhoneNumber())
                    .build();
            return new BaseResponse<>(presentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //????????? ??????
    @ResponseBody //????????????
    @GetMapping("/{userIdx}/present")
    public BaseResponse<List<PresentRes>> createPresent(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            List<PresentRes> presentRes = presentProvider.getPresents(userIdx);
            return new BaseResponse<>(presentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //??? ??????????????????
    @ResponseBody
    @PostMapping("/{userIdx}/item/{itemId}/review")
    public BaseResponse<ReviewRes> review(@PathVariable("userIdx") int userIdx, @PathVariable("itemId") int itemId, @RequestBody ReviewReq reviewReq) throws BaseException {
        // TODO: itemId ??? ???????????? ????????? ???????????? ??????
        try{

            ReviewRes reviewRes = reviewService.createReview(reviewReq, itemId, userIdx);
            return new BaseResponse<>(reviewRes);


        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //??? ?????? ?????? ??????
    @ResponseBody
    @GetMapping("/{userIdx}/reviews")
    public BaseResponse<List<ReviewRes>> getMyReview(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            //1 ?????? ??? ??????
            List<ReviewRes> reviewRes = reviewProvider.getReviewsByUser(userIdx);
            //2 ?????? ??? ??????
            //List<Integer> ordersNot = reviewProvider.getReviewsNotWrittenByUser(userIdx);

            return new BaseResponse<>(reviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}/reviews/not-written")
    public BaseResponse<List<ItemRes>> getMyReviewNotWritten(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{

            //2 ?????? ??? ??????
            List<ItemRes> ordersNotWritten = reviewProvider.getReviewsNotWrittenByUser(userIdx);

            return new BaseResponse<>(ordersNotWritten);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //?????? ?????? ??????
    @ResponseBody
    @GetMapping("/review/{reviewId}")
    public BaseResponse<ReviewRes> getReview(@PathVariable("reviewId") int reviewId) {
        try{
            ReviewRes reviewRes = reviewProvider.getReview(reviewId);

            return new BaseResponse<>(reviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //?????????????????????
    //token?????? username ??????
    //????????? ????????????
    //???????????? ??????
    //TODO ???????????? ????????? (userIdx)

    private static final String PREFIX_NOUSER_ENTITY = "tmp_";

    @ResponseBody
    @PostMapping("/nouser/basket")
    public BaseResponse<BasketResNouser> createBasketForNoUser(@RequestBody BasketReq basketReq) throws BaseException {
        try{

            //????????????
            String noUsername = TokenGenerator.randomCharacterWithPrefix(PREFIX_NOUSER_ENTITY);
            PostUserRes postUserRes = null;
            try {
                postUserRes = userService.createNoUser(PostUserReq.builder().username(noUsername)
                        .password("nouser")
                        .email("nouser")
                        .phoneNumber("nouser")
                        .createdAt(LocalDate.now())
                        .build());
            } catch (BaseException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            int basketId = basketService.createBaskets(basketReq, postUserRes.getUserIdx());
            BasketResNouser basketRes = BasketResNouser.builder().basketId(basketId).userId(postUserRes.getUserIdx()).jwtToken(postUserRes.getJwt()).build();

            return new BaseResponse<>(basketRes);  //?????? ????????????, ????????? id???

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //wishlist ??????, ?????? -> WishController


    //level -> Level Controller

    //event , event _participants
    //coupon
    //????????????

    // -> ??????

    @ResponseBody //????????? ????????????
    @PostMapping("/{userIdx}/order/nouser")
    public BaseResponse<OrderRes> createOrderNouser(@PathVariable("userIdx") int userIdx, @RequestBody OrderReq orderReq) throws BaseException {
        try{
            //TODO : ????????????, ????????? ?????? ???

            //???????????? -> OrderService realOrder ; refactoring
            OrderRes orderRes = orderService.realOrderNouser(orderReq, userIdx);
            //????????????

            int nouserId = userService.createNoUserForOrder(orderReq.getInfoNouser(),userIdx);

//            int orderId = orderService.createOrder(orderReq, userIdx); //1. order??????, ?????? ???
//
//            //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.?????? api ??????, ???????????? ???
//            OrderRes orderRes = orderService.completePay(orderId); //????????????
//
//            //3. ????????? ??????, ????????? ??????
//            GetUserRes user = userProvider.getUser(userIdx);
//            int pointsRateUserLevel = levelService.getLevel(user.getLevel()).getPointsRate();
//
//            //????????? - ???????????? ????????? ????????? ?????? ??????  DB ??? ??????
//            int points = user.getPoint() + (orderReq.getPrice() * pointsRateUserLevel) - orderReq.getPoints();
//            userService.givePoints(points, userIdx); // ????????? ??????
//
//            //?????? ??????
//            for (Integer couponId : orderReq.getCouponIdList()){
//                couponService.useCoupon(couponId, userIdx);
//            }
//
//            //???????????? ??????
//            basketService.completeBasket(orderId, orderReq.getBasketIds());

            return new BaseResponse<>(orderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
