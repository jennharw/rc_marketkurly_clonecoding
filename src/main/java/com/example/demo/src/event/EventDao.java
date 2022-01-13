package com.example.demo.src.event;

import com.example.demo.src.event.model.EventReq;
import com.example.demo.src.event.model.EventRes;
import com.example.demo.src.level.model.LevelRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class EventDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createEvent(EventReq eventReq) {
        String createLevelQuery = "insert into EVENTS (description) VALUES (?)";
        Object[] createLevelParams = new Object[]{eventReq.getDescription()};
        this.jdbcTemplate.update(createLevelQuery, createLevelParams);

        String lastInserIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class); //In

    }

    public List<EventRes> getEvent() {
        String getEventQuery = "select * from EVENTS";
        return this.jdbcTemplate.query(getEventQuery,
                (rs, rowNum) -> EventRes.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .build()
                );
    }

    public int participate(int eventId, int userIdxByJwt) {
        String createEventParticipateQuery = "insert into EVENT_PARTICIPANTS (event_id, user_id) VALUES (?,?)";
        Object[] createEventParticipateParams = new Object[]{eventId, userIdxByJwt};
        this.jdbcTemplate.update(createEventParticipateQuery, createEventParticipateParams);

        String lastInsertIdQuery =  "select last_insert_id()"; //postUserReq.getId();

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); //In
    }


}
