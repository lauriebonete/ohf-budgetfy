package org.ohf.dao.impl;

import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Laurie on 8/28/2016.
 */
@Repository("reportDao")
public class ReportDaoJdbcImpl implements ReportDao {

    @Autowired
    private DataSource dataSource;

    private static final StringBuilder GET_VOUCHER = new StringBuilder();

    static {
        GET_VOUCHER.append("SELECT V.ID AS VOUCHER_ID, DATE_FORMAT(VC_DATE,'%m-%d-%Y') AS VC_DATE, ")
                .append("  PAYEE, GROUP_CONCAT(P.DESCRIPTION SEPARATOR '; ') AS PARTICULARS,")
                .append("  V.REFERENCE AS REFERENCE, V.VC_NUMBER AS VC_NUMBER, V.TOTAL_AMOUNT AS TOTAL_AMOUNT ")
                .append("FROM VOUCHER V ")
                .append("LEFT JOIN PARTICULAR P ON P.VOUCHER_ID = V.ID ")
                .append("GROUP BY P.VOUCHER_ID ")
                .append("ORDER BY V.VC_DATE ");

    }

    @Override
    public List<DisbursementDTO> generateDisbursementReport() {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);


        List<DisbursementDTO> disbursementDTOList = template.query(GET_VOUCHER.toString(), new RowMapper<DisbursementDTO>() {
            @Override
            public DisbursementDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                final DisbursementDTO disbursementDTO = new DisbursementDTO();
                disbursementDTO.setRow(i++);
                disbursementDTO.setVoucherId(resultSet.getLong("VOUCHER_ID"));
                disbursementDTO.setVoucherDate(resultSet.getString("VC_DATE"));
                disbursementDTO.setPayee(resultSet.getString("PAYEE"));
                disbursementDTO.setParticulars(resultSet.getString("PARTICULARS"));
                disbursementDTO.setReference(resultSet.getString("REFERENCE"));
                disbursementDTO.setVcNumber(resultSet.getString("VC_NUMBER"));
                disbursementDTO.setTotalAmount(resultSet.getBigDecimal("TOTAL_AMOUNT"));
                return disbursementDTO;
            }
        });

        return disbursementDTOList;
    }
}
