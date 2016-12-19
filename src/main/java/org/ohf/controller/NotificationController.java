package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Notification;
import org.ohf.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        returnMap.put("results", notificationService.getNotificationOfUser(userId,maxCount));
        return returnMap;
    }
}
