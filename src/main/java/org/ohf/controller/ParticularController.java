package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Particular;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/particular")
public class ParticularController extends BaseCrudController<Particular> {
}
