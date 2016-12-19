package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.Voucher;
import org.ohf.dao.VoucherDao;
import org.ohf.dao.VoucherDaoJdbc;
import org.ohf.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("voucherService")
public class VoucherServiceImpl extends BaseCrudServiceImpl<Voucher> implements VoucherService {

    @Autowired
    private VoucherDaoJdbc voucherDaoJdbc;

    @Override
    public List<DisbursementDTO> getDisbursementReportDetails(Date fromDate, Date toDate) {
        return voucherDaoJdbc.getDisbursementReportDetails(fromDate, toDate);
    }

    @Override
    public List<Voucher> sendNotificationForOpenVoucher(Integer days) {
        return ((VoucherDao) getDao()).sendNotificationForOpenVoucher(days);
    }
}
