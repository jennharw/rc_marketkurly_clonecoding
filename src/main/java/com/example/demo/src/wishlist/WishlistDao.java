package com.example.demo.src.wishlist;

import com.example.demo.src.event.model.EventRes;
import com.example.demo.src.wishlist.model.WishReq;
import com.example.demo.src.wishlist.model.WishRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class WishlistDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createWishlist(int userIdxByJwt, WishReq wishReq) {
        String createWishlistQuery = "insert into WISHLISTS (item_id, user_id) VALUES (?, ?)";

        Object[] createUserParams = new Object[]{wishReq.getItemId(), userIdxByJwt};
        this.jdbcTemplate.update(createWishlistQuery, createUserParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //In

    }

    public List<WishRes> getWishlist(int userIdxByJwt) {
        String getWishListQuery = "select * from WISHLISTS WHERE user_id = ?";
        int getWishListParam = userIdxByJwt;

        return (List<WishRes>) this.jdbcTemplate.query(getWishListQuery,
                (rs, rowNum) -> WishRes.builder()
                        .itemId(rs.getInt("item_id"))
                        .userId(rs.getInt("user_id"))

                        .build(),getWishListParam
        );
    }

    public int deleteWishlist(int userIdxByJwt, WishReq wishReq) {
        String getWishListQuery = "delete from WISHLISTS WHERE user_id = ? AND item_id = ?";
        Object[] createUserParams = new Object[]{userIdxByJwt, wishReq.getItemId()};

        return this.jdbcTemplate.update(getWishListQuery,
                createUserParams
        );
    }
}
