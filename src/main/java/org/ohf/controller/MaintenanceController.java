package org.ohf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Laurie on 9/17/2016.
 */
@Controller
@RequestMapping("/system-maintenance")
public class MaintenanceController {

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/system-maintenance.html");
    }
}
