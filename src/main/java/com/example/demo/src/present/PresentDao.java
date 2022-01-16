package com.example.demo.src.present;

import com.example.demo.src.payment.model.PaymentRes;
import com.example.demo.src.present.model.PresentReq;
import com.example.demo.src.present.model.PresentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PresentDao { //STATUS 를 선물로 바꿔야 ...

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    public List<PresentRes> getPresentsByUser(int userIdx) {
        String getPresentQuery = "select * from PRESENTS WHERE user_id = ?";

        return this.jdbcTemplate.query(getPresentQuery,
                (rs, rowNum) -> PresentRes.builder()
                        .present_id(rs.getInt("present_id"))
                        .userId(rs.getInt("user_id"))
                        .orderId(rs.getInt("order_id"))
                        .message(rs.getString("message"))
                        .messenger(rs.getString("messenger"))
                        .receiverPhoneNumber(rs.getString("receiver_phone_number"))
                        .receiverName(rs.getString("receiver_name"))
                        .build(), userIdx
        );
    }

    public int givePresent(int userIdx, PresentReq presentReq) {
        String createUserQuery = "insert into PRESENTS (user_id,order_id,created_at,status, message, messenger, receiver_name, receiver_phone_number) VALUES (?,?,?,?,?,?,?, ?)";
        Object[] createUserParams = new Object[]{userIdx, presentReq.getOrderId(), LocalDate.now(), 0, presentReq.getMessage(), presentReq.getMessenger(), presentReq.getReceiverName(), presentReq.getReceiverPhoneNumber()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
