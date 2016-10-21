package org.ohf.bean.DTO;

import org.ohf.bean.Activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Laurie on 8/20/2016.
 */
public class ActivityExpenseDTO {

    private Activity activity;
    private BigDecimal expense;
    private String displayExpense;


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

    public String getDisplayExpense() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return "P"+formatter.format(this.expense);
    }
}
