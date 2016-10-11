package org.ohf.dao.impl;

import org.ohf.bean.DTO.ParticularDTO;
import org.ohf.dao.ParticularDaoJdbc;
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
 * Created by Laurie on 12 Oct 2016.
 */
@Repository("particularDaoJdbc")
public class ParticularDaoJdbcImpl implements ParticularDaoJdbc {

    @Autowired
    private DataSource dataSource;

    private static final String GET_PARTICULARS = "SELECT p.DESCRIPTION   AS DESCRIPTION,\n" +
            "       p_.PROGRAM_NAME AS PROGRAM_NAME,\n" +
            "       a.ACTIVITY_NAME AS ACTIVITY_NAME,\n" +
            "       p.EXPENSE       AS EXPENSE\n" +
            "FROM   PARTICULAR p\n" +
            "       LEFT JOIN ACTIVITY a\n" +
            "              ON p.ACTIVITY_ID = a.ID\n" +
            "       LEFT JOIN PROGRAM p_\n" +
            "              ON a.PROGRAM_ID = p_.ID\n" +
            "WHERE  p.VOUCHER_ID = :voucherId";


    @Override
    public List<ParticularDTO> getParticularsByVoucher(Long voucherId) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        final Map<String, Object> params = new HashMap<>();
        params.put("voucherId", voucherId);
        List<ParticularDTO> results = template.query(GET_PARTICULARS.toString(), params, new RowMapper<ParticularDTO>() {
            @Override
            public ParticularDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                ParticularDTO particularDTO = new ParticularDTO();

                particularDTO.setParticularName(resultSet.getString("DESCRIPTION"));
                particularDTO.setProgramName(resultSet.getString("PROGRAM_NAME"));
                particularDTO.setActivityName(resultSet.getString("ACTIVITY_NAME"));
                particularDTO.setExpense(resultSet.getBigDecimal("EXPENSE"));

                return particularDTO;
            }
        });

        return results;
    }
}
