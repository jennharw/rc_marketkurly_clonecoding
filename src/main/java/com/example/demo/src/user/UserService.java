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
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            //encode password User db 저장
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(PatchUserReq patchUserReq) {
        try{
            int result = userDao.deleteUser(patchUserReq);
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

    public boolean checkLogout(int userIdx) {
        try{
            if (userDao.checkLogout(userIdx) == 0){
                return true; //logout table 에 없으면
            }else{
                throw new BaseException(LOGOUT_USER);
            }
        } catch(Exception exception){
            try {
                throw new BaseException(DATABASE_ERROR);
            } catch (BaseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean checkId(int kakaoId) {
        if (userDao.checkId(kakaoId)==1){
            return true;
        }
        return false;//DB 에 없음, 새로 가입
    }
}
