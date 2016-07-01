package org.evey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Laurie on 3/6/2016.
 */
@Controller
@RequestMapping("/user-profile")
public class UserProfileController {

    @RequestMapping
    public ModelAndView loadHomepage(){
        return new ModelAndView("html/user-profile.html");
    }

}
