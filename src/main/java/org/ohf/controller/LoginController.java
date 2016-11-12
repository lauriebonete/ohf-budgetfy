package org.ohf.controller;

import org.evey.bean.User;
import org.evey.bean.UserRole;
import org.evey.service.LoginService;
import org.evey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/login")
public class LoginController implements AuthenticationProvider{

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return loginService.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return loginService.supports(aClass);
    }

    @RequestMapping
    public ModelAndView loadHtml() {
        return new ModelAndView("html/login.html");
    }

    @RequestMapping(value="/access-denied", method = RequestMethod.GET)
    public String accessDenied(){
        return "redirect:/login?login_response=access_denied&error=true";
    }

    @RequestMapping(value = "/get-logged-user", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Map<String, Object> getLoggedUser() throws Exception{
        Map<String,Object> returnMap = new HashMap<>();
        User user = userService.getCurrentUser();
        for(UserRole role : user.getUserRole()){
            //do this to bypass lazy
            role.getRoleName();
        }
        returnMap.put("status", true);
        returnMap.put("user",user);
        return returnMap;
    }
}
