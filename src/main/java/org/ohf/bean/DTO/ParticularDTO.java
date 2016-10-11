package org.ohf.bean.DTO;

import java.math.BigDecimal;

/**
 * Created by Laurie on 12 Oct 2016.
 */
public class ParticularDTO {
    private String particularName;
    private String programName;
    private String activityName;
    private BigDecimal expense;

    public String getParticularName() {
        return particularName;
    }

    public void setParticularName(String particularName) {
        this.particularName = particularName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
