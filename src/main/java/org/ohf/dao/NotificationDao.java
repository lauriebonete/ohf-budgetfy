package org.ohf.dao;

import org.evey.dao.BaseEntityDao;
import org.ohf.bean.DTO.NotificationUpdateDTO;
import org.ohf.bean.Notification;
import org.ohf.bean.Voucher;

import java.util.List;

/**
 * Created by Laurie on 19 Dec 2016.
 */
public interface NotificationDao extends BaseEntityDao<Notification, Long> {
    public List<Notification> getNotificationOfUser(Long userId, Long maxCount);
    public void setNotificationSeen(NotificationUpdateDTO notificationUpdateDTO);
}
