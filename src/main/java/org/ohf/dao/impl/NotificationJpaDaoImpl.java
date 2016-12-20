package org.ohf.dao.impl;

import org.evey.dao.impl.BaseEntityDaoJpaImpl;
import org.ohf.bean.DTO.NotificationUpdateDTO;
import org.ohf.bean.Notification;
import org.ohf.dao.NotificationDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 19 Dec 2016.
 */
@Repository("notificationDao")
public class NotificationJpaDaoImpl extends BaseEntityDaoJpaImpl<Notification, Long> implements NotificationDao {
    @Override
    public List<Notification> getNotificationOfUser(Long userId, Long maxCount) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT n FROM Notification n WHERE n.notifyUserId = :userId ORDER BY n.id DESC");


        Query query = getEntityManager().createQuery(queryString.toString());
        query.setParameter("userId", userId);
        if(maxCount!=null){
            return query.setMaxResults(maxCount.intValue()).getResultList();
        }
        return query.getResultList();
    }

    @Override
    public void setNotificationSeen(NotificationUpdateDTO notificationUpdateDTO) {
        StringBuilder updateSQL = new StringBuilder();
        updateSQL.append("UPDATE Notification n SET n.isSeen = true WHERE n.notifyUserId = :userId AND n.id IN :notificationList");

        List<Long> notificationIds = new ArrayList<>();
        for(Notification notification: notificationUpdateDTO.getNotificationList()){
            notificationIds.add(notification.getId());
        }

        Query query = getEntityManager().createQuery(updateSQL.toString());
        query.setParameter("userId", notificationUpdateDTO.getUserId());
        query.setParameter("notificationList", notificationIds);

        query.executeUpdate();
    }
}
