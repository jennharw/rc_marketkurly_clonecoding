package com.example.demo.src.user;

import com.example.demo.common.util.TokenGenerator;
import com.example.demo.src.address.AddressService;
import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.basket.BasketProvider;
import com.example.demo.src.basket.model.BasketRes;
import com.example.demo.src.basket.model.BasketResNouser;
import com.example.demo.src.coupon.CouponService;
import com.example.demo.src.item.model.ItemRes;
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

import java.time.LocalDate;
import java.util.ArrayList;
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


    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
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
     * 회원 1명 조회 API
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
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) throws BaseException {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        System.out.println(postUserReq);
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if(!isRegexPhone(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        //전화번호중복
        if (userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1){
            return new BaseResponse<>(POST_USERS_EXISTS_PHONE_NUMBER);
        }

        //비밀번호
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
     * 로그인 API
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
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 아이디 찾기 API - 이메일로! 회원 번호 검색 조회 API
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
     * 비밀번호 찾기 API - 회원 번호 와 이메일 검색 조회 API
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/password") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<PostLoginRes> findPassword(@RequestBody FindUserReq findUserReq) {
        try {
            PostLoginRes getUsersRes = userProvider.getPasswordByEmailAndUsername(findUserReq.getEmail(), findUserReq.getUsername());
            //id, jwt
            //Token넘기기
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호 찾기 API - 회원 번호 와 이메일 검색 조회 API
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/password/phone") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<PostLoginRes> findPasswordByPhoneNumber(@RequestBody FindUserReq findUserReq) {
        try {
            PostLoginRes getUsersRes = userProvider.getPasswordByPhoneNumberAndUsername(findUserReq.getPhoneNumber(), findUserReq.getUsername());
            //id, jwt
            //Token넘기기
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //새로운비번만들기 //비밀번호 기입없이 , 유저 변경
    @ResponseBody
    @PatchMapping("/{userIdx}/new-password")
    public BaseResponse<String> modifyUserPassword(@RequestBody PatchUserReq patchUserReq){
        //(jwt token)만 확인
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if (userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            try {
                patchUserReq.setUserIdx(userIdxByJwt);

                userService.modifyUserInfo(patchUserReq, userIdxByJwt);
                String result = "변경되었습니다";
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
    //logout (jwt token)으로 확인
        try {
            if(jwtService.checkExp()){
                return new BaseResponse<>(EXPIRED_TOKEN);
            }
            //userIdx와 접근한 유저가 같은지 확인
        //같다면 유저네임 변경
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
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            String result = "회원정보 수정 가능";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq){
        //logout (jwt token)으로 확인
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            try {
                patchUserReq.setUserIdx(userIdxByJwt);
                if (userProvider.checkPassword(patchUserReq)){
                    userService.modifyUserInfo(patchUserReq, userIdxByJwt);
                    String result = "변경되었습니다";
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

    //알림설정
//    @ResponseBody
//    @PatchMapping("/{userIdx}")
//    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody NotifyUserReq notifyUserReq){
//        //logout (jwt token)으로 확인
//        try {
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if (userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//            userService.notifyUser(notifyUserReq);
//            String result = "알림해줄게요";
//            return new BaseResponse<>(result);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    /**
     * 회원탈퇴 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PutMapping("/content/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        userService.deleteUser(userIdxByJwt);

        String result = "탈퇴처리 되었습니다";
        return new BaseResponse<>(result);
    }

    //장바구니 담기
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

    //장바구니 조회
    @ResponseBody
    @GetMapping("/{userIdx}/basket/v2")
    public BaseResponse<List<BasketRes>> getBasket(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            List<BasketRes> baskets = basketProvider.getBaskets(userIdx);
            return new BaseResponse<>(baskets);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody //주문하기
    @PostMapping("/{userIdx}/order")
    public BaseResponse<OrderRes> createOrder(@PathVariable("userIdx") int userIdx, @RequestBody OrderReq orderReq) throws BaseException {
        try{
            //TODO : 가격계산, 적립금 할인 등

            //주문하기 -> OrderService realOrder ; refactoring
            OrderRes orderRes = orderService.realOrder(orderReq, userIdx);
            //배송하기

//            int orderId = orderService.createOrder(orderReq, userIdx); //1. order생성, 결제 중
//
//            //paymentService.createPayment(orderRes.getOrderId(), orderRes.getOrderId()); //2.결제 api 결제, 결제처리 중
//            OrderRes orderRes = orderService.completePay(orderId); //결제완료
//
//            //3. 적립금 누적, 포인트 차감
//            GetUserRes user = userProvider.getUser(userIdx);
//            int pointsRateUserLevel = levelService.getLevel(user.getLevel()).getPointsRate();
//
//            //적립금 - 주문위해 사용한 포인트 차감 해서  DB 에 저장
//            int points = user.getPoint() + (orderReq.getPrice() * pointsRateUserLevel) - orderReq.getPoints();
//            userService.givePoints(points, userIdx); // 적립금 적립
//
//            //쿠폰 사용
//            for (Integer couponId : orderReq.getCouponIdList()){
//                couponService.useCoupon(couponId, userIdx);
//            }
//
//            //장바구니 삭제
//            basketService.completeBasket(orderId, orderReq.getBasketIds());

            return new BaseResponse<>(orderRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //주문내역 조회
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

    //주문내역 조회
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


    @ResponseBody //선물하기
    @PostMapping("/{userIdx}/present")
    public BaseResponse<PresentRes> createPresent(@PathVariable("userIdx") int userIdx, @RequestBody PresentReq presentReq) throws BaseException {
        try{
            OrderReq orderReq = OrderReq.builder().orderId(presentReq.getOrderId())
                    .deliverType("선물")
                    .paymentId(presentReq.getPaymentId())
                    .deliverType(presentReq.getDeliverType())
                    .price(presentReq.getPrice())
                    .build();
            OrderRes orderRes = orderService.realOrder(orderReq, userIdx);//결제 중
            int presentId = presentService.givePresent(presentReq, userIdx);//결제 중

            //배송 X ->  선물받는 api 처리
            //재고처리

            //문자보내기
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

    //선물함 조회
    @ResponseBody //선물하기
    @GetMapping("/{userIdx}/present")
    public BaseResponse<List<PresentRes>> createPresent(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            List<PresentRes> presentRes = presentProvider.getPresents(userIdx);
            return new BaseResponse<>(presentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //내 리뷰작성하기
    @ResponseBody
    @PostMapping("/{userIdx}/item/{itemId}/review")
    public BaseResponse<ReviewRes> review(@PathVariable("userIdx") int userIdx, @PathVariable("itemId") int itemId, @RequestBody ReviewReq reviewReq) throws BaseException {
        // TODO: itemId 가 사용자가 구매한 물건인지 확인
        try{

            ReviewRes reviewRes = reviewService.createReview(reviewReq, itemId, userIdx);
            return new BaseResponse<>(reviewRes);


        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //내 리뷰 전체 보기
    @ResponseBody
    @GetMapping("/{userIdx}/reviews")
    public BaseResponse<List<ReviewRes>> getMyReview(@PathVariable("userIdx") int userIdx) throws BaseException {
        try{
            //1 내가 쓴 목록
            List<ReviewRes> reviewRes = reviewProvider.getReviewsByUser(userIdx);
            //2 내가 쓸 목록
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

            //2 내가 쓸 목록
            List<ItemRes> ordersNotWritten = reviewProvider.getReviewsNotWrittenByUser(userIdx);

            return new BaseResponse<>(ordersNotWritten);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //리뷰 하나 보기
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


    //비회원주문조회
    //token생성 username 으로
    //비회원 장바구니
    //장바구니 담기
    //TODO 일반유저 합치기 (userIdx)

    private static final String PREFIX_NOUSER_ENTITY = "tmp_";

    @ResponseBody
    @PostMapping("/nouser/basket")
    public BaseResponse<BasketResNouser> createBasketForNoUser(@RequestBody BasketReq basketReq) throws BaseException {
        try{

            //유저생성
            String noUsername = TokenGenerator.randomCharacterWithPrefix(PREFIX_NOUSER_ENTITY);
            PostUserRes postUserRes = userService.createNoUser(PostUserReq.builder().username(noUsername)
                    .password("nouser")
                    .email("nouser")
                    .phoneNumber("nouser")
                    .createdAt(LocalDate.now())
                    .build());

            int basketId = basketService.createBaskets(basketReq, postUserRes.getUserIdx());
            BasketResNouser basketRes = BasketResNouser.builder().basketId(basketId).userId(postUserRes.getUserIdx()).jwtToken(postUserRes.getJwt()).build();

            return new BaseResponse<>(basketRes);  //이후 주문하기, 임시의 id로

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //wishlist 조회, 생성 -> WishController


    //level -> Level Controller

    //event , event _participants
    //coupon
    //컬리패스

    // -> 결제




}
