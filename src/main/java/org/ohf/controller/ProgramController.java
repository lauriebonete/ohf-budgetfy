package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Program;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/program")
public class ProgramController extends BaseCrudController<Program> {


    @RequestMapping(value = "/create-program", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody void createProgram(@RequestBody Program program){
        get_log().info("CREATE_PROGRAM");
    }
}
