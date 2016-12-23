package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Voucher;
import org.ohf.dao.VoucherDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("voucherDao")
public class VoucherDaoJpaImpl extends BaseEntityDaoJpaImpl<Voucher,Long> implements VoucherDao {
    @Override
    public List<Voucher> sendNotificationForOpenVoucher(Integer days) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("date", days);
        return findEntityByNamedQuery("jpq.voucher.get-open-activity-schedule", parameter);
    }


}
