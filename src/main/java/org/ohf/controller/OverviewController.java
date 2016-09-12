package org.ohf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Laurie on 9/12/2016.
 */
@Controller
@RequestMapping("/overview")
public class OverviewController {

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/overview.html");
    }
}
