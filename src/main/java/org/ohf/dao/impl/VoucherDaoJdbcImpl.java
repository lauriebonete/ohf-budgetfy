package org.ohf.dao.impl;

import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.dao.VoucherDaoJdbc;
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
 * Created by Laurie on 16 Oct 2016.
 */
@Repository("voucherDaoJdbc")
public class VoucherDaoJdbcImpl implements VoucherDaoJdbc {

    @Autowired
    private DataSource dataSource;

    private static StringBuilder DISBURSEMENT_REPORT_DETAILS = new StringBuilder();

    static {
        DISBURSEMENT_REPORT_DETAILS.append("SELECT v.ID            AS ID, ")
                .append("       v.VC_DATE       AS VC_DATE, ")
                .append("       v.PAYEE         AS PAYEE, ")
                .append("       v.REFERENCE     AS REFERENCE, ")
                .append("       v.VC_NUMBER     AS VC_NUMBER, ")
                .append("       v.TOTAL_EXPENSE AS TOTAL_EXPENSE, ")
                .append("       p.DESCRIPTION   AS PARTICULAR, ")
                .append("       p.EXPENSE       AS EXPENSE, ")
                .append("       a.ID            AS ACTIVITY_ID, ")
                .append("       a.ACTIVITY_NAME AS ACTIVITY_NAME, ")
                .append("       p_.ID           AS PROGRAM_ID, ")
                .append("       p_.PROGRAM_NAME AS PROGRAM_NAME ")
                .append("FROM   VOUCHER v ")
                .append("       LEFT JOIN PARTICULAR p ON v.ID = p.VOUCHER_ID ")
                .append("       LEFT JOIN ACTIVITY a ON p.ACTIVITY_ID = a.ID ")
                .append("       LEFT JOIN PROGRAM p_ ON a.PROGRAM_ID = p_.ID ")
                .append("WHERE  VC_DATE >= :fromDate ")
                .append("       AND VC_DATE <= :toDate ")
                .append("ORDER  BY 1   ");
    }

    @Override
    public List<DisbursementDTO> getDisbursementReportDetails(Date fromDate, Date toDate) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);

        try {
            List<DisbursementDTO> results = template.query(DISBURSEMENT_REPORT_DETAILS.toString(), params, new RowMapper<DisbursementDTO>() {
                @Override
                public DisbursementDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                    DisbursementDTO disbursementDTO = new DisbursementDTO();

                    disbursementDTO.setVoucherId(resultSet.getLong("ID"));
                    disbursementDTO.setVoucherDate(resultSet.getString("VC_DATE"));
                    disbursementDTO.setPayee(resultSet.getString("PAYEE"));
                    disbursementDTO.setReference(resultSet.getString("REFERENCE"));
                    disbursementDTO.setVcNumber(resultSet.getString("VC_NUMBER"));
                    disbursementDTO.setTotalExpense(resultSet.getBigDecimal("TOTAL_EXPENSE"));
                    disbursementDTO.setParticulars(resultSet.getString("PARTICULAR"));
                    disbursementDTO.setExpense(resultSet.getBigDecimal("EXPENSE"));
                    disbursementDTO.setActivityId(resultSet.getLong("ACTIVITY_ID"));
                    disbursementDTO.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                    disbursementDTO.setProgramId(resultSet.getLong("PROGRAM_ID"));
                    disbursementDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));

                    return disbursementDTO;
                }
            });
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
