package org.ohf.bean.DTO;

import java.math.BigDecimal;

/**
 * Created by Laurie on 23 Oct 2016.
 */
public class ProgramActivityDTO {
    private Long programId;
    private String programName;
    private Long activityId;
    private String activityName;
    private String hexColor;
    private BigDecimal actualExpense;
    private BigDecimal expectedExpense;

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getActualExpense() {
        return actualExpense;
    }

    public void setActualExpense(BigDecimal actualExpense) {
        this.actualExpense = actualExpense;
    }

    public BigDecimal getExpectedExpense() {
        return expectedExpense;
    }

    public void setExpectedExpense(BigDecimal expectedExpense) {
        this.expectedExpense = expectedExpense;
    }
}
