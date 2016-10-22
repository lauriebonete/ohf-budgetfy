package org.ohf.controller;

import org.evey.controller.BaseCrudController;
import org.ohf.bean.Particular;
import org.ohf.bean.Voucher;
import org.ohf.service.ParticularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/expense")
public class VoucherController extends BaseCrudController<Voucher> {

    @Autowired
    private ParticularService particularService;

    @Override
    public ModelAndView loadHtml() {
        return new ModelAndView("html/expense.html");
    }

    @Override
    protected void postCreate(Voucher command) {
        for(Particular particular : command.getParticulars()){
            particularService.save(particular);
        }
    }

    @Override
    protected void preCreate(Voucher command) {

        if(command.getParticulars()!=null){
            BigDecimal sum = new BigDecimal(0);
            for(Particular particular: command.getParticulars()){
                sum = sum.add(particular.getExpense());
            }
            command.setTotalExpense(sum);
        }
    }
}
