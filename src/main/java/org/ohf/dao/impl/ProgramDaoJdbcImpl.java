package org.ohf.dao.impl;

import org.ohf.bean.DTO.ProgramActivityDTO;
import org.ohf.bean.DTO.TotalProgramDTO;
import org.ohf.bean.Program;
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
    private static StringBuilder GET_TOTAL_PER_PROGRAM = new StringBuilder();
    private static StringBuilder GET_ACTUAL_BUDGET = new StringBuilder();
    private static StringBuilder GET_ACTUAL_EXPENSE_PROGRAM = new StringBuilder();
    private static StringBuilder GET_ACTUAL_EXPENSE_ACTIVITY = new StringBuilder();

    static {
        GET_PROGRAM_ACTIVITY.append("SELECT p.ID AS PROGRAM_ID, p.PROGRAM_NAME AS PROGRAM_NAME, a.ID AS ACTIVITY_ID, a.ACTIVITY_NAME AS ACTIVITY_NAME, p.HEX_COLOR AS HEX_COLOR ")
                .append("FROM PROGRAM p ")
                .append("LEFT JOIN ACTIVITY a ON p.ID = a.PROGRAM_ID ")
                .append("WHERE p.PROGRAM_START <= :from_date and p.PROGRAM_START <= :to_date");

        GET_TOTAL_PROGRAM.append("SELECT Sum(p.EXPENSE) AS EXPENSE, ")
                .append("       p_.ID           AS PROGRAM_ID, ")
                .append("       p_.PROGRAM_NAME AS PROGRAM_NAME, ")
                .append("       p_.HEX_COLOR AS HEX_COLOR, ")
                .append("       Month(v.VC_DATE) AS VC_DATE ")
                .append("FROM   VOUCHER v ")
                .append("       JOIN PARTICULAR p ON v.ID = p.VOUCHER_ID ")
                .append("       JOIN ACTIVITY a ON p.ACTIVITY_ID = a.ID ")
                .append("       JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID ")
                .append("WHERE  v.VOUCHER_YEAR = :year ")
                .append("GROUP  BY p_.ID, Month(v.VC_DATE) ");

        GET_TOTAL_PER_PROGRAM.append("SELECT Sum(p.EXPENSE)   AS EXPENSE, ")
                .append("       a.ID             AS ACTIVITY_ID, ")
                .append("       Month(v.VC_DATE) AS VC_DATE ")
                .append("FROM   VOUCHER v ")
                .append("       JOIN PARTICULAR p ON v.ID = p.VOUCHER_ID ")
                .append("       JOIN ACTIVITY a ON p.ACTIVITY_ID = a.ID ")
                .append("       JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID ")
                .append("WHERE  p_.ID = :programId ")
                .append("       AND v.VOUCHER_YEAR = :year ")
                .append("GROUP  BY a.ID, ")
                .append("          Month(v.VC_DATE)  ");

        GET_ACTUAL_BUDGET.append("SELECT p.PROGRAM_NAME AS PROGRAM_NAME, p.HEX_COLOR AS HEX_COLOR, ")
                .append("       Sum(AMOUNT)    AS BUDGET ")
                .append("FROM   ACTIVITY a ")
                .append("       LEFT JOIN PROGRAM p ")
                .append("              ON a.PROGRAM_ID = p.ID ")
                .append("WHERE  p.YEAR = :year ")
                .append("GROUP  BY p.ID ");


        GET_ACTUAL_EXPENSE_PROGRAM.append("SELECT Sum(p.EXPENSE) AS EXPENSE,  ")
                .append("p_.total_budget AS TOTAL_BUDGET, ")
                .append("p_.ID           AS PROGRAM_ID, ")
                .append("p_.PROGRAM_NAME AS PROGRAM_NAME ")
                .append("FROM   VOUCHER v ")
                .append("left JOIN PARTICULAR p ON v.ID = p.VOUCHER_ID ")
                .append("left JOIN ACTIVITY a ON p.ACTIVITY_ID = a.ID ")
                .append("left JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID  ")
                .append("WHERE p_.ID = :programId");

        GET_ACTUAL_EXPENSE_ACTIVITY.append("SELECT IFNULL(Sum(p.EXPENSE),0)   AS ACTUAL_EXPENSE, ")
                .append("       a.ID             AS ACTIVITY_ID,")
                .append("       a.ACTIVITY_NAME AS ACTIVITY_NAME,")
                .append("       a.amount AS EXPECTED_AMOUNT ")
                .append("FROM   ACTIVITY a ")
                .append("       LEFT JOIN PARTICULAR p ON p.ACTIVITY_ID = a.ID ")
                .append("       LEFT JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID ")
                .append("WHERE  p_.ID = :programId ")
                .append("GROUP  BY a.ID ");
    }

    @Override
    public List<ProgramActivityDTO> getProgramActivity(Date fromDate, Date toDate) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        Map<String, Object> params = new HashMap<>();
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        List<ProgramActivityDTO> results = template.query(GET_PROGRAM_ACTIVITY.toString(), params, new RowMapper<ProgramActivityDTO>() {
            @Override
            public ProgramActivityDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                ProgramActivityDTO programActivityDTO = new ProgramActivityDTO();
                programActivityDTO.setProgramId(resultSet.getLong("PROGRAM_ID"));
                programActivityDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));
                programActivityDTO.setActivityId(resultSet.getLong("ACTIVITY_ID"));
                programActivityDTO.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                programActivityDTO.setHexColor(resultSet.getString("HEX_COLOR"));
                return programActivityDTO;
            }
        });
        return results;
    }

    @Override
    public List<TotalProgramDTO> getTotalProgram(String year) {
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
                totalProgramDTO.setHexColor(resultSet.getString("HEX_COLOR"));
                return totalProgramDTO;
            }
        });
        return results;
    }

    @Override
    public List<TotalProgramDTO> getTotalPerProgram(String year, Long programId) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("programId", programId);
        List<TotalProgramDTO> results = template.query(GET_TOTAL_PER_PROGRAM.toString(), params, new RowMapper<TotalProgramDTO>() {
            @Override
            public TotalProgramDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                TotalProgramDTO totalProgramDTO = new TotalProgramDTO();
                totalProgramDTO.setActivityId(resultSet.getLong("ACTIVITY_ID"));
                totalProgramDTO.setExpense(resultSet.getBigDecimal("EXPENSE"));
                totalProgramDTO.setMonth(resultSet.getInt("VC_DATE"));
                return totalProgramDTO;
            }
        });
        return results;
    }

    @Override
    public List<Program> getActualBudgetPerProgram(String year) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        List<Program> results = template.query(GET_ACTUAL_BUDGET.toString(), params, new RowMapper<Program>() {
            @Override
            public Program mapRow(ResultSet resultSet, int i) throws SQLException {
                Program program = new Program();
                program.setProgramName(resultSet.getString("PROGRAM_NAME"));
                program.setTotalBudget(resultSet.getBigDecimal("BUDGET"));
                program.setHexColor(resultSet.getString("HEX_COLOR"));
                return program;
            }
        });
        return results;
    }

    @Override
    public List<TotalProgramDTO> getExpectedActualProgram(Long programId) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("programId", programId);

        List<TotalProgramDTO> results = template.query(GET_ACTUAL_EXPENSE_PROGRAM.toString(), params, new RowMapper<TotalProgramDTO>() {
            @Override
            public TotalProgramDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                TotalProgramDTO totalProgramDTO = new TotalProgramDTO();
                totalProgramDTO.setProgramId(resultSet.getLong("PROGRAM_ID"));
                totalProgramDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));
                totalProgramDTO.setTotalBudget(resultSet.getBigDecimal("TOTAL_BUDGET"));
                totalProgramDTO.setExpense(resultSet.getBigDecimal("EXPENSE"));
                return totalProgramDTO;
            }
        });
        return results;
    }

    @Override
    public List<ProgramActivityDTO> getExpectedActualActivity(Long programId) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("programId", programId);

        List<ProgramActivityDTO> results = template.query(GET_ACTUAL_EXPENSE_ACTIVITY.toString(), params, new RowMapper<ProgramActivityDTO>() {
            @Override
            public ProgramActivityDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                ProgramActivityDTO programActivityDTO = new ProgramActivityDTO();
                programActivityDTO.setActivityId(resultSet.getLong("ACTIVITY_ID"));
                programActivityDTO.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                programActivityDTO.setActualExpense(resultSet.getBigDecimal("ACTUAL_EXPENSE"));
                programActivityDTO.setExpectedExpense(resultSet.getBigDecimal("EXPECTED_AMOUNT"));
                return programActivityDTO;
            }
        });
        return results;
    }
}
