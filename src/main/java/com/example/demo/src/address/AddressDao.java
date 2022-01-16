package com.example.demo.src.address;

import com.example.demo.src.address.model.AddressReq;
import com.example.demo.src.address.model.AddressRes;
import com.example.demo.src.user.model.PatchUserReq;
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
                        .isSelected(rs.getInt("is_selected"))
                        .isFirst(rs.getInt("is_first"))
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .detailAddressInfo(
                                new AddressRes.DetailAddressInfo(
                                        rs.getString("receive_space"),
                                        rs.getString("receive_space_detail"),
                                        rs.getString("receive_space_enter"),
                                        rs.getString("receive_space_enter_detail"),
                                        rs.getString("deliver_completed_message")
                                )
                        )
                        .build(), userIdxByJwt);
    }

    public int createAddress(AddressReq addressReq, int userIdxByJwt) {
        String createUserQuery = "insert into DELIVER_ADDRESSES (user_id, address, detail_address, name, phone_number, is_selected, is_first) VALUES (?,?,?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{userIdxByJwt, addressReq.getAddress(), addressReq.getDetailAddress(), addressReq.getName(), addressReq.getPhoneNumber() , 1, 1}; //, addressReq.getIsSelected(), addressReq.getIsFirst()
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
                        .isSelected(rs.getInt("is_selected"))
                        .isFirst(rs.getInt("is_first"))
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .build(), getAddressParam);
    }

    public int modifyUserName(AddressReq addressReq, int addressId){
        if (addressReq.getDetailAddressInfo() != null){
            String modifyUserNameQuery = "UPDATE DELIVER_ADDRESSES SET address = IF(? IS NULL, address, ?) , " +
                    "name = IF(? IS NULL, name, ?),phone_number = IF(? IS NULL, phone_number, ?)," +
                    "is_selected = IF(? IS NULL, is_selected, ?),is_first = IF(? IS NULL, is_first, ?)," +
                    "receive_space = IF(? IS NULL, receive_space, ?)," +
                    "receive_space_detail = IF(? IS NULL, receive_space_detail, ?), " +
                "receive_space_enter = IF(? IS NULL, receive_space_enter, ?),receive_space_enter_detail = IF(? IS NULL, receive_space_enter_detail, ?), " +
                "deliver_completed_message = IF(? IS NULL, deliver_completed_message, ?)" +
                    "WHERE id = ?";
            Object[] modifyUserNameParams = new Object[]{addressReq.getAddress(), addressReq.getAddress(),
                    addressReq.getName(), addressReq.getName(),
                    addressReq.getPhoneNumber(), addressReq.getPhoneNumber(),
                    addressReq.getIsSelected(),addressReq.getIsSelected(),
                    addressReq.getIsFirst(),addressReq.getIsFirst(),
                addressReq.getDetailAddressInfo().getReceiveSpace(), addressReq.getDetailAddressInfo().getReceiveSpace(),
                addressReq.getDetailAddressInfo().getReceiveSpaceDetail(), addressReq.getDetailAddressInfo().getReceiveSpaceDetail(),
                addressReq.getDetailAddressInfo().getReceiveSpaceEnter(), addressReq.getDetailAddressInfo().getReceiveSpaceEnterDetail(),
                addressReq.getDetailAddressInfo().getReceiveSpaceEnterDetail(), addressReq.getDetailAddressInfo().getReceiveSpaceEnterDetail(),
                    addressReq.getDetailAddressInfo().getDeliverCompleteMessage(), addressReq.getDetailAddressInfo().getDeliverCompleteMessage(),
                    addressId};

            return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
        }

        String modifyUserNameQuery = "UPDATE DELIVER_ADDRESSES SET address = IF(? IS NULL, address, ?) , " +
                "name = IF(? IS NULL, name, ?),phone_number = IF(? IS NULL, phone_number, ?)," +
                "is_selected = IF(? IS NULL, is_selected, ?),is_first = IF(? IS NULL, is_first, ?)" +
//                ,receive_space_detail = IF(? IS NULL, receive_space_detail, ?)" +
//                "receive_space_enter = IF(? IS NULL, receive_space_enter, ?),receive_space_enter_detail = IF(? IS NULL, receive_space_enter_detail, ?), " +
//                "deliver_completed_message = IF(? IS NULL, deliver_completed_message, ?)" +
                "WHERE id = ?";
        Object[] modifyUserNameParams = new Object[]{addressReq.getAddress(), addressReq.getAddress(),
                addressReq.getName(), addressReq.getName(),
                addressReq.getPhoneNumber(), addressReq.getPhoneNumber(),
                addressReq.getIsSelected(),addressReq.getIsSelected(),
                addressReq.getIsFirst(),addressReq.getIsFirst(),
//                addressReq.getDetailAddressInfo().getReceiveSpace(), addressReq.getDetailAddressInfo().getReceiveSpace(),
//                addressReq.getDetailAddressInfo().getReceiveSpaceDetail(), addressReq.getDetailAddressInfo().getReceiveSpaceDetail(),
//                addressReq.getDetailAddressInfo().getReceiveSpaceEnter(), addressReq.getDetailAddressInfo().getReceiveSpaceEnterDetail(),
//                addressReq.getDetailAddressInfo().getDeliverCompleteMessage(), addressReq.getDetailAddressInfo().getReceiveSpaceEnterDetail(),
//                addressReq.getDetailAddressInfo().getDeliverCompleteMessage(), addressReq.getDetailAddressInfo().getDeliverCompleteMessage(),
                addressId};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);

    }

    public int deleteAddress(int addressIdx) {
        String modifyUserNameQuery = "delete from DELIVER_ADDRESSES where id =?";
        Object[] modifyUserNameParams = new Object[]{addressIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyAddressNotSelectedFirst(int addressIdx) {
        String modifyUserNameQuery = "UPDATE DELIVER_ADDRESSES SET is_selected = 0, is_first = 0 where id=?";

        return this.jdbcTemplate.update(modifyUserNameQuery,addressIdx);


    }

    public int modifyAddressNotSelected(int addressIdx) {
        String modifyUserNameQuery = "UPDATE DELIVER_ADDRESSES SET is_selected = 0 where id=?";

        return this.jdbcTemplate.update(modifyUserNameQuery,addressIdx);
    }

    public int modifyAddressNotFirst(int addressIdx) {
        String modifyUserNameQuery = "UPDATE DELIVER_ADDRESSES SET is_first = 0 where id=?";

        return this.jdbcTemplate.update(modifyUserNameQuery,addressIdx);
    }
}
