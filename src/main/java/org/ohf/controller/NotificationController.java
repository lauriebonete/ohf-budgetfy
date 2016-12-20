package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.DTO.NotificationUpdateDTO;
import org.ohf.bean.Notification;
import org.ohf.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 19 Dec 2016.
 */
@Controller
@RequestMapping("/notification")
public class NotificationController extends BaseCrudController<Notification> {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/get-notification", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String,Object> getNotificationOfUser(Long userId, Long maxCount){

        List<Notification> notificationList =  notificationService.getNotificationOfUser(userId,maxCount);

        List<Notification> notSeen = new ArrayList<>();
        for(Notification notification: notificationList){
            if(!notification.getIsSeen()){
                notSeen.add(notification);
            }
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        returnMap.put("results", notificationList);
        returnMap.put("notSeen", notSeen.size());
        returnMap.put("notSeenNotification", notSeen);
        return returnMap;
    }

    @RequestMapping(value = "/read-notification", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Map<String,Object> readNotification(@RequestBody NotificationUpdateDTO updateDTO){
        Map<String, Object> returnMap = new HashMap<>();
        if(!updateDTO.getNotificationList().isEmpty()){
            notificationService.setNotificationSeen(updateDTO);

            returnMap.put("status", true);
            returnMap.put("message", "Notification marked as read.");
        }

        return returnMap;
    }
}
