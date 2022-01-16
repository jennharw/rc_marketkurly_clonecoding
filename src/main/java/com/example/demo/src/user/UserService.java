package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.time.LocalDate;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        if (userProvider.checkUsername(postUserReq.getUsername()) == 1){
            throw new BaseException(POST_USERS_EXISTS_USERNAME);
        }

        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            System.out.println(ignored);
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        //try{
            //encode password User db 저장
            postUserReq.setCreatedAt(LocalDate.now());
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public PostUserRes createNoUser(PostUserReq postUserReq) throws BaseException {
        postUserReq.setCreatedAt(LocalDate.now());
        int userIdx = userDao.createUser(postUserReq);
        String jwt = jwtService.createJwt(userIdx);
        return new PostUserRes(jwt, userIdx);
    }

    public void modifyUserInfo(PatchUserReq patchUserReq,int userIdxByJwt) throws BaseException {
        try{
            if (patchUserReq.getNewPassword() != null){
                String pwd;
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchUserReq.getNewPassword());
                patchUserReq.setPassword(pwd);
            }
            int result = userDao.modifyUserName(patchUserReq, userIdxByJwt);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(int userIdxByJwt) {
        try{
            int result = userDao.deleteUser(userIdxByJwt);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            try {
                throw new BaseException(DATABASE_ERROR);
            } catch (BaseException e) {
                e.printStackTrace();
            }
        }
    }

    public void logoutUser(int userIdx) {
        try{
            int result = userDao.logoutUser(userIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            try {
                throw new BaseException(DATABASE_ERROR);
            } catch (BaseException e) {
                e.printStackTrace();
            }
        }
    }


    public Integer givePoints(Double points, int userIdx) throws BaseException {
        try{

            int result = userDao.changePoints(points, userIdx);
            return result;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
