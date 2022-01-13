package com.example.demo.src.level;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.level.model.Level;
import com.example.demo.src.level.model.LevelReq;
import com.example.demo.src.level.model.LevelRes;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/level")
@RequiredArgsConstructor
public class LevelController {
    //
    private final LevelService levelService;
    private final JwtService jwtService;

    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createLevel(@RequestBody LevelReq levelReq) {

        try{
            int levelId = levelService.createLevel(levelReq);
            String result = "";
            return new BaseResponse<>(levelId);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<LevelRes> getLevel(@RequestParam String levelId) {

        try{
            LevelRes levelInfo = levelService.getLevel(Integer.parseInt(levelId));
            return new BaseResponse<>(levelInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/all")
    public BaseResponse<List<LevelRes>> getLevels() {

        try{
            List<LevelRes> levelInfo = levelService.getLevels();
            return new BaseResponse<>(levelInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    @ResponseBody
//    @GetMapping("")
//    public BaseResponse<String> getLevelUsers(@RequestBody LevelReq levelReq) {
//
//        try{
//            int levelId = levelService.getLevelUsers(levelReq);
//            String result = "";
//            return new BaseResponse<>(result);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//
//    //user의 Level 확인하기 -> 적립률 (결제시) , 할인율 보여주기
//    @ResponseBody
//    @GetMapping("")
//    public BaseResponse<String> getUserLevel() {
//
//        try{
//            int userIdxByJwt = jwtService.getUserIdx();
//
//            int levelId = levelService.getUserLevel(userIdxByJwt);
//            String result = "";
//            return new BaseResponse<>(result);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
}
