package org.ohf.dao;

import org.evey.dao.BaseEntityDao;
import org.ohf.bean.DTO.DisbursementDTO;
import org.ohf.bean.Voucher;

import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
public interface VoucherDao extends BaseEntityDao<Voucher,Long> {
    public List<Voucher> sendNotificationForOpenVoucher(Integer days);
}
