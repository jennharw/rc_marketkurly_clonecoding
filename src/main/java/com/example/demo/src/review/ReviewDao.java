package com.example.demo.src.review;

import com.example.demo.src.itemV2Deleted.model.ItemRes;
import com.example.demo.src.review.model.ReviewReq;
import com.example.demo.src.review.model.ReviewRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ReviewRes createReview(ReviewReq reviewReq, int itemId, int userId) {
        String creteReviewQuery = "insert into REVIEWS (item_id,user_id ,title, description) values (?,?,?,?)";
        Object[] createReviewParam = new Object[]{itemId,userId ,reviewReq.getTitle(), reviewReq.getDescription()};
        this.jdbcTemplate.update(creteReviewQuery, createReviewParam);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        int reviewId =  this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
        return ReviewRes.builder().reviewId(reviewId)
                .orderId(itemId)
                .title(reviewReq.getTitle())
                .description(reviewReq.getDescription())
                .url(reviewReq.getUrl())
                .build();
    }

    public List<ReviewRes> getReviewsByUser(int userIdx) {
        String getReviewQuery = "select * from REVIEWS where user_id = ?";

        int getReviewParam = userIdx;
        return (List<ReviewRes>) this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> ReviewRes.builder()
                        .reviewId(rs.getInt("id"))
                        .itemId(rs.getInt("item_id"))
                        .orderId(rs.getInt("order_id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .build()
                ,getReviewParam);
    }

    public List<ItemRes> getReviewedItemByUser(int userIdx) {
        String getReviewQuery = "select item_id from REVIEWS where user_id = ?";
        int getReviewParam = userIdx;

        return (List<ItemRes>) this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> ItemRes.builder()
                        .itemId(rs.getInt("item_id"))
                        .build()
                ,getReviewParam);
    }

    public ReviewRes getReviewByUser(int reviewId) {
        String getReviewQuery = "select * from REVIEWS where id = ?";
        Object[] getReviewParam = new Object[]{reviewId};
        return this.jdbcTemplate.queryForObject(getReviewQuery,
                (rs, rowNum) -> ReviewRes.builder()
                        .reviewId(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .itemId(rs.getInt("item_id"))
                        .orderId(rs.getInt("order_id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .build()
                ,reviewId);
    }
}
