package com.example.demo.src.order;

import com.example.demo.src.item.model.ItemRes;
import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.order.model.OrderRes;
import com.example.demo.src.review.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderRes> getDelivers(int userId){
        String getDeliverQuery = "select * from ORDERS where user_id = ?";
        int getDeliverByUserId = userId;
        return this.jdbcTemplate.query(getDeliverQuery,
                (rs, rowNum) -> OrderRes.builder()
                        .orderId(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .paymentId(rs.getInt("payment_id"))
                        .status(rs.getString("status"))
                       .build(), getDeliverByUserId);
    }

    public int createOrder(OrderReq order, int userIdx) {
        String createDeliverQuery = "insert into ORDERS (user_id, payment_id,deliver_address_id,total_price,status) values (?,?,?,?,?)";
        Object[] createDeliverParam = new Object[]{userIdx, order.getPaymentId(),order.getDeliverAddressId(),order.getPrice(),"결제 중"};  // payment 결제
        this.jdbcTemplate.update(createDeliverQuery, createDeliverParam);
        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public OrderRes completePay(int orderId) {
        String createDeliverQuery = "UPDATE ORDERS SET status =? WHERE id =?;";
        Object[] createDeliverParam = new Object[]{"결제 완료", orderId};  // payment 결제
        this.jdbcTemplate.update(createDeliverQuery, createDeliverParam);
        return OrderRes.builder().orderId(orderId).status("결제 완료").build();
    }

    public OrderRes getOrder(int userIdx, int orderId) {
        String deliverQuery = "select id, user_id, payment_id, status from ORDERS where id = ?";
        int deliverParam = orderId;
        return this.jdbcTemplate.queryForObject(deliverQuery,
                (rs, rowNum) -> OrderRes.builder()
                        .orderId(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .paymentId(rs.getInt("payment_id"))
                        .status(rs.getString("status"))
                        .build()
                ,deliverParam);

    }

    public boolean checkOrderItem(int itemId, int userId) {
        String checkOrderItemQuery = "SELECT item_id FROM BASKETS b LEFT JOIN ORDERS o ON b.order_id = o.id where o.user_id =? AND o.status = ?";
        Object[] createCheckParam = new Object[]{userId ,"결제 완료"};
        List<OrderItem> items = this.jdbcTemplate.query(checkOrderItemQuery,
                (rs, rowNum) -> OrderItem.builder()
                        .id(rs.getInt("item_id"))
                        .build(),
                createCheckParam);
        List<Integer> itemLists = new ArrayList<>();
        for (OrderItem item : items){
            itemLists.add(item.getId());
        }
        //결제한 상품
//
//        //리뷰한 상품
//        String checkReviewItemQuery = "SELECT item_id FROM REVIEWS where user_id =?";
//        List<OrderItem> reviewedItems = this.jdbcTemplate.query(checkReviewItemQuery,
//                (rs, rowNum) -> OrderItem.builder()
//                        .id(rs.getInt("id"))
//                        .build(),
//                userId);
//        for (OrderItem r : reviewedItems){
//            itemLists.remove(new Integer(r.getId()));
//        }

        return itemLists.contains(itemId);
    }

    public boolean checkReviewItem(int itemId, int userId) {
        String checkOrderItemQuery = "SELECT item_id FROM BASKETS b LEFT JOIN ORDERS o ON b.order_id = o.id where o.user_id =? AND o.status = ?";
        Object[] createCheckParam = new Object[]{userId ,"결제 완료"};
        List<ItemRes> items = this.jdbcTemplate.query(checkOrderItemQuery,
                (rs, rowNum) -> ItemRes.builder()
                        .itemId(rs.getInt("item_id"))
                        .build(),
                createCheckParam);
        List<Integer> itemLists = new ArrayList<>();
        for (ItemRes item : items){
            itemLists.add(item.getItemId());
        }
        //결제한 상품

        //리뷰한 상품
        String checkReviewItemQuery = "SELECT item_id FROM REVIEWS where user_id =?";
        List<OrderItem> reviewedItems = this.jdbcTemplate.query(checkReviewItemQuery,
                (rs, rowNum) -> OrderItem.builder()
                        .id(rs.getInt("item_id"))
                        .build(),
                userId);
        for (OrderItem r : reviewedItems){
            itemLists.remove(new Integer(r.getId()));
        }

        return itemLists.contains(itemId);
    }

    public List<ItemRes> getOrderedItemsByUser(int userIdx) {
        String checkOrderItemQuery = "SELECT item_id FROM BASKETS b LEFT JOIN ORDERS o ON b.order_id = o.id where o.user_id =? AND o.status = ?";
        Object[] createCheckParam = new Object[]{userIdx ,"결제 완료"};
        return this.jdbcTemplate.query(checkOrderItemQuery,
                (rs, rowNum) -> ItemRes.builder()
                        .itemId(rs.getInt("item_id"))
                        .build(),
                createCheckParam);

    }


//
//    public List<DeliverRes> getDeliversByUser(int userIdx) {
//        S


   // }
}
