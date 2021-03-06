package org.ohf.controller;

import org.evey.bean.ReferenceLookUp;
import org.evey.bean.User;
import org.evey.controller.BaseCrudController;
import org.ohf.bean.Activity;
import org.ohf.bean.Program;
import org.ohf.bean.ProgramAccess;
import org.ohf.bean.UserAccess;
import org.ohf.service.ActivityService;
import org.ohf.service.ProgramAccessService;
import org.ohf.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/program")
public class ProgramController extends BaseCrudController<Program> {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private ProgramAccessService programAccessService;

    @RequestMapping(value = "/getYears", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String, Object> getYears(){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("results", getService().findEntityByNamedQuery("jpql.program.get-years", String.class));
        returnMap.put("status", true);
        return  returnMap;
    }

    @RequestMapping(value = "/create-program/create", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Map<String,Object> createProgram(@RequestBody Program program){
        get_log().info("CREATE_PROGRAM");

        Program saveThisProgram = new Program();
        saveThisProgram.setProgramName(program.getProgramName());
        saveThisProgram.setProgramEnd(program.getProgramEnd());
        saveThisProgram.setProgramStart(program.getProgramStart());
        saveThisProgram.setPercentage(program.getPercentage());
        saveThisProgram.setThreshold(program.getThreshold());
        saveThisProgram.setTotalBudget(program.getTotalBudget());
        saveThisProgram.setHexColor(program.getHexColor());
        getService().save(saveThisProgram);

        for(Activity activity: program.getActivities()){
            Activity saveThisActivity = new Activity();
            saveThisActivity.setActivityName(activity.getActivityName());
            saveThisActivity.setAmount(activity.getAmount());
            saveThisActivity.setProgram(saveThisProgram);
            saveThisActivity.setActivityType(new ReferenceLookUp());
            saveThisActivity.getActivityType().setId(activity.getActivityTypeId());
            saveThisActivity.setActivityCode(new ReferenceLookUp());
            saveThisActivity.getActivityCode().setId(activity.getActivityCodeId());
            saveThisActivity.setActivityCodeName(activity.getActivityCodeName());
            activityService.save(saveThisActivity);
        }

        for(UserAccess userAccess: program.getUserAccessList()){
            UserAccess saveThisAccess = new UserAccess();
            saveThisAccess.setUser(new User());
            saveThisAccess.getUser().setId(userAccess.getUserId());
            saveThisAccess.setProgram(saveThisProgram);
            userAccessService.save(saveThisAccess);
            for(ProgramAccess programAccess: userAccess.getProgramAccessSet()){
                ProgramAccess saveThisProgramAccess = new ProgramAccess();
                saveThisProgramAccess.setAccess(programAccess.getAccess());
                saveThisProgramAccess.setUserAccess(saveThisAccess);
                programAccessService.save(saveThisProgramAccess);
            }
        }
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("success",true);
        returnMap.put("program",saveThisProgram);
        return returnMap;
    }

    @RequestMapping(value = "/create-program")
    public ModelAndView loadCreateProgramPage() {
        return new ModelAndView("/html/create-program.html");
    }
}
