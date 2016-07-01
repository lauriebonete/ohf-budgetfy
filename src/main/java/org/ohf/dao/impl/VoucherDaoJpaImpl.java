package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.Voucher;
import org.ohf.dao.VoucherDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Laurie on 7/2/2016.
 */
@Repository("voucherDao")
public class VoucherDaoJpaImpl extends BaseEntityDaoJpaImpl<Voucher,Long> implements VoucherDao {
}
