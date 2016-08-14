package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Voucher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/expense")
public class VoucherController extends BaseCrudController<Voucher> {

    @Override
    public ModelAndView loadHtml() {
        return new ModelAndView("html/expense.html");
    }
}
