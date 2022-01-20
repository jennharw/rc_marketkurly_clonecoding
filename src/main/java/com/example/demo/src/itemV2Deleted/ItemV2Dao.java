package com.example.demo.src.itemV2Deleted;


import com.example.demo.src.itemV2Deleted.model.GetHowRes;
import com.example.demo.src.itemV2Deleted.model.GetItemMoreRes;
import com.example.demo.src.itemV2Deleted.model.GetItemRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ItemV2Dao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    //상품 1개 조회
    public GetItemRes getItem(int id){ //UserProvider.java에서 item_id값을 받아옴.
        String getItemQuery = "select I.id AS id , I.name AS name ,I.price AS price , I.discount_rate AS discount_rate, I.member_discount_price AS member_discount_price,I.created_at AS created_at,I.items_img_url AS items_img_url, A.category AS category,SC.category AS sub_category, I.coupon AS coupon, I.present AS present                            FROM HOME_CATEGORY A LEFT JOIN SUB_CATEGORY SC on A.id = SC.category_id LEFT JOIN ITEMS I on SC.id = I.sub_id where I.id =?";
        int getItemParams = id;
        return this.jdbcTemplate.queryForObject(getItemQuery,
                (rs, rowNum) -> GetItemRes.builder().
                        itemId(rs.getInt("id")).
                        name(rs.getString("name")).
                        price(rs.getInt("price")).
                        discount_rate(rs.getString("discount_rate")).
                        member_discount_price(rs.getInt("member_discount_price")).
                        created_at(rs.getTimestamp("created_at")).
                        items_img_url(rs.getString("items_img_url"))
                        .category(rs.getString("category"))
                        .sub_category(rs.getString("sub_category"))
                                .coupon(rs.getInt("coupon"))
                                        .present(rs.getInt("present")).

                        build(),

                        //rs.getString("name"),
//                        rs.getInt("price"),
//                        rs.getString("discount_rate"),
//                        rs.getInt("member_discount_price"),
//                        rs.getTimestamp("created_at"),
//                        rs.getString("items_img_url"),
//                        rs.getString("category"),
//                        rs.getString("sub_category"),
//                        rs.getInt("coupon"),
//                        rs.getInt("present")
//                ),
                getItemParams);
    }
//
//    /**
//     * 상품설명 조회 API
//     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public List<GetItemMoreRes> getItemsmores(){
//        String getItemsmoresQuery = "select A.items_img_url AS items_img_url, A.name AS name, ID.Product_description AS Product_description\n" +
//                "                ,A.price AS price, A.discount_rate AS discount_rate,A.member_discount_price AS member_discount_price, C.coupon_bool AS coupon\n" +
//                "                ,C.coupon_name AS coupon_name, P.present_bool AS present, A.quantity AS quantity,ID.more_informaion_url AS more_informaion_url,\n" +
//                "                       ID.sales_unit AS sales_unit,ID.weight_capacity AS weight_capacity, ID.shipping_category AS shipping_category,\n" +
//                "                       ID.origin AS origin, ID.packaging_type AS packaging_type, ID.shelf_life AS shelf_life,ID.notification AS notification,\n" +
//                "                ID.item_details_img_url AS item_details_img_url\n" +
//                "                FROM ITEMS A LEFT JOIN ITEM_DETAILS ID on A.id = ID.item_id LEFT JOIN ITEM_COUPONS C on A.id = C.items_id LEFT JOIN ITEM_PRESENTS P on A.id = P.items_id";
//                  //getUsersQuery -> getItemsQuery
//        return this.jdbcTemplate.query(getItemsmoresQuery,
//                (rs,rowNum) -> new GetItemMoreRes(
//                        rs.getString("items_img_url"),
//                        rs.getString("name"),
//                        rs.getString("Product_description"),
//                        rs.getInt("price"),
//                        rs.getString("discount_rate"),
//                        rs.getInt("member_discount_price"),
//                        rs.getInt("coupon"),
//                        rs.getString("coupon_name"),
//                        rs.getInt("present"),
//
//                        rs.getInt("quantity"),
//                        rs.getString("more_informaion_url"),
//
//                        rs.getString("sales_unit"),
//                        rs.getString("weight_capacity"),
//                        rs.getString("shipping_category"),
//                        rs.getString("origin"),
//                        rs.getString("packaging_type"),
//                        rs.getString("shelf_life"),
//                        rs.getString("notification"),
//                        rs.getString("item_details_img_url")
//
//
//                        )
//        );
//    }
//
//
//    //상품설명 1개 조회
//    public GetItemMoreRes getItemMore(int ItemMore){ //UserProvider.java에서 item_id값을 받아옴.
//        String getItemMoreQuery = "select A.items_img_url AS items_img_url, A.name AS name, ID.Product_description AS Product_description, A.price AS price, A.discount_rate AS discount_rate,A.member_discount_price AS member_discount_price, C.coupon_bool AS coupon, C.coupon_name AS coupon_name, P.present_bool AS present, A.quantity AS quantity,ID.sales_unit AS sales_unit,ID.weight_capacity AS weight_capacity, ID.shipping_category AS shipping_category,ID.origin AS origin, ID.packaging_type AS packaging_type, ID.shelf_life AS shelf_life,ID.notification AS notification,ID.item_details_img_url AS item_details_img_url, ID.more_informaion_url AS more_informaion_url FROM ITEMS A LEFT JOIN ITEM_DETAILS ID on A.id = ID.item_id LEFT JOIN ITEM_COUPONS C on A.id = C.items_id LEFT JOIN ITEM_PRESENTS P on A.id = P.items_id where A.id =?"; //쿼리문 실행 DB 내 items_id로 조회
//        int getItemMoreParams = ItemMore;
//        return this.jdbcTemplate.queryForObject(getItemMoreQuery,
//                (rs, rowNum) -> GetItemMoreRes.builder()
//                                .item_details_img_url(rs.getString("items_img_url"))
//                                .member_discount_price(rs.getInt("member_discount_price"))
//                                    .name(rs.getString("name"))
//                                            .Product_description(rs.getString("Product_description"))
//                                                    .price(rs.getInt("price"))
//                                                            .discount_rate(rs.getString("discount_rate"))
//                                                                            .coupon(rs.getInt("coupon"))
//                        .coupon_name(rs.getString("coupon_name"))
//                        .more_informaion_url(rs.getString("more_informaion_url"))
//                        .present(rs.getInt("present"))
//                        .quantity(rs.getInt("quantity"))
//                        .sales_unit(rs.getString("sales_unit"))
//                        .weight_capacity(rs.getString("weight_capacity"))
//                        .shipping_category(rs.getString("shipping_category"))
//                        .origin(rs.getString("origin"))
//                        .packaging_type(rs.getString("packaging_type"))
//                        .shelf_life(rs.getString("shelf_life"))
//                        .notification(rs.getString("notification"))
//                        .item_details_img_url(rs.getString("item_details_img_url"))
//                        .build(),
//
////
//////                        ,
////                        ,
////                        ,
////
////                        ,
////                        ,
////
////                        ,
////                        ,
////                        ,
////                        ,
////                        ,
////                        ,
////                        ,
////
//
//                getItemMoreParams);
//    }
////    /**
////     * 상품설명 1개 조회 API
////     */
////    public GetItemRes getItems(int id){ //UserProvider.java에서 item_id값을 받아옴.
////        String getItemQuery = "select * from ITEMS where id = ?"; //쿼리문 실행 DB 내 items_id로 조회
////        int getItemParams = id;
////        return this.jdbcTemplate.queryForObject(getItemQuery,
////                (rs, rowNum) -> new GetItemRes(
////                        rs.getInt("id"),
////                        rs.getString("name"),
////                        rs.getInt("price"),
////                        rs.getTimestamp("created_at"),
////                        rs.getString("items_img_url"),
////                        rs.getString("discount_rate"),
////                        rs.getInt("member_discount_price")
////                ),
////                getItemParams);
////    }
////
//
////아이템 조회 백업
////    public GetUserRes getUser(int userIdx){
////        String getUserQuery = "select * from Profiles where IDX = ?";
////        int getUserParams = userIdx;
////        return this.jdbcTemplate.queryForObject(getUserQuery,
////                (rs, rowNum) -> new GetUserRes(
////                        rs.getInt("IDX"),
////                        rs.getTimestamp("CREATE_AT"),
////                        rs.getTimestamp("UPDATE_AT"),
////                        rs.getString("userNickName"),
////                        rs.getString("imgUrl"),
////                        rs.getString("mannerTemp"),
////                        rs.getString("retradeRate"),
////                        rs.getString("responeRate"),
////                        rs.getInt("productSaleNum")),
////                getUserParams);
////    }
////아이템 조회 백업
//
//
////


}
