package org.ohf.dao.impl;

import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.dao.ActivityDaoJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 20 Nov 2016.
 */
@Repository("activityDaoJdbc")
public class ActivityDaoJdbcImpl implements ActivityDaoJdbc {

    @Autowired
    private DataSource dataSource;

    private static StringBuilder GET_PROGRAM_ACTIVITY = new StringBuilder();

    static {
        GET_PROGRAM_ACTIVITY.append("SELECT A.ID AS ID ,ACTIVITY_NAME AS ACTIVITY_NAME, ")
                .append("       IFNULL(Sum(EXPENSE), 0) AS AMOUNT ")
                .append("FROM   ACTIVITY a ")
                .append("       LEFT JOIN PARTICULAR p ")
                .append("              ON a.ID = p.ACTIVITY_ID ")
                .append("WHERE  a.PROGRAM_ID = :program_id ")
                .append("GROUP  BY a.ID; ");

    }

    @Override
    public List<Activity> getActualExpensePerActivity(Long programId) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("program_id", programId);
        List<Activity> results = template.query(GET_PROGRAM_ACTIVITY.toString(), params, new RowMapper<Activity>() {
            @Override
            public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
                Activity activity = new Activity();
                activity.setId(resultSet.getLong("ID"));
                activity.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                activity.setAmount(resultSet.getBigDecimal("AMOUNT"));
                return activity;
            }
        });
        return results;
    }
}
