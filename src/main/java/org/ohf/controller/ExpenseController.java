package org.ohf.controller;

import org.evey.bean.ReferenceLookUp;
import org.evey.bean.User;
import org.evey.controller.BaseCrudController;
import org.ohf.bean.*;
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
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/expense")
public class ExpenseController extends BaseCrudController<Expense> {

}
