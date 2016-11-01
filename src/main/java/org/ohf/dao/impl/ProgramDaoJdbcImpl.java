package org.ohf.dao.impl;

import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.DTO.TotalProgramDTO;
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
    private static StringBuilder GET_TOTAL_PROGRAM = new StringBuilder();

    static {
        GET_PROGRAM_ACTIVITY.append("SELECT p.ID AS PROGRAM_ID, p.PROGRAM_NAME AS PROGRAM_NAME, a.ID AS ACTIVITY_ID, a.ACTIVITY_NAME AS ACTIVITY_NAME ")
                .append("FROM PROGRAM p ")
                .append("LEFT JOIN ACTIVITY a ON p.ID = a.PROGRAM_ID ");

        GET_TOTAL_PROGRAM.append("SELECT Sum(p.EXPENSE) AS EXPENSE, ")
                .append("       p_.ID           AS PROGRAM_ID, ")
                .append("       p_.PROGRAM_NAME AS PROGRAM_NAME, ")
                .append("       Month(v.VC_DATE) AS VC_DATE ")
                .append("FROM   VOUCHER v ")
                .append("       JOIN PARTICULAR p ON v.ID = p.VOUCHER_ID ")
                .append("       JOIN ACTIVITY a ON p.ACTIVITY_ID = a.ID ")
                .append("       JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID ")
                .append("WHERE  v.VOUCHER_YEAR = :year ")
                .append("GROUP  BY p_.ID, Month(v.VC_DATE) ");
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

    @Override
    public List<TotalProgramDTO> getTotalProgram(String year) {
        try {
            final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
            final Map<String, Object> params = new HashMap<>();
            params.put("year", year);
            List<TotalProgramDTO> results = template.query(GET_TOTAL_PROGRAM.toString(), params, new RowMapper<TotalProgramDTO>() {
                @Override
                public TotalProgramDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                    TotalProgramDTO totalProgramDTO = new TotalProgramDTO();
                    totalProgramDTO.setProgramId(resultSet.getLong("PROGRAM_ID"));
                    totalProgramDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));
                    totalProgramDTO.setExpense(resultSet.getBigDecimal("EXPENSE"));
                    totalProgramDTO.setMonth(resultSet.getInt("VC_DATE"));
                    return totalProgramDTO;
                }
            });
            return results;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
