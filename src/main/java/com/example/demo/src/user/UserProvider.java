package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{ //List<GetUserRes>
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes
                    .stream()
                    .map(getUserRes1 -> {
                        getUserRes1.afterBirthString();
                        return getUserRes1;
                    })
                    .collect(Collectors.toList());
            //return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUsersByEmail(String email) throws BaseException{
        try{
            GetUserRes getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(CANNOT_FIND_USERID);
        }
                    }


    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            getUserRes.afterBirthString();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            //password decode
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx); //jwt token
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }



    public int checkUsername(String username) throws BaseException {
        try{
            return userDao.checkUsername(username);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkPassword(PatchUserReq patchUserReq) throws BaseException{
        User user = userDao.getPwdById(patchUserReq);
        String password;
        try {
            //password decode
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
            if(patchUserReq.getPassword().equals(password)){
                return true;
            }
            else{
                throw new BaseException(FAILED_TO_LOGIN);
            }

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
            }

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try{
            return userDao.checkPhoneNumber(phoneNumber);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUsersByPhoneNumber(String phoneNumber) throws BaseException {
        try{
            GetUserRes getUsersRes = userDao.getUsersByPhoneNumber(phoneNumber);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(CANNOT_FIND_USERID);
        }
    }

    public PostLoginRes getPasswordByEmailAndUsername(String email, String username) throws BaseException {

        try{
            GetUserRes getUsersRes = userDao.getUsersByEmailAndUsername(email, username);
            String jwt = jwtService.createJwt(getUsersRes.getUserId()); //jwt token
            return new PostLoginRes(getUsersRes.getUserId(),jwt); // id, jwt

            //return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(CANNOT_FIND_PASSWORD);

        }
    }
    public PostLoginRes getPasswordByPhoneNumberAndUsername(String phoneNumber, String username) throws BaseException {
        try{
            GetUserRes getUsersRes = userDao.getUsersByPhoneNumber(phoneNumber);
            String jwt = jwtService.createJwt(getUsersRes.getUserId()); //jwt token
            return new PostLoginRes(getUsersRes.getUserId(),jwt); // id, jwt
        }
        catch (Exception exception) {
            throw new BaseException(CANNOT_FIND_PASSWORD);

        }
    }
}
