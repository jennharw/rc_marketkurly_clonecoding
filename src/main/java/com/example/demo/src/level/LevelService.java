package com.example.demo.src.level;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.level.model.LevelReq;
import com.example.demo.src.level.model.LevelRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelDao levelDao;

    public int createLevel(LevelReq levelReq) throws BaseException {
        try{
            int levelId = levelDao.createLevel(levelReq);
            return levelId;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public LevelRes getLevel(int levelId) throws BaseException {
        try{
            LevelRes levelRes = levelDao.getLevel(levelId);
            return levelRes;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<LevelRes> getLevels() throws BaseException {
        try{
            List<LevelRes> levelRes = levelDao.getLevels();
            return levelRes;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

//    public int getLevelUsers(LevelReq levelReq) throws BaseException {
//        try{
//            int levelId = levelDao.getLevelUsers(levelReq);
//            return levelId;
//        }catch (Exception e){
//            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//        }
//    }
//
//    public int getUserLevel(int userIdxByJwt) {
//    }
}
