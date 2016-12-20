package org.ohf.bean.DTO;

import org.ohf.bean.Notification;

import java.util.List;

/**
 * Created by Laurie on 20 Dec 2016.
 */
public class NotificationUpdateDTO {
    private Long userId;
    private List<Notification> notificationList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
