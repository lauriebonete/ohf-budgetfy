package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.Voucher;
import org.ohf.service.VoucherService;
import org.springframework.stereotype.Service;

/**
 * Created by Laurie on 7/2/2016.
 */
@Service("voucherServiceImpl")
public class VoucherServiceImpl extends BaseCrudServiceImpl<Voucher> implements VoucherService {
}
