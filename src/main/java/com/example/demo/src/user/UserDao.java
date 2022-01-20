package com.example.demo.src.user;


import com.example.demo.src.order.model.OrderReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.src.user.model.GetUserRes.Status.USING;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes>  getUsers(){
        String getUsersQuery = "select * from USERS where status = 'USING'";
//        List<UserTest> asdawesr = this.jdbcTemplate.query(getUsersQuery,
//                (rs, rowNum) -> new UserTest(rs.getString("name")));

        return (List<GetUserRes>) this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .gender(rs.getString("gender"))
                        .birthDay(rs.getTimestamp("birth"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))
                        .build());
    }

    public GetUserRes getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from USERS where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .birthDay(rs.getTimestamp("birth"))
                        .phoneNumber(rs.getString("phone_number"))
                        .gender(rs.getString("gender"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))

                        .build(),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from USERS where id = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .birthDay(rs.getTimestamp("birth"))
                        .gender(rs.getString("gender"))
                        .level(rs.getInt("level_id"))
                        .point(rs.getInt("points"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))

                        .build(),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq){ //ID authincrement
        String createUserQuery = "insert into USERS (username,name,level_id,points, password, email, phone_number, birth, gender,status, created_at) VALUES (?,?,1,0,?,?,?,?,?,'USING', ?)";
        Object[] createUserParams = new Object[]{postUserReq.getUsername(), postUserReq.getName(),postUserReq.getPassword(), postUserReq.getEmail(), postUserReq.getPhoneNumber(), postUserReq.getBirth(), postUserReq.getGender(), postUserReq.getCreatedAt()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //Integer.parseInt(postUserReq.getId());
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from USERS where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq, int userIdxByJwt){
        //name, password, email, phonenumber
        String modifyUserNameQuery = "UPDATE USERS SET name = IF(? IS NULL, name, ?) , email = IF(? IS NULL, email, ?),phone_number = IF(? IS NULL, phone_number, ?),password = IF(? IS NULL, password, ?) WHERE id = ?";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNewName(), patchUserReq.getNewName(),
                patchUserReq.getNewEmail(), patchUserReq.getNewEmail(),
                patchUserReq.getNewPhoneNumber(), patchUserReq.getNewPhoneNumber(),
                patchUserReq.getNewPassword(),patchUserReq.getPassword(),
                userIdxByJwt};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);

    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select id, username, password,email from USERS where username = ?";
        String getPwdParams = postLoginReq.getUsername();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }




    public int deleteUser(int patchUserReq) {
        String modifyUserNameQuery = "update USERS set status = 'DELETED' where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int logoutUser(int userIdx) {
        String logoutUserQuery = "insert into LOGOUT (`id`)  values (?)";
        Object[] logoutUserParams = new Object[]{userIdx};
        this.jdbcTemplate.update(logoutUserQuery, logoutUserParams);

        String lastInserIdQuery =  "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    public boolean checkPassword(int jwtTokenIdx, String password) {
        String checkIdQuery = "select password where id = ?";
        int checkIdParams = jwtTokenIdx;
        String checkPassword =  this.jdbcTemplate.queryForObject(checkIdQuery,
                String.class,
                checkIdParams);
        return checkPassword == password;
    }

    public GetUserRes getUsersByEmailAndUsername(String email, String username) {
        String getUsersByEmailQuery = "select * from USERS where email =? and username =?";
        Object[] getUsersByEmailAndUserParams = new Object[]{email, username};

        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .birthDay(rs.getTimestamp("birth"))
                        .gender(rs.getString("gender"))
                        .level(rs.getInt("level_id"))
                        .point(rs.getInt("points"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))

                        .build(),
                getUsersByEmailAndUserParams);
    }

    public int checkUsername(String username) {
        String checkEmailQuery = "select exists(select username from USERS where username = ?)";
        String checkEmailParams = username;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public User getPwdById(PatchUserReq patchUserReq) {
        String getPwdQuery = "select id, username, password,email from USERS where id = ?";
        int getPwdParams = patchUserReq.getUserIdx();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
        );
    }

    public int changePoints(int points, int userIdx) {
        String modifyUserNameQuery = "update USERS set points = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{points, userIdx};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneNumberQuery = "select exists(select phone_number from USERS where phone_number = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);
    }

    public GetUserRes getUsersByPhoneNumber(String phoneNumber) {
        String getUsersByEmailQuery = "select * from USERS where phone_number =?";
        String getUsersByEmailParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .birthDay(rs.getTimestamp("birth"))
                        .phoneNumber(rs.getString("phone_number"))
                        .gender(rs.getString("gender"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))

                        .build(),
                getUsersByEmailParams);
    }

    public int createNoUser(OrderReq.InfoNouser infoNouser, int userIdx) {
        String createUserQuery = "insert into NOUSERS (nouser_no,name, email, phone_number) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{userIdx, infoNouser.getName(),infoNouser.getEmail(),infoNouser.getPhoneNumber()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //Integer.parseInt(postUserReq.getId());

    }

//    public GetUserRes getPasswordByPhoneNumberAndUsername(String phoneNumber, String username) {
//    }
}
