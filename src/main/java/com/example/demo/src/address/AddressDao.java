package com.example.demo.src.address;

import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.address.model.AddressRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AddressRes> getAddress(int userIdxByJwt) {
        String getUsersQuery = "select * from DELIVER_ADDRESSES where user_id = ?";

        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> AddressRes.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .address(rs.getString("address"))
                        .detail_address(rs.getString("detail_address"))
                        .build(), userIdxByJwt);
    }

    public int createAddress(AddressReq addressReq, int userIdxByJwt) {
        String createUserQuery = "insert into DELIVER_ADDRESSES (user_id, address, detail_address) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{userIdxByJwt, addressReq.getAddress(), addressReq.getDetailAddress()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class); //Integer.parseI
    }

    public List<AddressRes> chooseAddress(int addressReq , int userIdxByJwt) {
        String getAddressQuery = "select * from DELIVER_ADDRESSES where user_id = ? and id = ?";
        Object[] getAddressParam = new Object[]{userIdxByJwt,addressReq};

        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> AddressRes.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .address(rs.getString("address"))
                        .detail_address(rs.getString("detail_address"))
                        .build(), getAddressParam);
    }
}
