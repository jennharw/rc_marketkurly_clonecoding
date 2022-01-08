package com.example.demo.src.user;

import com.example.demo.src.deliver.DeliverProvider;
import com.example.demo.src.deliver.DeliverService;
import com.example.demo.src.deliver.model.DeliverRes;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.review.ReviewProvider;
import com.example.demo.src.review.ReviewService;
import com.example.demo.src.review.model.ReviewReq;
import com.example.demo.src.review.model.ReviewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    private final OrderService orderService;

    private final DeliverService deliverService;

    private final ReviewService reviewService;

    private final DeliverProvider deliverProvider;

    private final ReviewProvider reviewProvider;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, OrderService orderService, DeliverService deliverService, ReviewService reviewService, DeliverProvider deliverProvider, ReviewProvider reviewProvider){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.orderService = orderService;
        this.deliverService = deliverService;
        this.reviewService = reviewService;
        this.deliverProvider = deliverProvider;
        this.reviewProvider = reviewProvider;
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
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users BaseResponse<List<GetUserRes>>
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
            if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            //Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
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
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        System.out.println(postUserReq);
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //비밀번호
        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
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
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
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
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
        //logout (jwt token)으로 확인
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경

            //logout table 에 없다면
            if(userService.checkLogout(userIdx))
            {

            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
            userService.modifyUserName(patchUserReq);

            String result = "";

            return new BaseResponse<>(result);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
        return new BaseResponse<>(LOGOUT_USER);

    }

    /**
     * 회원탈퇴 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/delete/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx) throws BaseException {
                    //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
        // 같다면 회원탈퇴

        //logout 체크
        if(userService.checkLogout(userIdx)) {

            PatchUserReq patchUserReq = new PatchUserReq(userIdx);
            userService.deleteUser(patchUserReq);
        }

        String result = "";
        return new BaseResponse<>(result);
    }

    //주문하기
    @ResponseBody
    @PostMapping("/{userIdx}/deliver")
    public BaseResponse<DeliverRes> deliver(@PathVariable("userIdx") int userIdx, @RequestBody List<OrderReq> orderReqList) throws BaseException {
        try{
            int deliverId = orderService.createOrders(orderReqList);
            //paymentService.createPayment();
            DeliverRes deliverRes = deliverService.createDelivers(deliverId, userIdx);
            return new BaseResponse<>(deliverRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //리뷰작성하기
    @ResponseBody
    @PostMapping("/{userIdx}/{deliverId}/review")
    public BaseResponse<ReviewRes> review(@PathVariable("userIdx") int userIdx, @PathVariable("deliverId") int deliverId, @RequestBody ReviewReq reviewReq) throws BaseException {
        //TODO : deliver.userId = userId
        // user 리뷰 조회..

        try{
            ReviewRes reviewRes = reviewService.createReview(reviewReq, deliverId);
            return new BaseResponse<>(reviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //주문내역 조회
    @ResponseBody
    @GetMapping("/{userIdx}/delivers")
    public BaseResponse<List<DeliverRes>> getDeliversByUser(@PathVariable("userIdx") int userIdx){
        try{
            List<DeliverRes> deliverRes = deliverProvider.getDeliversByUser(userIdx);
            return new BaseResponse<>(deliverRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //리뷰 조회
    @ResponseBody
    @GetMapping("/{userIdx}/reviews")
    public BaseResponse<List<ReviewRes>> getReviewsByUser(@PathVariable("userIdx") int userIdx){
        try{
            List<ReviewRes> reviewRes = reviewProvider.getReviewsByUser(userIdx);
            return new BaseResponse<>(reviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    //frontend how to get access token from kakao




    @ResponseBody
    @GetMapping("/{userIdx}/logout")
    public BaseResponse<String> logout(@PathVariable int userIdx) throws BaseException {
        // 로그아웃, REDIS 대신 DB로 로그아웃 테이블 만들고, (BLACK BOX) – 로그아웃된 사용자 확인
        //jwt token으로 아이디 확인, exp 도 마찬가지
        //LOGOUT table 생성
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        //logout table 에 없다면
        if(userService.checkLogout(userIdx)){

            userService.logoutUser(userIdx);

            String result = "";
            return new BaseResponse<>(result);
        }
        return new BaseResponse<>(INVALID_USER_JWT);
        //logout (jwt token)으로 확인
    }



}
