package com.example.demo.src.level;

import com.example.demo.src.level.model.LevelReq;
import com.example.demo.src.level.model.LevelRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LevelDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int  createLevel(LevelReq levelReq) {
        String createLevelQuery = "insert into LEVELS (points_rate, description, name) VALUES (?,?,?)";
        Object[] createLevelParams = new Object[]{levelReq.getPointsRate(), levelReq.getDescription(),  levelReq.getDescription()};
        this.jdbcTemplate.update(createLevelQuery, createLevelParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //In
    }

    public LevelRes getLevel(int levelId) {
        String getLevelQuery = "select * from LEVELS where id = ?";
        return this.jdbcTemplate.queryForObject(getLevelQuery,
                (rs, rowNum) -> LevelRes.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .pointsRate(rs.getDouble("points_rate"))
                        //.discountRate(rs.getInt("discount_rate"))
                        .build(),
                levelId);
    }

    public List<LevelRes> getLevels() {
        String getLevelQuery = "select * from LEVELS";
        return this.jdbcTemplate.query(getLevelQuery,
                (rs, rowNum) -> LevelRes.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .pointsRate(rs.getDouble("points_rate"))
                        //.discountRate(rs.getInt("discount_rate"))
                        .build());
    }


}
