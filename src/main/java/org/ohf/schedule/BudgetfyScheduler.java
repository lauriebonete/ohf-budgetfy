package org.ohf.schedule;

import org.evey.bean.ReferenceLookUp;
import org.evey.bean.User;
import org.evey.service.ReferenceLookUpService;
import org.evey.service.UserService;
import org.ohf.bean.Notification;
import org.ohf.bean.Voucher;
import org.ohf.service.NotificationService;
import org.ohf.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Laurie on 19 Dec 2016.
 */
@Component
public class BudgetfyScheduler {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ReferenceLookUpService referenceLookUpService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "${open.voucher}")
    @Transactional
    public void sendNotificationsForOpenVoucher(){

        ReferenceLookUp referenceLookUp = referenceLookUpService.getReferenceLookUpByKey("OPEN_VOUCHER");

        List<Voucher> voucherList = voucherService.sendNotificationForOpenVoucher(referenceLookUp.getNumberValue());
        List<User> userList = userService.findAllActive();


        for(Voucher voucher: voucherList){
            for(User user: userList){
                Notification notification = new Notification();
                notification.setMessage("Voucher " +voucher.getVcNumber()+" is still open.");
                notification.setClassType(voucher.getClass().toString());
                notification.setClassId(voucher.getId());
                notification.setNotifyUserId(user.getId());
                notificationService.save(notification);
            }
            voucher.setIsNotified(true);
            voucherService.save(voucher);
        }

    }
}
