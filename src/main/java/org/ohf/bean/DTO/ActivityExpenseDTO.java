package org.ohf.bean.DTO;

import org.ohf.bean.Activity;

import java.math.BigDecimal;

/**
 * Created by Laurie on 8/20/2016.
 */
public class ActivityExpenseDTO {

    private Activity activity;
    private BigDecimal expense;

    public ActivityExpenseDTO(Activity activity, BigDecimal expense) {
        this.activity = activity;
        this.expense = expense;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
