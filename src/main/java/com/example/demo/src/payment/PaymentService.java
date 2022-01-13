package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.PaymentReq;
import com.example.demo.src.payment.model.PaymentRes;
import com.example.demo.src.review.model.ReviewReq;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentDao paymentDao;
    public int createPayment(PaymentReq paymentReq) throws BaseException {
//        try{
            int paymentRes = paymentDao.createPayment(paymentReq);
            return paymentRes;
//        }catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }

    }

    public List<PaymentRes> getPayment() throws BaseException {
       // try{
            List<PaymentRes> paymentRes = paymentDao.getPayment();
            return paymentRes;
//        }catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
    }
}
