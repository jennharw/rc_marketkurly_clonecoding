package com.example.demo.src.present;

import com.example.demo.src.payment.model.PaymentRes;
import com.example.demo.src.present.model.PresentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PresentDao {
    private JdbcTemplate jdbcTemplate;
    public List<PresentRes> getPresentsByUser(int userIdx) {
        String getPresentQuery = "select * from PRESENTS WHERE user_id = ?";

        return this.jdbcTemplate.query(getPresentQuery,
                (rs, rowNum) -> PresentRes.builder()
                        .userId(rs.getInt("user_id"))
                        .orderId(rs.getInt("order_id"))
                        .message(rs.getString("message"))
                        .receiverPhoneNumber(rs.getString("receiver_phone_number"))
                        .receiverName(rs.getString("receiver_name"))
                        .build(), userIdx
        );
    }
}
