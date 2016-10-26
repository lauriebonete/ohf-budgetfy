package org.ohf.dao.impl;

import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.dao.ProgramDaoJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 23 Oct 2016.
 */
@Repository("programDaoJdbc")
public class ProgramDaoJdbcImpl implements ProgramDaoJdbc {

    @Autowired
    private DataSource dataSource;

    private static StringBuilder GET_PROGRAM_ACTIVITY = new StringBuilder();

    static {
        GET_PROGRAM_ACTIVITY.append("SELECT p.ID AS PROGRAM_ID, p.PROGRAM_NAME AS PROGRAM_NAME, a.ID AS ACTIVITY_ID, a.ACTIVITY_NAME AS ACTIVITY_NAME ")
                .append("FROM PROGRAM p ")
                .append("LEFT JOIN ACTIVITY a ON p.ID = a.PROGRAM_ID ");
    }

    @Override
    public List<ProgramActivityDTO> getProgramActivity(Date fromDate, Date toDate) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();

        List<ProgramActivityDTO> results = template.query(GET_PROGRAM_ACTIVITY.toString(), params, new RowMapper<ProgramActivityDTO>() {
            @Override
            public ProgramActivityDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                ProgramActivityDTO programActivityDTO = new ProgramActivityDTO();
                programActivityDTO.setProgramId(resultSet.getLong("PROGRAM_ID"));
                programActivityDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));
                programActivityDTO.setActivityId(resultSet.getLong("ACTIVITY_ID"));
                programActivityDTO.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                return programActivityDTO;
            }
        });
        return results;
    }
}
