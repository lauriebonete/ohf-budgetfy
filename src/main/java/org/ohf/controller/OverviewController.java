package org.ohf.controller;

import org.apache.log4j.Logger;
import org.ohf.bean.Program;
import org.ohf.service.ActivityService;
import org.ohf.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laurie on 9/12/2016.
 */
@Controller
@RequestMapping("/overview")
public class OverviewController {

    private static Logger _log = Logger.getLogger(OverviewController.class);

    @Autowired
    private ProgramService programService;

    @Autowired
    private ActivityService activityService;

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    Map<String,Object> handleException(Exception e){
        Map<String,Object> returnMap = new HashMap<>();

        _log.error("ERROR LOGGER:", e);

        returnMap.put("success", false);
        returnMap.put("message", "Ooopppsie! Something went wrong, but you're still awesome.");
        returnMap.put("error",e.getMessage());
        return returnMap;
    }

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/overview.html");
    }

    @RequestMapping(value = "/getProgramBudgetOverview", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String,Object> getAllProgramsPerYear(String year) throws Exception{
        Map<String, Object> returnMap = new HashMap<>();

        if (!year.isEmpty()){
            Program lookFor = new Program();
            lookFor.setYear(year);
            returnMap.put("success", true);
            returnMap.put("expected", programService.findEntity(lookFor));
            returnMap.put("actual", programService.getActualBudgetPerProgram(year));
        } else {
            returnMap.put("success", false);
            returnMap.put("message", "Please provide a year.");
        }

        return  returnMap;
    }

    @RequestMapping(value = "/getActualActivity", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String,Object> getActualExpensePerActivity(Long programId) throws Exception{
        Map<String, Object> returnMap = new HashMap<>();

        if (programId!=null){
            returnMap.put("success", true);
            returnMap.put("actual", activityService.getActualExpensePerActivity(programId));
        } else {
            returnMap.put("success", false);
            returnMap.put("message", "Please choose a program.");
        }

        return  returnMap;
    }
}
