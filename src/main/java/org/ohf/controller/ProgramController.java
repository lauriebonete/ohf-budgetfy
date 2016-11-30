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
import org.ohf.service.ProgramService;
import org.ohf.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/check-delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public final @ResponseBody Map<String, Object> deleteProgram(@PathVariable("id") final Long programId) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();

        if(activityService.validateIfCanDeleteProgram(programId)) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    getService().delete(programId);
                }
            });

            returnMap.put("status", true);
            returnMap.put("message", "That thing is now gone for good.");
        } else {
            returnMap.put("status", false);
            returnMap.put("message", "Before deleting this program, remove the Activities found in the Budget section first.");
        }
        return returnMap;
    }

    @RequestMapping(value = "/check-duplicate", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String, Object> checkIfDuplicate(String programName) throws Exception{
        Map<String, Object> returnMap = new HashMap<>();

        Program lookFor = new Program();
        lookFor.setProgramName(programName);

        List<Program> programList = getService().findEntity(lookFor);
        if(!programList.isEmpty()){
            returnMap.put("status", false);
            returnMap.put("message", "A program with program name "+programName+" already exists. Please choose a different name.");
        } else {
            returnMap.put("status", true);
        }
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
