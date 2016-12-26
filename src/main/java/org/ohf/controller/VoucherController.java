package org.ohf.controller;

import org.evey.bean.ReferenceLookUp;
import org.evey.bean.User;
import org.evey.controller.BaseCrudController;
import org.evey.service.ReferenceLookUpService;
import org.evey.service.UserService;
import org.ohf.bean.Notification;
import org.ohf.bean.Particular;
import org.ohf.bean.Voucher;
import org.ohf.service.NotificationService;
import org.ohf.service.ParticularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Laurie on 7/2/2016.
 */
@Controller
@RequestMapping("/expense")
public class VoucherController extends BaseCrudController<Voucher> {

    @Autowired
    private ParticularService particularService;

    @Autowired
    private ReferenceLookUpService referenceLookUpService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public ModelAndView loadHtml() {
        return new ModelAndView("html/expense.html");
    }

    @Override
    protected void postCreate(Voucher command) {
        for(Particular particular : command.getParticulars()){
            particularService.save(particular);
        }


        /*ReferenceLookUp referenceLookUp = referenceLookUpService.getReferenceLookUpByKey("OPEN_VOUCHER");

        final long MSPERDAY = 60 * 60 * 24 * 1000;
        final Calendar dateStartCal = Calendar.getInstance();
        dateStartCal.setTime(command.getDate());
        dateStartCal.set(Calendar.HOUR_OF_DAY, 0); // Crucial.
        dateStartCal.set(Calendar.MINUTE, 0);
        dateStartCal.set(Calendar.SECOND, 0);
        dateStartCal.set(Calendar.MILLISECOND, 0);
        final Calendar dateEndCal = Calendar.getInstance();
        dateEndCal.setTime(new Date());
        dateEndCal.set(Calendar.HOUR_OF_DAY, 0); // Crucial.
        dateEndCal.set(Calendar.MINUTE, 0);
        dateEndCal.set(Calendar.SECOND, 0);
        dateEndCal.set(Calendar.MILLISECOND, 0);
        final long dateDifferenceInDays = ( dateEndCal.getTimeInMillis()  - dateStartCal.getTimeInMillis()) / MSPERDAY;

        if(dateDifferenceInDays >= referenceLookUp.getNumberValue()){
            List<User> userList = userService.findAllActive();

            for(User user: userList){
                Notification notification = new Notification();
                notification.setMessage("Voucher " +command.getVcNumber()+" is still open.");
                notification.setClassType(command.getClass().toString());
                notification.setClassId(command.getId());
                notification.setNotifyUserId(user.getId());
                notificationService.save(notification);
            }

            command.setIsNotified(true);
            getService().save(command);
        }*/
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

    @RequestMapping(value = "/get-open-activity", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> getOpenActivity(){
        Map<String,Object> returnMap = new HashMap<>();

        Map<String, Object> parameters = new HashMap<>();
        ReferenceLookUp openVoucher = referenceLookUpService.getReferenceLookUpByKey("OPEN_VOUCHER");

        if(openVoucher==null){
            parameters.put("date", 10);
        } else {
            parameters.put("date", openVoucher.getNumberValue());
        }

        returnMap.put("results", getService().findEntityByNamedQuery("jpq.voucher.get-open-activity", parameters));
        returnMap.put("status", true);
        return returnMap;
    }
}
