package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Activity;
import org.ohf.bean.DTO.ActivityExpenseDTO;
import org.ohf.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseCrudController<Activity> {


    @RequestMapping(value = "/getActivityExpense", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String,Object> findActivityExpense(Long programId){
        Map<String,Object> returnMap = new HashMap<>();

        List<ActivityExpenseDTO> activityList = ((ActivityService)getService()).findActivityExpense(programId);
        returnMap.put("results",activityList);

        return returnMap;
    }

}
