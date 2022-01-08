package com.example.demo.src.user;


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
                        .name(rs.getString("username"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone_number"))
                        .gender(rs.getString("gender"))
                        .status(GetUserRes.Status.valueOf(rs.getString("status")))
                        .build());
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from USERS where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> GetUserRes.builder()
                                .userId(rs.getInt("user_id"))
                                .name(rs.getString("name"))
                              //  .address(rs.getString("address"))
                                .email(rs.getString("email"))
                                .password(rs.getString("password")).build(),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from USERS where user_id = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> GetUserRes.builder()
                        .userId(rs.getInt("user_id"))
                        //.address(rs.getString("address"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .point(rs.getInt("point"))
                       // .level(rs.getString("level"))
                        .build(),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq){ //ID authincrement
        String createUserQuery = "insert into USERS (username,level_id, password, email, phone_number, birth, gender,status) VALUES (?,1,?,?,?,?,?,'USING')";
        Object[] createUserParams = new Object[]{postUserReq.getUsername(), postUserReq.getPassword(), postUserReq.getEmail(), postUserReq.getPhoneNumber(), postUserReq.getBirth(), postUserReq.getGender()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class); //Integer.parseInt(postUserReq.getId());
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from USERS where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update USERS set name = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select id, username, password,email,name from USERS where username = ?";
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


    public int deleteUser(PatchUserReq patchUserReq) {
        String modifyUserNameQuery = "update USERS set is_deleted = 1 where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserIdx()};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int logoutUser(int userIdx) {
        String logoutUserQuery = "insert into LOGOUT (`user_id`)  values (?)";
        Object[] logoutUserParams = new Object[]{userIdx};
        this.jdbcTemplate.update(logoutUserQuery, logoutUserParams);

        String lastInserIdQuery =  "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkLogout(int userIdx) {
        String checkEmailQuery = "select exists(select * from LOGOUT where user_id = ?)";
        int checkEmailParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkId(int kakaoId) {
        String checkIdQuery = "select exists(select user_id from USERS where user_id = ?)";
        int checkIdParams = kakaoId;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);
    }
}
