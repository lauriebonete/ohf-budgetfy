package org.ohf.dao;

import org.ohf.bean.DTO.DisbursementDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 16 Oct 2016.
 */
public interface VoucherDaoJdbc {
    public List<DisbursementDTO> getDisbursementReportDetails(Date fromDate, Date toDate);
}
