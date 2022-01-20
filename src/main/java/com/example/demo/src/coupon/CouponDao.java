package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.CouponIdReq;
import com.example.demo.src.coupon.model.CouponIdRes;
import com.example.demo.src.coupon.model.CouponReq;
import com.example.demo.src.coupon.model.CouponRes;
import com.example.demo.src.level.model.LevelRes;
import com.example.demo.src.user.model.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CouponRes> getCouponByEvent(int eventId) {
        String getCouponQuery = "select * from COUPONS where event_id = ?"; //coupon ID
        int getCouponParam = eventId;
        return this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> CouponRes.builder()
                        .id(rs.getInt("id"))
                        .eventId(rs.getInt("event_id"))
                        .couponName(rs.getString("coupon_name"))
                        .discount(rs.getInt("discount"))
                        .build(),getCouponParam);
    }


    public List<CouponIdRes> getUserCoupon(int userIdxByJwt) {
        //String getCouponQuery = "select * from COUPON_RECEIVERS where user_id = ? AND status = 0";
        String getCouponQuery = "select * from COUPON_RECEIVERS where user_id =? AND status = 0";

        ; //coupon ID
        int getCouponParam = userIdxByJwt;
        return this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> CouponIdRes.builder()
                        .couponId(rs.getInt("coupon_id"))
                        .isUsed(rs.getInt("status"))
                        .userIdx(rs.getInt("user_id"))
                        .build(),getCouponParam);
    }


    public CouponRes getCoupon(int couponId) {
        String getCouponQuery = "select * from COUPONS where id = ?"; //coupon ID
        int getCouponParam = couponId;
        return (CouponRes) this.jdbcTemplate.queryForObject(getCouponQuery,
                (rs, rowNum) -> CouponRes.builder()
                        .id(rs.getInt("id"))
                        .eventId(rs.getInt("event_id"))

                        .couponName(rs.getString("coupon_name"))
                        .discount(rs.getInt("discount"))
                        .description(rs.getString("description"))
                        .expired_date(rs.getTimestamp("expired_date"))
                        .build(),getCouponParam);
    }

    public void giveCouponUser(int userId, int couponId) {
        String createUserCouponQuery = "insert into COUPON_RECEIVERS (user_id, coupon_id ,status) VALUES (?,?,0)";
        Object[] createUserCouponParams = new Object[]{userId,couponId};
        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);
    }

    public void userCoupon(int userId, int couponId){
        String updateUserCouponQuery = "UPDATE COUPON_RECEIVERS SET status = ? where coupon_id =? AND user_id = ? ";
        Object[] updateUserCouponParams = new Object[]{1,couponId,userId};
        this.jdbcTemplate.update(updateUserCouponQuery, updateUserCouponParams);

    }

    public int createCoupon(CouponReq couponReq) {
        String createUserCouponQuery = "insert into COUPONS (coupon_name ,discount, event_id) VALUES (?,?,?)";
        Object[] createUserCouponParams = new Object[]{couponReq.getCouponName(), couponReq.getDiscount(), couponReq.getEventId()};
        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);
        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<CouponRes> getCoupons() {
        String getCouponQuery = "select * from COUPONS"; //coupon ID
        return (List<CouponRes>) this.jdbcTemplate.query(getCouponQuery,
                (rs, rowNum) -> CouponRes.builder()
                        .id(rs.getInt("id"))
                        .eventId(rs.getInt("event_id"))

                        .couponName(rs.getString("coupon_name"))
                        .discount(rs.getInt("discount"))
                        .description(rs.getString("description"))
                        .expired_date(rs.getTimestamp("expired_date"))
                        .build());

    }
}
