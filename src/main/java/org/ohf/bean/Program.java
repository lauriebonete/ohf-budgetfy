package org.ohf.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.bean.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurie on 7/2/2016.
 */
@Entity
@Table(name = "PROGRAM")
public class Program extends BaseEntity {

    @Column(name = "PROGRAM_NAME")
    private String programName;

    @Column(name = "TOTAL_BUDGET")
    private BigDecimal totalBudget;

    @Column(name = "THRESHOLD")
    private BigDecimal threshold;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROGRAM_START")
    private Date programStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROGRAM_END")
    private Date programEnd;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<Activity> activities;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<UserAccess> userAccessList;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public Date getProgramStart() {
        return programStart;
    }

    public void setProgramStart(Date programStart) {
        this.programStart = programStart;
    }

    public Date getProgramEnd() {
        return programEnd;
    }

    public void setProgramEnd(Date programEnd) {
        this.programEnd = programEnd;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<UserAccess> getUserAccessList() {
        return userAccessList;
    }

    public void setUserAccessList(List<UserAccess> userAccessList) {
        this.userAccessList = userAccessList;
    }
}
