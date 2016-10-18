package org.ohf.bean.DTO;

import java.util.List;

/**
 * Created by Laurie on 18 Oct 2016.
 */
public class SheetHelper {
    private String period;
    private List<PeriodHelper> periodHelpers;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<PeriodHelper> getPeriodHelpers() {
        return periodHelpers;
    }

    public void setPeriodHelpers(List<PeriodHelper> periodHelpers) {
        this.periodHelpers = periodHelpers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SheetHelper that = (SheetHelper) o;

        return !(period != null ? !period.equals(that.period) : that.period != null);

    }

    @Override
    public int hashCode() {
        return period != null ? period.hashCode() : 0;
    }
}
