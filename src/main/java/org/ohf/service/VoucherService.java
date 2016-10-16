package org.ohf.service;

import org.evey.service.BaseCrudService;
import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.Voucher;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface VoucherService extends BaseCrudService<Voucher> {
    public List<DisbursementDTO> getDisbursementReportDetails(Date fromDate, Date toDate);
}
