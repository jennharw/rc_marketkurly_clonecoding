package com.example.demo.src.basket;

import com.example.demo.src.basket.model.BasketReq;
import com.example.demo.src.basket.model.BasketRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BasketDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createBasket(BasketReq basketReq, int userId) { //status 일단 0, 주문 아직 하지 않음
        String createOrderQuery = "insert into BASKETS (item_id, user_id, count, status) VALUES (?,?,?, 0)";
        Object[] createOrderParams = new Object[]{basketReq.getItemId(),userId, basketReq.getCount()};
        this.jdbcTemplate.update(createOrderQuery, createOrderParams);
        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<BasketRes> getBaskets(int userId) {
        String getBasketsQuery = "select * from BASKETS where user_id = ? and status  = 0";
        int getBaskets = userId;
        return this.jdbcTemplate.query(getBasketsQuery,
                (rs, rowNum) -> BasketRes.builder()
                        .basketId(rs.getInt("id"))
                        .count(rs.getInt("count"))
                        .itemId(rs.getInt("item_id"))
                        .build(),
                getBaskets);
    }
    public int completeBaskets(int basket, int orderId) {
        String completeBasketQuery = "UPDATE BASKETS SET status =?, order_id =? WHERE id = ?";
        Object[] completeBasketParam = new Object[]{1 ,orderId, basket};  // payment 결제
        this.jdbcTemplate.update(completeBasketQuery, completeBasketParam);
        return 1;
    }


}
