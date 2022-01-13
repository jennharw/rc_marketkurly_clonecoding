package com.example.demo.src.present;

import com.example.demo.config.BaseException;
import com.example.demo.src.present.model.PresentRes;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class PresentProvider {

    private final PresentDao presentDao;

    public List<PresentRes> getPresents(int userIdx) throws BaseException {
        try{
            List<PresentRes> presentRes = presentDao.getPresentsByUser(userIdx);
            return presentRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);

        }
    }
}
