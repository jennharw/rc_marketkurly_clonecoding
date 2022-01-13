package com.example.demo.src.payment;

import com.example.demo.src.payment.model.PaymentReq;
import com.example.demo.src.payment.model.PaymentRes;
import com.example.demo.src.review.model.ReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int createPayment(PaymentReq paymentReq) {
        String cretePaymentQuery = "insert into PAYMENTS (discount , card_type ,description) values (?,?,?)";
        Object[] createPaymentParam = new Object[]{paymentReq.getDiscount(), paymentReq.getCardType(), paymentReq.getDescription()};
        this.jdbcTemplate.update(cretePaymentQuery, createPaymentParam);


        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //Integ
    }

    public List<PaymentRes> getPayment() {
        String getPaymentQuery = "select * from PAYMENTS";

        return this.jdbcTemplate.query(getPaymentQuery,
                (rs, rowNum) -> PaymentRes.builder()
                        .id(rs.getInt("id"))
                        .discount(rs.getInt("discount"))
                        .cardType(rs.getString("card_type"))
                        .description(rs.getString("description"))
                        .build()
                );
    }
}
