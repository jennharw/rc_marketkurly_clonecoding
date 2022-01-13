package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.PaymentReq;
import com.example.demo.src.payment.model.PaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/app/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    //payment생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createPayment(@RequestBody PaymentReq paymentReq) throws BaseException {

        try{
            int payment = paymentService.createPayment(paymentReq);
            return new BaseResponse<>(payment);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //user payment 조회
    @ResponseBody //
    @GetMapping("")
    public BaseResponse<List<PaymentRes>> getPayment() throws BaseException {
        try{
            List<PaymentRes> paymentRes = paymentService.getPayment();
            return new BaseResponse<>(paymentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
}
