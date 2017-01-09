package org.ohf.service.impl;

import org.evey.service.impl.BaseCrudServiceImpl;
import org.ohf.bean.DTO.NotificationUpdateDTO;
import org.ohf.bean.Notification;
import org.ohf.dao.NotificationDao;
import org.ohf.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Laurie on 19 Dec 2016.
 */
@Service("notificationService")
public class NotificationServiceImpl extends BaseCrudServiceImpl<Notification> implements NotificationService {

    @Override
    public List<Notification> getNotificationOfUser(Long userId, Long maxCount) {
        return ((NotificationDao)getDao()).getNotificationOfUser(userId, maxCount);
    }

    @Override
    @Transactional
    public void setNotificationSeen(NotificationUpdateDTO notificationUpdateDTO) {
        ((NotificationDao)getDao()).setNotificationSeen(notificationUpdateDTO);
    }
}