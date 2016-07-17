package org.ohf.controller;

import org.evey.bean.ReferenceLookUp;
import org.evey.controller.BaseCrudController;
import org.ohf.bean.Activity;
import org.ohf.bean.Program;
import org.ohf.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/program")
public class ProgramController extends BaseCrudController<Program> {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/create-program/create", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody void createProgram(@RequestBody Program program){
        get_log().info("CREATE_PROGRAM");

        Program saveThisProgram = new Program();
        saveThisProgram.setProgramName(program.getProgramName());
        saveThisProgram.setProgramEnd(program.getProgramEnd());
        saveThisProgram.setProgramStart(program.getProgramStart());
        saveThisProgram.setPercentage(program.getPercentage());
        saveThisProgram.setThreshold(program.getThreshold());
        saveThisProgram.setTotalBudget(program.getTotalBudget());
        getService().save(saveThisProgram);

        for(Activity activity: program.getActivities()){
            Activity saveThisActivity = new Activity();
            saveThisActivity.setActivityName(activity.getActivityName());
            saveThisActivity.setAmount(activity.getAmount());
            saveThisActivity.setProgram(saveThisProgram);
            saveThisActivity.setActivityType(new ReferenceLookUp());
            saveThisActivity.getActivityType().setId(activity.getActivityTypeId());
            activityService.save(saveThisActivity);
        }
    }

    @RequestMapping(value = "/create-program")
    public ModelAndView loadCreateProgramPage() {
        return new ModelAndView("/html/create-program.html");
    }
}
