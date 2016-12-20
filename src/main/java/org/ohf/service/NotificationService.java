package org.ohf.service;

import org.evey.service.BaseCrudService;
import org.ohf.bean.DTO.NotificationUpdateDTO;
import org.ohf.bean.Notification;

import java.util.List;

/**
 * Created by Laurie on 19 Dec 2016.
 */
public interface NotificationService extends BaseCrudService<Notification> {
    public List<Notification> getNotificationOfUser(Long userId, Long maxCount);
    public void setNotificationSeen(NotificationUpdateDTO notificationUpdateDTO);
}
